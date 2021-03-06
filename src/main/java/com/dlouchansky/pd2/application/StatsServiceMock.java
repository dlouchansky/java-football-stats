package com.dlouchansky.pd2.application;

import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;

import java.util.ArrayList;
import java.util.List;

public class StatsServiceMock implements StatsService {
    @Override
    public List<TopDTO> getForTop() {
        List<TopDTO> result = new ArrayList<>();
        result.add(new TopDTO(1, 333, "team1", 6, 7, 5, 4, 3, 2, 1));
        result.add(new TopDTO(2, 444, "team2", 6, 5, 4, 3, 2, 1, 0));
        result.add(new TopDTO(3, 555, "team3", 5, 4, 3, 2, 1, 0, -1));
        return result;
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers() {
        List<TopPlayerDTO> result = new ArrayList<>();
        result.add(new TopPlayerDTO(1, "player 1", "team 1", 5, 2, 5, 5, "0:01", 5, 5, 5, 0, 0));
        result.add(new TopPlayerDTO(2, "player 2", "team 2", 4, 1, 5, 5, "0:33", 5, 5, 5, 0, 0));
        result.add(new TopPlayerDTO(3, "player 3", "team 3", 3, 0, 5, 5, "11:11", 5, 5, 5, 0, 0));
        return result;
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers() {
        List<TopGoalkeeperDTO> result = new ArrayList<>();
        result.add(new TopGoalkeeperDTO(1, "gk1", "team1", Double.parseDouble("5.55"), 1, 4, 0));
        result.add(new TopGoalkeeperDTO(2, "gk2", "team2", Double.parseDouble("55.55"), 2, 5, 0));
        result.add(new TopGoalkeeperDTO(3, "gk3", "team3", Double.parseDouble("555.555"), 3, 6, 0));
        return result;
    }

    @Override
    public List<TopPlayerDTO> getForTopPlayers(Team team) {
        return getForTopPlayers();
    }

    @Override
    public List<TopGoalkeeperDTO> getForTopGoalkeepers(Team team) {
        return getForTopGoalkeepers();
    }

    @Override
    public List<RefereeDTO> getForRoughestReferees() {
        List<RefereeDTO> result = new ArrayList<>();
        result.add(new RefereeDTO(1, "ref1", Double.parseDouble("5.55"), 0, 0));
        result.add(new RefereeDTO(2, "ref2", Double.parseDouble("56.555"), 0, 0));
        result.add(new RefereeDTO(3, "ref3", Double.parseDouble("588.555"), 0, 0));
        return result;
    }

    @Override
    public List<TopPlayerDTO> getForRudePlayers() {
        return getForTopPlayers();
    }

    @Override
    public List<TopPlayerDTO> getForPlayersByDuration() {
        return null;
    }

    @Override
    public List<TopPlayerDTO> getForPlayersByPenalties() {
        return null;
    }
}
