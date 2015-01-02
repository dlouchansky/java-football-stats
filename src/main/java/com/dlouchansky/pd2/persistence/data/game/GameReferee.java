package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Referee;

import javax.persistence.*;

@Entity
@Table(name="gameReferees")
public class GameReferee {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referees_id")
    private Referee referee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "games_id")
    private Game game;

    @Column(name="isMain")
    private Boolean isMain;

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

    public GameReferee(Referee referee, Game game, Boolean isMain) {
        this.referee = referee;
        this.game = game;
        this.isMain = isMain;
    }

    public GameReferee() {

    }

}
