package com.dlouchansky.pd2.application.dtos;

public class TopPlayerDTO {

    private Integer nr;
    private String name;
    private String team;
    private Integer goals;
    private Integer assists;
    private Integer gamesPlayed;
    private String minutes;
    private Integer yellowCards;
    private Integer redCards;
    private Integer number;
    private Integer cards;

    public Integer getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public Integer getGoals() {
        return goals;
    }

    public Integer getAssists() {
        return assists;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public String getMinutes() {
        return minutes;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public Integer getRedCards() {
        return redCards;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getCards() {
        return cards;
    }

    public TopPlayerDTO(Integer nr, String name, String team, Integer goals, Integer assists, Integer gamesPlayed, String minutes, Integer yellowCards, Integer redCards, Integer number, Integer cards) {

        this.nr = nr;
        this.name = name;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.gamesPlayed = gamesPlayed;
        this.minutes = minutes;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.number = number;
        this.cards = cards;
    }
}
