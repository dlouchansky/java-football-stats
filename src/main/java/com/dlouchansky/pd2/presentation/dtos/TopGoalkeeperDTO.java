package com.dlouchansky.pd2.presentation.dtos;

public class TopGoalkeeperDTO {
    private Integer nr;
    private String name;
    private String team;
    private Double missedGoalsAverage;
    private Integer number;
    private Integer missedGoals;

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

    public Integer getNumber() {
        return number;
    }

    public Integer getMissedGoals() {
        return missedGoals;
    }

    public TopGoalkeeperDTO(Integer nr, String name, String team, Double missedGoalsAverage, Integer number, Integer missedGoals) {

        this.nr = nr;
        this.name = name;
        this.team = team;
        this.missedGoalsAverage = missedGoalsAverage;
        this.number = number;
        this.missedGoals = missedGoals;
    }
}
