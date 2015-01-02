package com.dlouchansky.pd2.application;

import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;
import com.dlouchansky.pd2.presentation.dtos.TopDTO;
import com.dlouchansky.pd2.presentation.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.presentation.dtos.TopPlayerDTO;

import java.util.List;

public interface StatsService {

    List<TopDTO> getForTop();

    /* Ten best players */
    List<TopPlayerDTO> getForTopPlayers();

    /* Five best goalkeepers */
    List<TopGoalkeeperDTO> getForTopGoalkeepers();

    /* Ten best players for team */
    List<TopPlayerDTO> getForTopPlayers(Team team);

    /* Five best goalkeepers for team */
    List<TopGoalkeeperDTO> getForTopGoalkeepers(Team team);

    List<RefereeDTO> getForRoughestReferees();

    Team getById(String teamId);

}
