package com.dlouchansky.pd2.presentation;

import com.dlouchansky.pd2.application.StatsService;
import com.dlouchansky.pd2.application.XmlSavingService;
import com.dlouchansky.pd2.persistence.DataManipulationFacade;
import com.dlouchansky.pd2.persistence.data.Team;
import com.dlouchansky.pd2.presentation.dtos.RefereeDTO;
import com.dlouchansky.pd2.presentation.dtos.TopDTO;
import com.dlouchansky.pd2.presentation.dtos.TopGoalkeeperDTO;
import com.dlouchansky.pd2.presentation.dtos.TopPlayerDTO;
import com.dlouchansky.pd2.service.xml.data.XmlGame;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
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

    private final XmlSavingService xmlService;
    private final StatsService statsService;
    private final DataManipulationFacade dataManipulationFacade;

    public WebApp(XmlSavingService xmlService,
                  StatsService statsService,
                  DataManipulationFacade dataManipulationFacade
    ) {
        this.xmlService = xmlService;
        this.statsService = statsService;
        this.dataManipulationFacade = dataManipulationFacade;
    }

    public void initRoutes() {

        staticFileLocation("/static");

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            if ("true".equals(req.cookie("truncated"))) {
                res.cookie("truncated", "false");
                model.put("truncated", true);
            } else {
                model.put("truncated", false);
            }
            return new ModelAndView(model, "home.ftl");
        }, new FreeMarkerEngine());

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
            Team team = statsService.getById(req.params("teamId"));
            List<TopGoalkeeperDTO> goalkeepers = statsService.getForTopGoalkeepers(team);
            List<TopPlayerDTO> players = statsService.getForTopPlayers(team);

            model.put("goalkeepers", goalkeepers);
            model.put("players", players);
            model.put("team", team.getName());
            return new ModelAndView(model, "topTeamPlayers.ftl");
        }, new FreeMarkerEngine());

        get("/upload", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "upload.ftl");
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
                    //todo make it in job
                    MultiPartInputStreamParser.MultiPart partMulti = (MultiPartInputStreamParser.MultiPart) part;
                    File dest = new File("/tmp/xmls/" + System.currentTimeMillis() + partMulti.getContentDispositionFilename());
                    if (partMulti.getFile().renameTo(dest)) {
                        XmlGame game = xmlService.parseFile(dest);
                        xmlService.saveGame(game);
                    } else {
                        throw new RuntimeException("file not renamed");
                    }
                }
            });
            res.redirect("/");
            return null;
        });


    }
}
