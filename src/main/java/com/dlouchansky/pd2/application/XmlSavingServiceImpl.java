package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.DataCreationFacade;
import com.dlouchansky.pd2.persistence.DataRetrievalFacade;
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
import java.util.*;

public class XmlSavingServiceImpl implements XmlSavingService {

    private final DataCreationFacade creationFacade;
    private final XmlImporter xmlImporter;
    private final DataRetrievalFacade retrievalFacade;

    public XmlSavingServiceImpl(DataCreationFacade creationFacade, XmlImporter xmlImporter, DataRetrievalFacade retrievalFacade) {
        this.creationFacade = creationFacade;
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
        Venue venue = creationFacade.createVenue(xmlGame.venue);

        Map<String, Player> createdPlayers = new HashMap<>();

        Set<Team> createdTeams = new HashSet<>();
        xmlGame.teams.forEach(xmlTeam -> {
            Team createdTeam = creationFacade.createTeam(xmlTeam.name);
            createdTeams.add(createdTeam);

            xmlTeam.players.forEach(xmlPlayer -> {
                Role playerRole;
                if (xmlPlayer.role.equals("U"))
                    playerRole = Role.Forward;
                else if (xmlPlayer.role.equals("V"))
                    playerRole = Role.Goalkeeper;
                else
                    playerRole = Role.Defender;

                Player player = creationFacade.createPlayer(xmlPlayer.firstName, xmlPlayer.lastName, xmlPlayer.number, createdTeam, playerRole);
                createdPlayers.put(xmlTeam.name + " " + player.getNumber(), player);
            });
        });

        Game createdGame = creationFacade.createGame(xmlGame.date, xmlGame.watchers, tournament, venue, createdTeams);

        xmlGame.referees.forEach(xmlReferee -> {
            Referee createdReferee = creationFacade.createReferee(xmlReferee.firstName, xmlReferee.lastName);
            creationFacade.createGameReferee(createdReferee, createdGame, xmlReferee.isMain);
        });

        xmlGame.teams.forEach(xmlTeam -> {
            xmlTeam.cards.forEach(xmlCard -> {
                creationFacade.createCard(xmlCard.time, createdPlayers.get(xmlTeam.name + " " + xmlCard.playerId), createdGame);
            });

            xmlTeam.goals.forEach(xmlGoal -> {
                GoalType goalType = xmlGoal.sitiens ? GoalType.Penalty : GoalType.InGame;
                GamePart gamePart = xmlGoal.time > 90 * 60 ? GamePart.Extra : GamePart.Main;
                Goal createdGoal = creationFacade.createGoal(goalType, xmlGoal.time, gamePart, createdGame);

                creationFacade.addGoalPlayer(createdGoal, createdPlayers.get(xmlTeam.name + " " + xmlGoal.playerId), true);
                xmlGoal.goalHelperIds.forEach(goalHelper -> {
                    creationFacade.addGoalPlayer(createdGoal, createdPlayers.get(xmlTeam.name + " " + goalHelper), false);
                });
            });

            xmlTeam.gamePlayerTimeByIds.keySet().forEach(playerId -> {
                Player player = createdPlayers.get(xmlTeam.name + " " + playerId);
                creationFacade.createGamePlayer(player, createdGame, xmlTeam.gamePlayerTimeByIds.get(playerId));
            });
        });

        return createdGame.getId();
    }
}
