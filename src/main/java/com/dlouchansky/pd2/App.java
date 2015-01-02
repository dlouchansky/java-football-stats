package com.dlouchansky.pd2;

import com.dlouchansky.pd2.application.StatsServiceMock;
import com.dlouchansky.pd2.application.XmlSavingServiceImpl;
import com.dlouchansky.pd2.persistence.*;
import com.dlouchansky.pd2.persistence.data.Tournament;
import com.dlouchansky.pd2.presentation.WebApp;
import com.dlouchansky.pd2.service.xml.XmlImporterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {

    public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        //todo find normal DI tool
        ConcreteDAO.TournamentDAO tournamentDAO = new ConcreteDAO.TournamentDAO();

        ConcreteDAO.VenueDAO venueDAO = new ConcreteDAO.VenueDAO();
        ConcreteDAO.RefereeDAO refereeDAO = new ConcreteDAO.RefereeDAO();
        ConcreteDAO.TeamDAO teamDAO = new ConcreteDAO.TeamDAO();
        ConcreteDAO.PlayerDAO playerDAO = new ConcreteDAO.PlayerDAO();
        ConcreteDAO.GameCardDAO gameCardDAO = new ConcreteDAO.GameCardDAO();
        ConcreteDAO.GameDAO gameDAO = new ConcreteDAO.GameDAO();
        ConcreteDAO.GameRefereeDAO gameRefereeDAO = new ConcreteDAO.GameRefereeDAO();
        ConcreteDAO.GoalDAO goalDAO = new ConcreteDAO.GoalDAO();
        ConcreteDAO.GoalPlayerDAO goalPlayerDAO = new ConcreteDAO.GoalPlayerDAO();

        DataCreationFacade savingFacade = new DataCreationFacadeImpl(
                venueDAO,
                refereeDAO,
                teamDAO,
                playerDAO,
                gameCardDAO,
                gameDAO,
                gameRefereeDAO,
                goalDAO,
                goalPlayerDAO,
                tournamentDAO
        );

        DataManipulationFacade manipulationFacade = new DataManipulationFacadeImpl(
                teamDAO,
                venueDAO,
                refereeDAO,
                playerDAO,
                gameCardDAO,
                gameDAO,
                gameRefereeDAO,
                goalDAO,
                goalPlayerDAO
        );

        DataRetrievalFacade retrievalFacade = new DataRetrievalFacadeImpl(tournamentDAO);

        WebApp webApp = new WebApp(
                new XmlSavingServiceImpl(
                        savingFacade,
                        new XmlImporterImpl(),
                        retrievalFacade
                ),
                new StatsServiceMock(),
                manipulationFacade
        );

        webApp.initRoutes();

        boolean uploadFileDirCreated = new File("/tmp/xmls").mkdirs();
        if (uploadFileDirCreated) {
            throw new RuntimeException("Xml upload directory not created");
        }

        if (tournamentDAO.getByYear(2014) == null) {
            tournamentDAO.add(new Tournament(2014));
        }
    }

}
