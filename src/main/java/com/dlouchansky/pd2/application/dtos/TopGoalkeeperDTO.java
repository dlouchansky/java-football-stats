package com.dlouchansky.pd2.application.dtos;

public class TopGoalkeeperDTO {
    private Integer nr;
    private String name;
    private String team;
    private Double missedGoalsAverage;
    private Integer missedGoals;
    private Integer gameCount;
    private Integer teamId;

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public Double getMissedGoalsAverage() {
        return missedGoalsAverage;
    }

    public Integer getMissedGoals() {
        return missedGoals;
    }

    public Integer getGameCount() {
        return gameCount;
    }

    public TopGoalkeeperDTO(Integer nr, String name, String team, Double missedGoalsAverage, Integer missedGoals, Integer gameCount, Integer teamId) {
        this.nr = nr;
        this.name = name;
        this.team = team;
        this.missedGoalsAverage = missedGoalsAverage;
        this.missedGoals = missedGoals;
        this.gameCount = gameCount;
        this.teamId = teamId;
    }
}
