package com.dlouchansky.pd2.persistence;

import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;

public interface DataRetrievalFacade {

    Tournament getCurrentTournament();

    RefereeDTO getRefereeTop();

    boolean checkIfExists(Integer gameTime);
}
