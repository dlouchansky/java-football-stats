package com.dlouchansky.pd2;

import com.dlouchansky.pd2.application.StatsRetrievalServiceMock;
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
        ConcreteDAO.TournamentDAO tournamentDAO = new ConcreteDAO.TournamentDAO();

        DataSavingFacade savingFacade = new DataSavingFacadeImpl(
                new ConcreteDAO.VenueDAO(),
                new ConcreteDAO.RefereeDAO(),
                new ConcreteDAO.TeamDAO(),
                new ConcreteDAO.PlayerDAO(),
                new ConcreteDAO.GameCardDAO(),
                new ConcreteDAO.GameDAO(),
                new ConcreteDAO.GameRefereeDAO(),
                new ConcreteDAO.GoalDAO(),
                new ConcreteDAO.GoalPlayerDAO(),
                tournamentDAO
        );

        DataRetrievalFacade retrievalFacade = new DataRetrievalFacadeImpl(tournamentDAO);

        WebApp webApp = new WebApp(
                new XmlSavingServiceImpl(
                        savingFacade,
                        new XmlImporterImpl(),
                        retrievalFacade
                ),
                new StatsRetrievalServiceMock());

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
