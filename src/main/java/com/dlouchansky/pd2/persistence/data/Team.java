package com.dlouchansky.pd2.persistence.data;

import com.dlouchansky.pd2.persistence.data.game.Game;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="teams")
public class Team {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @ManyToMany(
            mappedBy = "teams",
            targetEntity = Game.class,
            cascade = {}
    )
    private Set<Game> games;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="team")
    private Set<Player> players;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team() {

    }
}
