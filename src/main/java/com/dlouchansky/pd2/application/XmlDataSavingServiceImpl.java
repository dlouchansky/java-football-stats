package com.dlouchansky.pd2.application;


import com.dlouchansky.pd2.persistence.DataSavingFacade;
import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.Game;
import com.dlouchansky.pd2.persistence.data.game.GamePart;
import com.dlouchansky.pd2.persistence.data.game.Goal;
import com.dlouchansky.pd2.persistence.data.game.GoalType;
import com.dlouchansky.pd2.service.xml.data.XmlGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlDataSavingServiceImpl implements XmlDataSavingService {

    private final DataSavingFacade saver;


    public XmlDataSavingServiceImpl(DataSavingFacade saver) {
        this.saver = saver;
    }

    @Override
    public Integer saveGame(XmlGame xmlGame, Tournament tournament) {
        Venue venue = saver.createVenue(xmlGame.venue);

        Map<Integer, Player> createdPlayers = new HashMap<>();
        List<Player> createdGamePlayers = new ArrayList<>();
        List<Team> createdTeams = new ArrayList<>();
        xmlGame.teams.forEach(xmlTeam -> {
            Team createdTeam = saver.createTeam(xmlTeam.name);
            createdTeams.add(createdTeam);

            xmlTeam.players.forEach(xmlPlayer -> {
                Role playerRole;
                if (xmlPlayer.role.equals("U"))
                    playerRole = Role.Forward;
                else if (xmlPlayer.role.equals("V"))
                    playerRole = Role.Goalkeeper;
                else
                    playerRole = Role.Defender;

                Player player = saver.createPlayer(xmlPlayer.firstName, xmlPlayer.lastName, xmlPlayer.number, createdTeam, playerRole);
                createdPlayers.put(player.getNumber(), player);

                if (xmlTeam.gamePlayerIds.contains(player.getNumber())) {
                    createdGamePlayers.add(player);
                }
            });
        });

        Game createdGame = saver.createGame(xmlGame.date, xmlGame.watchers, tournament, venue, createdGamePlayers, createdTeams);

        xmlGame.referees.forEach(xmlReferee -> {
            Referee createdReferee = saver.createReferee(xmlReferee.firstName, xmlReferee.lastName);
            saver.createGameReferee(createdReferee, createdGame, xmlReferee.isMain);
        });

        xmlGame.teams.forEach(xmlTeam -> {
            xmlTeam.cards.forEach(xmlCard -> {
                saver.createCard(xmlCard.time, createdPlayers.get(xmlCard.playerId), createdGame);
            });

            xmlTeam.goals.forEach(xmlGoal -> {
                GoalType goalType = xmlGoal.sitiens ? GoalType.Penalty : GoalType.InGame;
                GamePart gamePart = xmlGoal.time > 90 * 60 ? GamePart.Extra : GamePart.Main;
                Goal createdGoal = saver.createGoal(goalType, xmlGoal.time, gamePart, createdGame);

                saver.addGoalPlayer(createdGoal, createdPlayers.get(xmlGoal.playerId), true);
                xmlGoal.goalHelperIds.forEach(goalHelper -> {
                    saver.addGoalPlayer(createdGoal, createdPlayers.get(goalHelper), false);
                });
            });
        });

        return createdGame.getId();
    }
}
