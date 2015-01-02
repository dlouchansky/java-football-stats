package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.DataRetrievalFacade;
import com.dlouchansky.pd2.persistence.DataCreationFacade;
import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.Game;
import com.dlouchansky.pd2.persistence.data.game.GamePart;
import com.dlouchansky.pd2.persistence.data.game.Goal;
import com.dlouchansky.pd2.persistence.data.game.GoalType;
import com.dlouchansky.pd2.service.xml.XmlImporter;
import com.dlouchansky.pd2.service.xml.data.XmlGame;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlSavingServiceImpl implements XmlSavingService {

    private final DataCreationFacade savingFacade;
    private final XmlImporter xmlImporter;
    private final DataRetrievalFacade retrievalFacade;

    public XmlSavingServiceImpl(DataCreationFacade savingFacade, XmlImporter xmlImporter, DataRetrievalFacade retrievalFacade) {
        this.savingFacade = savingFacade;
        this.xmlImporter = xmlImporter;
        this.retrievalFacade = retrievalFacade;
    }

    @Override
    public XmlGame parseFile(File file) {
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

    @Override
    public Integer saveGame(XmlGame xmlGame) {
        Tournament tournament = retrievalFacade.getCurrentTournament();
        return saveGame(xmlGame, tournament);
    }

    @Override
    public Integer saveGame(XmlGame xmlGame, Tournament tournament) {
        Venue venue = savingFacade.createVenue(xmlGame.venue);

        Map<Integer, Player> createdPlayers = new HashMap<>();
        List<Player> createdGamePlayers = new ArrayList<>();
        List<Team> createdTeams = new ArrayList<>();
        xmlGame.teams.forEach(xmlTeam -> {
            Team createdTeam = savingFacade.createTeam(xmlTeam.name);
            createdTeams.add(createdTeam);

            xmlTeam.players.forEach(xmlPlayer -> {
                Role playerRole;
                if (xmlPlayer.role.equals("U"))
                    playerRole = Role.Forward;
                else if (xmlPlayer.role.equals("V"))
                    playerRole = Role.Goalkeeper;
                else
                    playerRole = Role.Defender;

                Player player = savingFacade.createPlayer(xmlPlayer.firstName, xmlPlayer.lastName, xmlPlayer.number, createdTeam, playerRole);
                createdPlayers.put(player.getNumber(), player);

                if (xmlTeam.gamePlayerIds.contains(player.getNumber())) {
                    createdGamePlayers.add(player);
                }
            });
        });

        Game createdGame = savingFacade.createGame(xmlGame.date, xmlGame.watchers, tournament, venue, createdGamePlayers, createdTeams);

        xmlGame.referees.forEach(xmlReferee -> {
            Referee createdReferee = savingFacade.createReferee(xmlReferee.firstName, xmlReferee.lastName);
            savingFacade.createGameReferee(createdReferee, createdGame, xmlReferee.isMain);
        });

        xmlGame.teams.forEach(xmlTeam -> {
            xmlTeam.cards.forEach(xmlCard -> {
                savingFacade.createCard(xmlCard.time, createdPlayers.get(xmlCard.playerId), createdGame);
            });

            xmlTeam.goals.forEach(xmlGoal -> {
                GoalType goalType = xmlGoal.sitiens ? GoalType.Penalty : GoalType.InGame;
                GamePart gamePart = xmlGoal.time > 90 * 60 ? GamePart.Extra : GamePart.Main;
                Goal createdGoal = savingFacade.createGoal(goalType, xmlGoal.time, gamePart, createdGame);

                savingFacade.addGoalPlayer(createdGoal, createdPlayers.get(xmlGoal.playerId), true);
                xmlGoal.goalHelperIds.forEach(goalHelper -> {
                    savingFacade.addGoalPlayer(createdGoal, createdPlayers.get(goalHelper), false);
                });
            });
        });

        return createdGame.getId();
    }
}
