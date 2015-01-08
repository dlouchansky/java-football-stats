package com.dlouchansky.pd2.persistence.data.game;


import com.dlouchansky.pd2.persistence.data.Team;

import javax.persistence.*;

@Entity
@Table(name="gameTeams")
public class GameTeam {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "games_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teams_id")
    private Team team;

    @Column(name = "isWinner")
    private Boolean isWinner;

    public Game getGame() {
        return game;
    }

    public Team getTeam() {
        return team;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    public GameTeam(Game game, Team team, Boolean isWinner) {
        this.game = game;
        this.team = team;
        this.isWinner = isWinner;
    }

    public GameTeam() {

    }
}
