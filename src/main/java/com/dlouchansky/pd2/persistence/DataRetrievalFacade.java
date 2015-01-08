package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.application.dtos.RefereeDTO;

public interface DataRetrievalFacade {

    Tournament getCurrentTournament();

    RefereeDTO getRefereeTop();

    boolean checkIfExists(Integer gameTime, String teamName);

    Team getById(String teamId);
}
