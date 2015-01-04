package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.DataCreationFacade;
import com.dlouchansky.pd2.persistence.DataRetrievalFacade;
import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.*;
import com.dlouchansky.pd2.service.xml.XmlImporter;
import com.dlouchansky.pd2.service.xml.data.XmlGame;
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

    Integer saveGame(XmlGame xmlGame, Tournament tournament) {
        Venue venue = creationFacade.createVenue(xmlGame.venue);

        Map<XmlTeam, Map<Integer, Player>> createdPlayersByNumberAndTeam = new HashMap<>();

        Set<Team> createdTeams = new HashSet<>();
        xmlGame.teams.forEach(xmlTeam -> {
            Team createdTeam = creationFacade.createTeam(xmlTeam.name);
            createdTeams.add(createdTeam);
            HashMap<Integer, Player> createdPlayersByNumber = new HashMap<>();
            createdPlayersByNumberAndTeam.put(xmlTeam, createdPlayersByNumber);

            xmlTeam.players.forEach(xmlPlayer -> {
                Role playerRole;
                if (xmlPlayer.role.equals("U"))
                    playerRole = Role.Forward;
                else if (xmlPlayer.role.equals("V"))
                    playerRole = Role.Goalkeeper;
                else
                    playerRole = Role.Defender;

                Player player = creationFacade.createPlayer(xmlPlayer.firstName, xmlPlayer.lastName, xmlPlayer.number, createdTeam, playerRole);
                createdPlayersByNumber.put(player.getNumber(), player);
            });
        });

        Game createdGame = creationFacade.createGame(xmlGame.date, xmlGame.watchers, tournament, venue, createdTeams);

        xmlGame.referees.forEach(xmlReferee -> {
            Referee createdReferee = creationFacade.createReferee(xmlReferee.firstName, xmlReferee.lastName);
            creationFacade.createGameReferee(createdReferee, createdGame, xmlReferee.isMain);
        });

        xmlGame.teams.forEach(xmlTeam -> {
            xmlTeam.cards.forEach(xmlCard -> {
                creationFacade.createCard(xmlCard.time, createdPlayersByNumberAndTeam.get(xmlTeam).get(xmlCard.playerId), createdGame);
            });

            xmlTeam.goals.forEach(xmlGoal -> {
                GoalType goalType = xmlGoal.sitiens ? GoalType.Penalty : GoalType.InGame;
                GamePart gamePart = xmlGoal.time > 90 * 60 ? GamePart.Extra : GamePart.Main;
                Goal createdGoal = creationFacade.createGoal(goalType, xmlGoal.time, gamePart, createdGame);

                creationFacade.addGoalPlayer(createdGoal, createdPlayersByNumberAndTeam.get(xmlTeam).get(xmlGoal.playerId), GoalPlayerType.Scorer);
                xmlGoal.goalHelperIds.forEach(goalHelper -> {
                    creationFacade.addGoalPlayer(createdGoal, createdPlayersByNumberAndTeam.get(xmlTeam).get(goalHelper), GoalPlayerType.Assist);
                });

                xmlTeam.goalkeeperNumberByTime.entrySet().forEach(entry -> {
                    Integer timeTilPlayed = entry.getKey();
                    Integer goalkeeperId = entry.getValue();
                    Player goalkeeper = createdPlayersByNumberAndTeam.get(xmlTeam).get(goalkeeperId);

                    if (xmlGoal.time <= timeTilPlayed) {
                        creationFacade.addGoalPlayer(createdGoal, goalkeeper, GoalPlayerType.Goalkeeper);
                        return;
                    }
                });

            });

            xmlTeam.gamePlayerTimeByIds.keySet().forEach(playerId -> {
                Player player = createdPlayersByNumberAndTeam.get(xmlTeam).get(playerId);
                creationFacade.createGamePlayer(player, createdGame, xmlTeam.gamePlayerTimeByIds.get(playerId));
            });
        });

        return createdGame.getId();
    }
}
