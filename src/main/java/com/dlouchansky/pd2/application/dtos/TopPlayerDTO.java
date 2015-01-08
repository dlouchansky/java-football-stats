package com.dlouchansky.pd2.application.dtos;

public class TopPlayerDTO {

    private Integer nr;
    private String name;
    private String team;
    private Integer goals;
    private Integer assists;
    private Integer gamesPlayed;
    private Integer gamesInGame;
    private String minutes;
    private Integer yellowCards;
    private Integer redCards;
    private Integer cards;
    private Integer penalties;
    private Integer teamId;

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

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public Integer getCards() {
        return cards;
    }

    public TopPlayerDTO(Integer nr, String name, String team, Integer goals, Integer assists, Integer gamesPlayed, Integer gamesInGame, String minutes, Integer yellowCards, Integer redCards, Integer cards, Integer penalties, Integer teamId) {
        this.nr = nr;
        this.name = name;
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.gamesPlayed = gamesPlayed;
        this.gamesInGame = gamesInGame;
        this.minutes = minutes;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.cards = cards;
        this.penalties = penalties;
        this.teamId = teamId;
    }

    public Integer getPenalties() {

        return penalties;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getGamesInGame() {

        return gamesInGame;
    }
}
