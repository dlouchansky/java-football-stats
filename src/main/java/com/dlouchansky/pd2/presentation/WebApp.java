package com.dlouchansky.pd2.presentation;

import com.dlouchansky.pd2.application.StatsRetrievalService;
import com.dlouchansky.pd2.application.XmlDataSavingService;
import com.dlouchansky.pd2.persistence.data.Referee;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;
import com.dlouchansky.pd2.presentation.dtos.TopDTO;
import com.dlouchansky.pd2.presentation.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.presentation.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.service.xml.XmlImporter;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebApp {

    private final XmlDataSavingService service;
    private final XmlImporter xmlImporter;
    private final StatsRetrievalService statsService;

    public WebApp(XmlDataSavingService service,
                  XmlImporter xmlImporter,
                  StatsRetrievalService statsService
    ) {
        this.service = service;
        this.xmlImporter = xmlImporter;
        this.statsService = statsService;
    }

    public void initRoutes() {

        staticFileLocation("/src/main/resources/static");

        get("/", (req, res) -> new ModelAndView(new HashMap<String, Object>(), "home.ftl"), new FreeMarkerEngine());

        get("/top", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopDTO> results = statsService.getForTop();
            model.put("results", results);
            return new ModelAndView(model, "top.ftl");
        }, new FreeMarkerEngine());

        get("/topPlayers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopPlayerDTO> results = statsService.getForTopPlayers();
            model.put("results", results);
            return new ModelAndView(model, "topPlayers.ftl");
        }, new FreeMarkerEngine());

        get("/topGoalkeepers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopGoalkeeperDTO> results = statsService.getForTopGoalkeepers();
            model.put("results", results);
            return new ModelAndView(model, "topGoalkeepers.ftl");
        }, new FreeMarkerEngine());

        get("/referees", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<RefereeDTO> results = statsService.getForRoughestReferees();
            model.put("results", results);
            return new ModelAndView(model, "roughestReferees.ftl");
        }, new FreeMarkerEngine());

        get("/topTeamPlayers/:teamId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopGoalkeeperDTO> goalkeepers = statsService.getForTopGoalkeepers(req.params("teamId"));
            model.put("goalkeepers", goalkeepers);
            List<TopPlayerDTO> players = statsService.getForTopPlayers(req.params("teamId"));
            model.put("players", players);
            return new ModelAndView(model, "topTeamPlayers.ftl");
        }, new FreeMarkerEngine());

        get("/upload", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "upload.ftl");
        }, new FreeMarkerEngine());


        post("upload", (req, res) -> {
            res.redirect("/");
            return null;
//        post("/upload", (req, res) -> {
//            try {
//                xmlImporter.init(new File("/Users/bekas/Dropbox/LU/1 семестр/MPT/pd2/src/test/java/com/dlouchansky/pd2/futbols1.xml"));
//                System.out.println(xmlImporter.parseGame());
//            } catch (ParserConfigurationException e) {
//                e.printStackTrace();
//            } catch (SAXException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            ConcreteDAO.TournamentDAO tournamentDAO = new ConcreteDAO.TournamentDAO();
//            ConcreteDAO.GameDAO gameDAO = new ConcreteDAO.GameDAO();
//            Venue venue = new Venue("Palladium");
//            Tournament tournament = new Tournament(2010);
//            Game game = new Game(1777, venue, 10000, tournament);
//
//            gameDAO.add(game);
//            tournamentDAO.add(tournament);
//        });
        });



    }
}
