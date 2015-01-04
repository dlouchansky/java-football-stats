package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;

import java.util.List;

public interface StatsRetrievalFacade {

    List<RefereeDTO> getRefereesByAverageCardsPerGame(Integer limit);

    List<TopDTO> getTeamsByPoints();

    List<TopPlayerDTO> getPlayersByReceivedCards();

    List<TopPlayerDTO> getPlayersByGoalsAndAssists(Integer limit);

    List<TopGoalkeeperDTO> getGoalkeepersByAverageMissedGoals(Integer limit);
}
