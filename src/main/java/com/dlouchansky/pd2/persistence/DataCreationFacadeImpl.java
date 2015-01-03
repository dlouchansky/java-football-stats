package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.*;
import com.dlouchansky.pd2.persistence.data.game.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataCreationFacadeImpl implements DataCreationFacade {

    private final ConcreteDAO.TeamDAO teamDAO;
    private final ConcreteDAO.VenueDAO venueDAO;
    private final ConcreteDAO.RefereeDAO refereeDAO;
    private final ConcreteDAO.PlayerDAO playerDAO;
    private final ConcreteDAO.GameCardDAO gameCardDAO;
    private final ConcreteDAO.GameDAO gameDAO;
    private final ConcreteDAO.GameRefereeDAO gameRefereeDAO;
    private final ConcreteDAO.GoalDAO goalDAO;
    private final ConcreteDAO.GoalPlayerDAO goalPlayerDAO;
    private final ConcreteDAO.TournamentDAO tournamentDAO;
    private final ConcreteDAO.GamePlayerDAO gamePlayerDAO;

    public DataCreationFacadeImpl(
            ConcreteDAO.VenueDAO venueDAO,
            ConcreteDAO.RefereeDAO refereeDAO,
            ConcreteDAO.TeamDAO teamDAO,
            ConcreteDAO.PlayerDAO playerDAO,
            ConcreteDAO.GameCardDAO gameCardDAO,
            ConcreteDAO.GameDAO gameDAO,
            ConcreteDAO.GameRefereeDAO gameRefereeDAO,
            ConcreteDAO.GoalDAO goalDAO,
            ConcreteDAO.GoalPlayerDAO goalPlayerDAO,
            ConcreteDAO.TournamentDAO tournamentDAO,
            ConcreteDAO.GamePlayerDAO gamePlayerDAO
    ) {
        this.venueDAO = venueDAO;
        this.refereeDAO = refereeDAO;
        this.teamDAO = teamDAO;
        this.playerDAO = playerDAO;
        this.gameCardDAO = gameCardDAO;
        this.gameDAO = gameDAO;
        this.gameRefereeDAO = gameRefereeDAO;
        this.goalDAO = goalDAO;
        this.goalPlayerDAO = goalPlayerDAO;
        this.tournamentDAO = tournamentDAO;
        this.gamePlayerDAO = gamePlayerDAO;
    }

    @Override
    public Tournament createTournament(Integer year) {
        Tournament tournament = new Tournament(year);
        tournamentDAO.add(tournament);
        return tournament;
    }

    public Venue createVenue(String venueName) {
        Venue newVenue = venueDAO.getByName(venueName);
        if (newVenue == null) {
            newVenue = new Venue(venueName);
            venueDAO.add(newVenue);
        }
        return newVenue;
    }

    public Referee createReferee(String firstName, String lastName) {
        Referee newReferee = refereeDAO.getByName(firstName, lastName);
        if (newReferee == null) {
            newReferee = new Referee(firstName, lastName);
            refereeDAO.add(newReferee);
        }
        return newReferee;
    }

    public Team createTeam(String name) {
        Team newTeam = teamDAO.getByName(name);
        if (newTeam == null) {
            newTeam = new Team(name);
            teamDAO.add(newTeam);
        }
        return newTeam;
    }

    public Player createPlayer(String firstName, String lastName, Integer number, Team team, Role role) {
        if (team == null) throw new RuntimeException("Team not created");

        Player newPlayer;
        List<Player> foundPlayers = playerDAO.getByName(firstName, lastName);
        if (foundPlayers.size() == 0) {
            newPlayer = new Player(firstName, lastName, number, team, role);
            playerDAO.add(newPlayer);
            teamDAO.refresh(team);
        } else {
            List<Player> playerInTeam = foundPlayers.stream().filter(p -> p.getTeam().getId().equals(team.getId())).collect(Collectors.toList());
            if (playerInTeam.size() == 0) {
                newPlayer = new Player(firstName, lastName, number, team, role);
                playerDAO.add(newPlayer);
                teamDAO.refresh(team);
            } else {
                newPlayer = playerInTeam.get(0);
            }
        }

        return newPlayer;
    }

    public Game createGame(Integer date, Integer watchers, Tournament tournament, Venue venue, Set<Team> gameTeams) {
        Game game = new Game(date, venue, watchers, tournament, gameTeams);
        gameDAO.add(game);
        tournamentDAO.refresh(tournament);
        venueDAO.refresh(venue);
        gameTeams.forEach(teamDAO::refresh);
        return game;
    }

    @Override
    public GamePlayer createGamePlayer(Player player, Game game, Integer duration) {
        GamePlayer gamePlayer = new GamePlayer(game, player, duration);
        gamePlayerDAO.add(gamePlayer);
        gameDAO.refresh(game);
        playerDAO.refresh(player);
        return gamePlayer;
    }

    public GameReferee createGameReferee(Referee referee, Game game, Boolean isMain) {
        GameReferee gameReferee = new GameReferee(referee, game, isMain);
        gameRefereeDAO.add(gameReferee);
        gameDAO.refresh(game);
        refereeDAO.refresh(referee);
        return gameReferee;
    }

    public GameCard createCard(Integer time, Player player, Game game) {
        GameCard gameCard = gameCardDAO.getByPlayerAndGame(player, game);
        if (gameCard != null) {
            gameCard.setType(Card.Red);
            gameCardDAO.update(gameCard);
        } else {
            gameCard = new GameCard(Card.Yellow, player, game);
            gameCardDAO.add(gameCard);
            System.out.println(gameCard);
            System.out.println(game.getCards());
            gameDAO.refresh(game);
            System.out.println(game.getCards());
            playerDAO.refresh(player);
        }

        return gameCard;
    }

    public Goal createGoal(GoalType goalType, Integer time, GamePart gamePart, Game game) {
        Goal goal = new Goal(goalType, time, gamePart, game);
        goalDAO.add(goal);
        gameDAO.refresh(game);
        return goal;
    }

    public GoalPlayer addGoalPlayer(Goal goal, Player player, Boolean isScorer) {
        GoalPlayer goalPlayer = new GoalPlayer(goal, player, isScorer);
        goalPlayerDAO.add(goalPlayer);
        playerDAO.refresh(player);
        return goalPlayer;
    }
}
