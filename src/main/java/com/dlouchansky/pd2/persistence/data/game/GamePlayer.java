package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Player;

import javax.persistence.*;

@Entity
@Table(name="gamePlayers")
public class GamePlayer {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "games_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "players_id")
    private Player player;

    @Column(name="duration")
    private Integer duration;

    public GamePlayer(Game game, Player player, Integer duration) {
        this.game = game;
        this.player = player;
        this.duration = duration;
    }

    public GamePlayer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
