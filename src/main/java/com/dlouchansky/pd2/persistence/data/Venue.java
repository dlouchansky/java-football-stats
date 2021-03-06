package com.dlouchansky.pd2.persistence.data;


import com.dlouchansky.pd2.persistence.data.game.Game;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="venues")
public class Venue {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    public Venue(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy="venue")
    private Set<Game> games;

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venue() {

    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", games=" + games +
                '}';
    }
}
