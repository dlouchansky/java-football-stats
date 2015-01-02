package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.*;

import java.util.List;

public interface DataSavingFacade {
    Venue createVenue(String venue);

    Referee createReferee(String firstName, String lastName);

    Team createTeam(String name);

    Player createPlayer(String firstName, String lastName, Integer number, Team team, Role role);

    Game createGame(Integer date, Integer watchers, Tournament tournament, Venue venue, List<Player> gamePlayers, List<Team> gameTeams);

    GameReferee createGameReferee(Referee referee, Game game, Boolean isMain);

    GameCard createCard(Integer time, Player player, Game game);

    Goal createGoal(GoalType goalType, Integer time, GamePart gamePart, Game game);

    GoalPlayer addGoalPlayer(Goal goal, Player player, Boolean isScorer);
}
