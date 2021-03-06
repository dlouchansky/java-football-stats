package com.dlouchansky.pd2.presentation;

import com.dlouchansky.pd2.application.StatsService;
import com.dlouchansky.pd2.application.XmlSavingService;
import com.dlouchansky.pd2.application.dtos.RefereeDTO;
import com.dlouchansky.pd2.application.dtos.TopDTO;
import com.dlouchansky.pd2.application.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.application.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.persistence.DataManipulationFacade;
import com.dlouchansky.pd2.persistence.DataRetrievalFacade;
import com.dlouchansky.pd2.persistence.data.Team;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class WebApp {

    private static Logger logger = LoggerFactory.getLogger(WebApp.class);

    private final XmlSavingService xmlService;
    private final StatsService statsService;
    private final DataManipulationFacade dataManipulationFacade;
    private final DataRetrievalFacade dataRetrievalFacade;
    private final FreeMarkerEngine freeMarkerEngine;

    public WebApp(XmlSavingService xmlService,
                  StatsService statsService,
                  DataManipulationFacade dataManipulationFacade,
                  DataRetrievalFacade dataRetrievalFacade,
                  FreeMarkerEngine freeMarkerEngine
    ) {
        this.xmlService = xmlService;
        this.statsService = statsService;
        this.dataManipulationFacade = dataManipulationFacade;
        this.dataRetrievalFacade = dataRetrievalFacade;
        this.freeMarkerEngine = freeMarkerEngine;
    }

    public void initRoutes() {

        staticFileLocation("/static");


        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if ("true".equals(req.cookie("truncated"))) {
                res.cookie("truncated", "false");
                model.put("truncated", true);
            } else {
                model.put("truncated", false);
            }
            return new ModelAndView(model, "home.ftl");
        }, freeMarkerEngine);

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

        get("/rude", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopPlayerDTO> results = statsService.getForRudePlayers();
            model.put("results", results);
            return new ModelAndView(model, "rudePlayers.ftl");
        }, new FreeMarkerEngine());

        get("/duration", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopPlayerDTO> results = statsService.getForPlayersByDuration();
            model.put("results", results);
            return new ModelAndView(model, "topDuration.ftl");
        }, new FreeMarkerEngine());

        get("/penalties", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<TopPlayerDTO> results = statsService.getForPlayersByPenalties();
            model.put("results", results);
            return new ModelAndView(model, "topPenalties.ftl");
        }, new FreeMarkerEngine());

        get("/topTeamPlayers/:teamId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Team team = dataRetrievalFacade.getById(req.params("teamId"));
            List<TopGoalkeeperDTO> goalkeepers = statsService.getForTopGoalkeepers(team);
            List<TopPlayerDTO> players = statsService.getForTopPlayers(team);

            model.put("goalkeepers", goalkeepers);
            model.put("players", players);
            model.put("team", team.getName());
            return new ModelAndView(model, "topTeamPlayers.ftl");
        }, new FreeMarkerEngine());

        get("/upload", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if ("true".equals(req.cookie("uploaded"))) {
                res.cookie("uploaded", "false");
                model.put("uploaded", true);
            } else {
                model.put("uploaded", false);
            }
            return new ModelAndView(model, "upload.ftl");
        }, new FreeMarkerEngine());

        post("/truncate", (req, res) -> {
            dataManipulationFacade.clearDatabase();
            res.cookie("truncated", "true");
            res.redirect("/");
            return null;
        });

        post("/upload", (req, res) -> {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            req.raw().setAttribute("org.eclipse.multipartConfig", multipartConfigElement);
            Collection<Part> reqParts = req.raw().getParts();

            reqParts.forEach(part -> {
                if ("filesToUpload".equals(part.getName())) {
                    MultiPartInputStreamParser.MultiPart partMulti = (MultiPartInputStreamParser.MultiPart) part;
                    String contentDispositionFilename = partMulti.getContentDispositionFilename();
                    File dest = new File("/tmp/xmls/" + System.currentTimeMillis() + contentDispositionFilename);
                    if (!partMulti.getFile().renameTo(dest)) {
                        throw new RuntimeException("file not renamed");
                    }
                    boolean xmlAdded = xmlService.addXml(dest);
                    if (!xmlAdded)
                        logger.info("Game " + contentDispositionFilename + " already exists");
                    else
                        logger.info("Game " + contentDispositionFilename + " added");
                }
            });
            res.cookie("uploaded", "true");
            res.redirect("/upload");
            return null;
        });


    }
}
