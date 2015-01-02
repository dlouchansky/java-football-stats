package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Player;
import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.persistence.data.Venue;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="games")
public class Game {
    public Game(Integer date, Venue venue, Integer watchers, Tournament tournament, List<Team> teams, List<Player> players) {
        this.date = date;
        this.venue = venue;
        this.watchers = watchers;
        this.tournament = tournament;
        this.teams = teams;
        this.players = players;
    }

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="date")
    private Integer date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venues_id")
    private Venue venue;

    @Column(name="watchers")
    private Integer watchers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tournaments_id")
    private Tournament tournament;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private List<Goal> goals;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private List<GameReferee> gameReferees;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private List<GameCard> cards;

    @ManyToMany(
            targetEntity = Team.class,
            cascade = { CascadeType.ALL }
    )
    @JoinTable(
            name = "gamePlayers",
            joinColumns = { @JoinColumn(name = "players_id") },
            inverseJoinColumns = { @JoinColumn(name = "gameTeams_id") }
    )
    private List<Team> teams;

    @ManyToMany(
            targetEntity = Player.class,
            cascade = { CascadeType.ALL }
    )
    @JoinTable(
            name = "gamePlayers",
            joinColumns = { @JoinColumn(name = "players_id") },
            inverseJoinColumns = { @JoinColumn(name = "gameTeams_id") }
    )
    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Integer getWatchers() {
        return watchers;
    }

    public void setWatchers(Integer watchers) {
        this.watchers = watchers;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    public List<GameReferee> getGameReferees() {
        return gameReferees;
    }

    public void setGameReferees(List<GameReferee> gameReferees) {
        this.gameReferees = gameReferees;
    }

    public List<GameCard> getCards() {
        return cards;
    }

    public void setCards(List<GameCard> cards) {
        this.cards = cards;
    }

    public Game() {

    }
}
