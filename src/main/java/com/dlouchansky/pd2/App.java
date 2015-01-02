package com.dlouchansky.pd2;

import com.dlouchansky.pd2.application.StatsRetrievalServiceMock;
import com.dlouchansky.pd2.application.XmlDataSavingServiceImpl;
import com.dlouchansky.pd2.persistence.ConcreteDAO;
import com.dlouchansky.pd2.persistence.DataSavingFacadeImpl;
import com.dlouchansky.pd2.presentation.WebApp;
import com.dlouchansky.pd2.service.xml.XmlImporterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        WebApp webApp = new WebApp(
                new XmlDataSavingServiceImpl(
                        new DataSavingFacadeImpl(
                                new ConcreteDAO.VenueDAO(),
                                new ConcreteDAO.RefereeDAO(),
                                new ConcreteDAO.TeamDAO(),
                                new ConcreteDAO.PlayerDAO(),
                                new ConcreteDAO.GameCardDAO(),
                                new ConcreteDAO.GameDAO(),
                                new ConcreteDAO.GameRefereeDAO(),
                                new ConcreteDAO.GoalDAO(),
                                new ConcreteDAO.GoalPlayerDAO(),
                                new ConcreteDAO.TournamentDAO()
                        )
                ),
                new XmlImporterImpl(),
                new StatsRetrievalServiceMock());

        webApp.initRoutes();
    }

}
