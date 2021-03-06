package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Player;
import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.persistence.data.Venue;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="games")
public class Game {
    public Game(Integer date, Venue venue, Integer watchers, Tournament tournament, GamePart winGamePart) {
        this.date = date;
        this.venue = venue;
        this.watchers = watchers;
        this.tournament = tournament;
        this.winGamePart = winGamePart;
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

    public GamePart getWinGamePart() {
        return winGamePart;
    }

    public void setWinGamePart(GamePart winGamePart) {
        this.winGamePart = winGamePart;
    }

    @Column(name = "winGamePart")
    @Enumerated(EnumType.ORDINAL)
    private GamePart winGamePart;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private Set<Goal> goals;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private Set<GameReferee> gameReferees;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private Set<GameCard> cards;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private Set<GameTeam> gameTeams;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="game")
    private Set<GamePlayer> gamePlayers;

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
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

    public Set<GameTeam> getGameTeams() {
        return gameTeams;
    }

    public void setGameTeams(Set<GameTeam> gameTeams) {
        this.gameTeams = gameTeams;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    public Set<GameReferee> getGameReferees() {
        return gameReferees;
    }

    public void setGameReferees(Set<GameReferee> gameReferees) {
        this.gameReferees = gameReferees;
    }

    public Set<GameCard> getCards() {
        return cards;
    }

    public void setCards(Set<GameCard> cards) {
        this.cards = cards;
    }

    public Game() {

    }
}
