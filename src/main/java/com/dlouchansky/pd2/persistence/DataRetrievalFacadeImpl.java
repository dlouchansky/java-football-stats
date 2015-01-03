package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.persistence.data.game.GameCard;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;
import org.hibernate.Session;

public class DataRetrievalFacadeImpl implements DataRetrievalFacade {

    private final ConcreteDAO.TournamentDAO tournamentDAO;
    private final ConcreteDAO.GameDAO gameDAO;

    public DataRetrievalFacadeImpl(ConcreteDAO.TournamentDAO tournamentDAO, ConcreteDAO.GameDAO gameDAO) {
        this.tournamentDAO = tournamentDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentDAO.getByYear(2014);
    }

    @Override
    public RefereeDTO getRefereeTop() {
        Session session = null;
        RefereeDTO object = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.createSQLQuery("");
        } catch (Exception e) {
            //logger.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return object;
    }

    @Override
    public boolean checkIfExists(Integer gameTime) {
        return gameDAO.getByDate(gameTime) != null;
    }
}
