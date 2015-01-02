package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Player;

import javax.persistence.*;

@Entity
@Table(name = "gameCards")
public class GameCard {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "card")
    @Enumerated(EnumType.ORDINAL)
    private Card type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "players_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "games_id")
    private Game game;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Card getType() {
        return type;
    }

    public void setType(Card type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public GameCard(Card type, Player player, Game game) {
        this.type = type;
        this.player = player;
        this.game = game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameCard() {

    }
}
