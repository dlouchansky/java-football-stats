package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.persistence.data.Team;

import java.util.List;

public interface StatsRetrievalFacade {

    List<RefereeDTO> getRefereesByAverageCardsPerGame(Integer limit);

    List<TopDTO> getTeamsByPoints();

    List<TopPlayerDTO> getPlayersByReceivedCards();

    List<TopPlayerDTO> getPlayersByGoalsAndAssists(Integer limit);

    List<TopGoalkeeperDTO> getGoalkeepersByAverageMissedGoals(Integer limit);

    List<TopGoalkeeperDTO> getGoalkeepersForTeam(Team team);

    List<TopPlayerDTO> getPlayersForTeam(Team team);

    List<TopPlayerDTO> getPlayersByDuration(Integer limit);

    List<TopPlayerDTO> getPlayersByScoredPenalties(Integer limit);
}
