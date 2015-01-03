package com.dlouchansky.pd2.persistence.data;

import com.dlouchansky.pd2.persistence.data.game.Game;
import com.dlouchansky.pd2.persistence.data.game.GamePlayer;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teams_id")
    private Team team;

    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="player")
    private Set<GamePlayer> gamePlayers;

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Player() {
    }

    public Player(String firstName, String lastName, Integer number, Team team, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.team = team;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Integer getNumber() {
        return number;
    }

    public Team getTeam() {
        return team;
    }

    public Role getRole() {
        return role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
