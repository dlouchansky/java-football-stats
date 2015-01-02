package com.dlouchansky.pd2.persistence.data;

import com.dlouchansky.pd2.persistence.data.game.Game;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class Tournament {

    public Tournament(Integer year) {
        this.year = year;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")

    private Integer id;

    @Column(name = "year")
    private Integer year;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="tournament")
    private List<Game> games;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Tournament() {

    }
}
