package com.dlouchansky.pd2.application;

import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.persistence.StatsRetrievalFacade;
import com.dlouchansky.pd2.persistence.data.Team;

import java.util.List;

public class StatsServiceImpl implements StatsService {

    private final StatsRetrievalFacade statsRetrievalFacade;

    public StatsServiceImpl(StatsRetrievalFacade statsRetrievalFacade) {
        this.statsRetrievalFacade = statsRetrievalFacade;
    }

    @Override
    public List<TopDTO> getForTop() {
        return statsRetrievalFacade.getTeamsByPoints();
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers() {
        return statsRetrievalFacade.getPlayersByGoalsAndAssists(10);
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers() {
        return statsRetrievalFacade.getGoalkeepersByAverageMissedGoals(5);
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers(Team team) {
        return statsRetrievalFacade.getPlayersForTeam(team);
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers(Team team) {
        return statsRetrievalFacade.getGoalkeepersForTeam(team);
    }

    @Override
    public List<RefereeDTO> getForRoughestReferees() {
        return statsRetrievalFacade.getRefereesByAverageCardsPerGame(10);
    }

    @Override
    public List<TopPlayerDTO> getForRudePlayers() {
        return statsRetrievalFacade.getPlayersByReceivedCards();
    }

    @Override
    public List<TopPlayerDTO> getForPlayersByDuration() {
        return statsRetrievalFacade.getPlayersByDuration(10);
    }

    @Override
    public List<TopPlayerDTO> getForPlayersByPenalties() {
        return statsRetrievalFacade.getPlayersByScoredPenalties(10);
    }
}
