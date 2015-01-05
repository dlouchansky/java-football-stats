package com.dlouchansky.pd2.persistence.data.game;


import com.dlouchansky.pd2.persistence.data.Team;

public class GameTeam {

    private Game game;
    private Team team;
    private Boolean isWinner;
    private GamePart winGamePart;

    public Game getGame() {
        return game;
    }

    public Team getTeam() {
        return team;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public GamePart getWinGamePart() {
        return winGamePart;
    }

    public GameTeam(Game game, Team team, Boolean isWinner, GamePart winGamePart) {

        this.game = game;
        this.team = team;
        this.isWinner = isWinner;
        this.winGamePart = winGamePart;
    }

    public GameTeam() {

    }
}
