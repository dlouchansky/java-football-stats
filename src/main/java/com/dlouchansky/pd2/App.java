package com.dlouchansky.pd2;

import com.dlouchansky.pd2.application.*;
import com.dlouchansky.pd2.persistence.*;
import com.dlouchansky.pd2.persistence.daos.ConcreteDAO;
import com.dlouchansky.pd2.presentation.WebApp;
import com.dlouchansky.pd2.service.xml.XmlImporterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.template.freemarker.FreeMarkerEngine;

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
        ConcreteDAO.GamePlayerDAO gamePlayerDAO = new ConcreteDAO.GamePlayerDAO();
        ConcreteDAO.GameTeamDAO gameTeamDAO = new ConcreteDAO.GameTeamDAO();

        XmlImporterImpl xmlImporter = new XmlImporterImpl();
        StatsService statsService = new StatsServiceImpl(new StatsRetrievalFacadeImpl());

        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();

        DataCreationFacade creationFacade = new DataCreationFacadeImpl(
                venueDAO,
                refereeDAO,
                teamDAO,
                playerDAO,
                gameCardDAO,
                gameDAO,
                gameRefereeDAO,
                goalDAO,
                goalPlayerDAO,
                tournamentDAO,
                gamePlayerDAO,
                gameTeamDAO);

        DataManipulationFacade manipulationFacade = new DataManipulationFacadeImpl(
                teamDAO,
                venueDAO,
                refereeDAO,
                playerDAO,
                gameCardDAO,
                gameDAO,
                gameRefereeDAO,
                goalDAO,
                goalPlayerDAO,
                gamePlayerDAO
        );

        DataRetrievalFacade retrievalFacade = new DataRetrievalFacadeImpl(
                tournamentDAO,
                gameDAO,
                teamDAO
        );

        XmlSavingService xmlService = new XmlSavingServiceImpl(
                creationFacade,
                xmlImporter,
                retrievalFacade
        );

        WebApp webApp = new WebApp(
                xmlService,
                statsService,
                manipulationFacade,
                retrievalFacade,
                freeMarkerEngine);

        webApp.initRoutes();

        boolean uploadFileDirCreated = new File("/tmp/xmls").mkdirs();
        if (uploadFileDirCreated) {
            throw new RuntimeException("Xml upload directory not created");
        }

        if (tournamentDAO.getByYear(2014) == null) {
            creationFacade.createTournament(2014);
        }
    }

}
