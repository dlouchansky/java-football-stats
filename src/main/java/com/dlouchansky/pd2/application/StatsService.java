package com.dlouchansky.pd2.application;

import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;

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

    List<TopPlayerDTO> getForRudePlayers();

    List<TopPlayerDTO> getForPlayersByDuration();

    List<TopPlayerDTO> getForPlayersByPenalties();
}
