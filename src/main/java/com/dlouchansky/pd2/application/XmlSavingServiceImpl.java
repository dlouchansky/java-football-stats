package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.DataCreationFacade;
import com.dlouchansky.pd2.persistence.DataRetrievalFacade;
import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.*;
import com.dlouchansky.pd2.service.xml.XmlImporter;
import com.dlouchansky.pd2.service.xml.data.XmlGame;
import com.dlouchansky.pd2.service.xml.data.XmlGoal;
import com.dlouchansky.pd2.service.xml.data.XmlPlayer;
import com.dlouchansky.pd2.service.xml.data.XmlTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XmlSavingServiceImpl implements XmlSavingService {

    private static Logger logger = LoggerFactory.getLogger(XmlSavingServiceImpl.class);

    private final DataCreationFacade creationFacade;
    private final XmlImporter xmlImporter;
    private final DataRetrievalFacade retrievalFacade;

    public XmlSavingServiceImpl(DataCreationFacade creationFacade, XmlImporter xmlImporter, DataRetrievalFacade retrievalFacade) {
        this.creationFacade = creationFacade;
        this.xmlImporter = xmlImporter;
        this.retrievalFacade = retrievalFacade;
    }

    @Override
    public boolean addXml(File file) {
        XmlGame game = parseFile(file);

        if (!retrievalFacade.checkIfExists(game.date)) {
            saveGame(game);
            return true;
        }

        return false;
    }

    XmlGame parseFile(File file) {
        XmlGame game = null;
        try {
            xmlImporter.init(file);
            game = xmlImporter.parseGame();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return game;
    }

    Integer saveGame(XmlGame xmlGame) {
        Tournament tournament = retrievalFacade.getCurrentTournament();
        return saveGame(xmlGame, tournament);
    }

    protected GamePart getWinGamePart(XmlTeam winnerTeam) {
        GamePart winGamePart;
        Integer lastGoalTime = winnerTeam.goals.stream().max((goal1, goal2) -> Integer.compare(goal1.time, goal2.time)).get().time;
        if (lastGoalTime <= 60*60)
            winGamePart = GamePart.Main;
        else
            winGamePart = GamePart.Extra;
        return winGamePart;
    }

    protected Role getPlayerRole(XmlPlayer xmlPlayer) {
        Role playerRole;
        if (xmlPlayer.role.equals("U"))
            playerRole = Role.Forward;
        else if (xmlPlayer.role.equals("V"))
            playerRole = Role.Goalkeeper;
        else
            playerRole = Role.Defender;
        return playerRole;
    }

    Integer saveGame(XmlGame xmlGame, Tournament tournament) {
        XmlTeam team1 = xmlGame.teams.get(0);
        XmlTeam team2 = xmlGame.teams.get(1);

        XmlTeam winnerTeam = team1.goals.size() > team2.goals.size() ? team1 : team2;
        GamePart winGamePart = getWinGamePart(winnerTeam);

        Venue venue = creationFacade.createVenue(xmlGame.venue);

        Map<XmlTeam, Map<Integer, Player>> createdPlayersByNumberAndTeam = new HashMap<>();

        Set<Team> createdTeams = new HashSet<>();
        xmlGame.teams.forEach(xmlTeam -> {
            Team createdTeam = creationFacade.createTeam(xmlTeam.name);
            createdTeams.add(createdTeam);
            HashMap<Integer, Player> createdPlayersByNumber = new HashMap<>();
            createdPlayersByNumberAndTeam.put(xmlTeam, createdPlayersByNumber);

            xmlTeam.players.forEach(xmlPlayer -> {
                Role playerRole = getPlayerRole(xmlPlayer);
                Player player = creationFacade.createPlayer(xmlPlayer.firstName, xmlPlayer.lastName, xmlPlayer.number, createdTeam, playerRole);
                createdPlayersByNumber.put(player.getNumber(), player);
            });
        });

        Game createdGame = creationFacade.createGame(xmlGame.date, xmlGame.watchers, tournament, venue, winGamePart);

        createdTeams.forEach(team -> {
            creationFacade.createGameTeam(createdGame, team, team.getName().equals(winnerTeam.name));
        });

        xmlGame.referees.forEach(xmlReferee -> {
            Referee createdReferee = creationFacade.createReferee(xmlReferee.firstName, xmlReferee.lastName);
            creationFacade.createGameReferee(createdReferee, createdGame, xmlReferee.isMain);
        });

        xmlGame.teams.forEach(xmlTeam -> {
            Map<Integer, Player> createdPlayersByNumber = createdPlayersByNumberAndTeam.get(xmlTeam);

            xmlTeam.cards.forEach(xmlCard -> {
                creationFacade.createCard(xmlCard.time, createdPlayersByNumber.get(xmlCard.playerId), createdGame);
            });

            createdPlayersByNumber.keySet().forEach(createdPlayerNumber -> {
                if (!xmlTeam.gamePlayerTimeByNumber.keySet().contains(createdPlayerNumber)) {
                    // create gamePlayer if was on game but not participated
                    creationFacade.createGamePlayer(createdPlayersByNumber.get(createdPlayerNumber), createdGame, 0);
                } else {
                    Integer duration = xmlTeam.gamePlayerTimeByNumber.get(createdPlayerNumber);
                    creationFacade.createGamePlayer(createdPlayersByNumber.get(createdPlayerNumber), createdGame, duration);
                }
            });
        });

        team1.goals.forEach(xmlGoal -> {
            Goal createdGoal = createGoal(xmlGoal, createdGame);
            addGoalScorerAndAssists(createdGoal, createdPlayersByNumberAndTeam.get(team1), xmlGoal);
            connectGoalWithOppositeTeamGoalkeeper(team2, createdPlayersByNumberAndTeam.get(team2), xmlGoal, createdGoal);
        });

        team2.goals.forEach(xmlGoal -> {
            Goal createdGoal = createGoal(xmlGoal, createdGame);
            addGoalScorerAndAssists(createdGoal, createdPlayersByNumberAndTeam.get(team2), xmlGoal);
            connectGoalWithOppositeTeamGoalkeeper(team1, createdPlayersByNumberAndTeam.get(team1), xmlGoal, createdGoal);
        });

        return createdGame.getId();
    }

    protected Goal createGoal(XmlGoal xmlGoal, Game createdGame) {
        GoalType goalType = xmlGoal.sitiens ? GoalType.Penalty : GoalType.InGame;
        GamePart gamePart = xmlGoal.time > 90 * 60 ? GamePart.Extra : GamePart.Main;
        return creationFacade.createGoal(goalType, xmlGoal.time, gamePart, createdGame);
    }

    protected void addGoalScorerAndAssists(Goal createdGoal, Map<Integer, Player> createdPlayersByNumber, XmlGoal xmlGoal) {
        creationFacade.addGoalPlayer(createdGoal, createdPlayersByNumber.get(xmlGoal.playerId), GoalPlayerType.Scorer);
        xmlGoal.goalHelperIds.forEach(goalHelper -> {
            creationFacade.addGoalPlayer(createdGoal, createdPlayersByNumber.get(goalHelper), GoalPlayerType.Assist);
        });
    }

    protected void connectGoalWithOppositeTeamGoalkeeper(XmlTeam oppositeTeam, Map<Integer, Player> createdPlayersByNumber, XmlGoal xmlGoal, Goal createdGoal) {
        oppositeTeam.goalkeeperNumberByTime.entrySet().forEach(entry -> {
            Integer timeTilPlayed = entry.getKey();
            Integer goalkeeperId = entry.getValue();
            Player goalkeeper = createdPlayersByNumber.get(goalkeeperId);

            if (xmlGoal.time <= timeTilPlayed) {
                creationFacade.addGoalPlayer(createdGoal, goalkeeper, GoalPlayerType.Goalkeeper);
                return;
            }
        });
    }
}
