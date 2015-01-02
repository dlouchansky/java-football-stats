package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.Tournament;

public class DataRetrievalFacadeImpl implements DataRetrievalFacade {

    private final ConcreteDAO.TournamentDAO tournamentDAO;

    public DataRetrievalFacadeImpl(ConcreteDAO.TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    @Override
    public Tournament getCurrentTournament() {
        return tournamentDAO.getByYear(2014);
    }
}
