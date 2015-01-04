package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.daos.ConcreteDAO;

public class DataManipulationFacadeImpl implements DataManipulationFacade {

    private final ConcreteDAO.TeamDAO teamDAO;
    private final ConcreteDAO.VenueDAO venueDAO;
    private final ConcreteDAO.RefereeDAO refereeDAO;
    private final ConcreteDAO.PlayerDAO playerDAO;
    private final ConcreteDAO.GameCardDAO gameCardDAO;
    private final ConcreteDAO.GameDAO gameDAO;
    private final ConcreteDAO.GameRefereeDAO gameRefereeDAO;
    private final ConcreteDAO.GoalDAO goalDAO;
    private final ConcreteDAO.GoalPlayerDAO goalPlayerDAO;
    private final ConcreteDAO.GamePlayerDAO gamePlayerDAO;

    public DataManipulationFacadeImpl(
            ConcreteDAO.TeamDAO teamDAO,
            ConcreteDAO.VenueDAO venueDAO,
            ConcreteDAO.RefereeDAO refereeDAO,
            ConcreteDAO.PlayerDAO playerDAO,
            ConcreteDAO.GameCardDAO gameCardDAO,
            ConcreteDAO.GameDAO gameDAO,
            ConcreteDAO.GameRefereeDAO gameRefereeDAO,
            ConcreteDAO.GoalDAO goalDAO,
            ConcreteDAO.GoalPlayerDAO goalPlayerDAO,
            ConcreteDAO.GamePlayerDAO gamePlayerDAO
    ) {
        this.teamDAO = teamDAO;
        this.venueDAO = venueDAO;
        this.refereeDAO = refereeDAO;
        this.playerDAO = playerDAO;
        this.gameCardDAO = gameCardDAO;
        this.gameDAO = gameDAO;
        this.gameRefereeDAO = gameRefereeDAO;
        this.goalDAO = goalDAO;
        this.goalPlayerDAO = goalPlayerDAO;
        this.gamePlayerDAO = gamePlayerDAO;
    }

    @Override
    public void clearDatabase() {
        gameRefereeDAO.deleteAll();
        gamePlayerDAO.deleteAll();
        refereeDAO.deleteAll();


        goalPlayerDAO.deleteAll();
        goalDAO.deleteAll();


        gameCardDAO.deleteAll();
        gameDAO.deleteAll();


        teamDAO.deleteAll();
        playerDAO.deleteAll();
        venueDAO.deleteAll();
    }
}
