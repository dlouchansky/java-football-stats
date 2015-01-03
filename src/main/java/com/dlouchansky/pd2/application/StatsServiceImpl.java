package com.dlouchansky.pd2.application;

import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;
import com.dlouchansky.pd2.presentation.dtos.TopDTO;
import com.dlouchansky.pd2.presentation.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.presentation.dtos.TopPlayerDTO;

import java.util.List;

public class StatsServiceImpl implements StatsService {
    @Override
    public List<TopDTO> getForTop() {
        return null;
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers() {
        return null;
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers() {
        return null;
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers(Team team) {
        return null;
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers(Team team) {
        return null;
    }

    @Override
    public List<RefereeDTO> getForRoughestReferees() {

        return null;
    }

    @Override
    public Team getById(String teamId) {
        return null;
    }

    @Override
    public List<TopPlayerDTO> getForRudePlayers() {
        return null;
    }
}
