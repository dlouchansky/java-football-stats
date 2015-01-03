package com.dlouchansky.pd2.service.xml;

import com.dlouchansky.pd2.service.xml.data.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlImporterImpl implements XmlImporter {

    private Element docRoot;

    @Override
    public void init(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        docRoot = doc.getDocumentElement();
    }

    @Override
    public XmlGame parseGame() throws ParseException {
        if (docRoot == null) throw new RuntimeException("Please, set file no parse");
        return new XmlGame(getVenueName(), getVenueWatchers(), getTeams(), getReferees(), getVenueDate());
    }

    private String getVenueName() {
        return docRoot.getAttribute("Vieta");
    }

    private Integer getVenueDate() throws ParseException {
        return parseDateToSec(docRoot.getAttribute("Laiks"));
    }

    private Integer getVenueWatchers() {
        return Integer.parseInt(docRoot.getAttribute("Skatitaji"));
    }

    private List<XmlReferee> getReferees() {
        List<XmlReferee> xmlReferees = new ArrayList<>();
        IterableNodeList nl = new IterableNodeList(docRoot.getElementsByTagName("VT"));
        xmlReferees.addAll(parseReferees(nl));
        IterableNodeList nl2 = new IterableNodeList(docRoot.getElementsByTagName("T"));
        xmlReferees.addAll(parseReferees(nl2));
        return xmlReferees;
    }

    private List<XmlTeam> getTeams() {
        List<XmlTeam> xmlTeams = new ArrayList<>();
        IterableNodeList nl = new IterableNodeList(docRoot.getElementsByTagName("Komanda"));
        xmlTeams.addAll(parseTeams(nl));
        return xmlTeams;
    }

    private List<XmlReferee> parseReferees(IterableNodeList referees) {
        List<XmlReferee> xmlReferees = new ArrayList<>();
        referees.forEach(el -> xmlReferees.add(parseRefereeElement(el)));
        return xmlReferees;
    }

    private XmlReferee parseRefereeElement(Element refereeElem) {
        String firstName = refereeElem.getAttribute("Vards");
        String lastName = refereeElem.getAttribute("Uzvards");
        Boolean isMain = refereeElem.getTagName().contains("VT");
        return new XmlReferee(firstName, lastName, isMain);
    }

    private List<XmlTeam> parseTeams(IterableNodeList teams) {
        List<XmlTeam> xmlTeams = new ArrayList<>();
        teams.forEach(el -> xmlTeams.add(parseTeamElement(el)));
        return xmlTeams;
    }

    private Integer parseGameDuration() {
        IterableNodeList domGoals = new IterableNodeList(docRoot.getElementsByTagName("VG"));
        List<XmlGoal> xmlGoals = parseGoals(domGoals);
        return xmlGoals
                .stream()
                .map(goal -> goal.time)
                .reduce(60 * 60, (time1, time2) -> {
                    return time1 > time2 ? time1 : time2;
                });
    }

    private XmlTeam parseTeamElement(Element el) {

        String name = el.getAttribute("Nosaukums");

        Element players = (Element) el.getElementsByTagName("Speletaji").item(0);
        IterableNodeList domPlayers = new IterableNodeList(players.getElementsByTagName("Speletajs"));
        List<XmlPlayer> xmlPlayers = parsePlayers(domPlayers);

        Element goals = (Element) el.getElementsByTagName("Varti").item(0);
        List<XmlGoal> xmlGoals = new ArrayList<>();
        if (goals != null) {
            IterableNodeList domGoals = new IterableNodeList(goals.getElementsByTagName("VG"));
            xmlGoals.addAll(parseGoals(domGoals));
        } else
            xmlGoals.addAll(new ArrayList<>());

        Integer gameDuration = parseGameDuration();

        Element gamePlayers = (Element) el.getElementsByTagName("Pamatsastavs").item(0);
        IterableNodeList domPlayerIds = new IterableNodeList(gamePlayers.getElementsByTagName("Speletajs"));
        Map<Integer, Integer> gamePlayerTimesByIds = parseGamePlayerIdsAndTime(domPlayerIds, gameDuration);

        Element switcherGamePlayers = (Element) el.getElementsByTagName("Mainas").item(0);
        if (switcherGamePlayers != null) {
            IterableNodeList domGamePlayers = new IterableNodeList(switcherGamePlayers.getElementsByTagName("Maina"));
            gamePlayerTimesByIds.putAll(parseSwitcherGamePlayerIdsAndTime(domGamePlayers, gameDuration));
        }

        Element cards = (Element) el.getElementsByTagName("Sodi").item(0);
        List<XmlCard> xmlCards = new ArrayList<>();
        if (cards != null) {
            IterableNodeList domCards = new IterableNodeList(cards.getElementsByTagName("Sods"));
            xmlCards.addAll(parseCards(domCards));
        } else
            xmlCards.addAll(new ArrayList<>());


        return new XmlTeam(name, xmlPlayers, xmlCards, xmlGoals, gamePlayerTimesByIds);
    }

    private List<XmlPlayer> parsePlayers(IterableNodeList speletaji) {
        List<XmlPlayer> xmlPlayers = new ArrayList<>();
        speletaji.forEach(el -> xmlPlayers.add(parsePlayer(el)));
        return xmlPlayers;
    }

    private XmlPlayer parsePlayer(Element player) {
        String firstName = player.getAttribute("Vards");
        String lastName = player.getAttribute("Uzvards");
        Integer number = Integer.parseInt(player.getAttribute("Nr"));
        String role = player.getAttribute("Loma");
        return new XmlPlayer(firstName, lastName, number, role);
    }

    private Map<Integer, Integer> parseGamePlayerIdsAndTime(IterableNodeList speletaji, Integer gameDuration) {
        Map<Integer, Integer> xmlPlayerTimeByIds = new HashMap<>();
        speletaji.forEach(el -> xmlPlayerTimeByIds.put(Integer.parseInt(el.getAttribute("Nr")), gameDuration));
        return xmlPlayerTimeByIds;
    }

    private Map<Integer, Integer> parseSwitcherGamePlayerIdsAndTime(IterableNodeList mainas, Integer gameDuration) {
        Map<Integer, Integer> xmlPlayerTimeByIds = new HashMap<>();
        mainas.forEach(el -> {
            int nr1 = Integer.parseInt(el.getAttribute("Nr1"));
            int nr2 = Integer.parseInt(el.getAttribute("Nr2"));
            int laiks = parseTimeToSec(el.getAttribute("Laiks"));
            xmlPlayerTimeByIds.put(nr1, laiks);
            xmlPlayerTimeByIds.put(nr2, gameDuration - laiks);
        });
        return xmlPlayerTimeByIds;
    }

    private List<XmlCard> parseCards(IterableNodeList cards) {
        List<XmlCard> xmlCards = new ArrayList<>();
        cards.forEach(el -> xmlCards.add(parseCardElement(el)));
        return xmlCards;
    }

    private XmlCard parseCardElement(Element goal) {
        Integer timeInSec = parseTimeToSec(goal.getAttribute("Laiks"));
        Integer playerId = Integer.parseInt(goal.getAttribute("Nr"));
        return new XmlCard(timeInSec, playerId);
    }

    private List<XmlGoal> parseGoals(IterableNodeList goals) {
        List<XmlGoal> xmlGoals = new ArrayList<>();
        goals.forEach(el -> xmlGoals.add(parseGoalElements(el)));
        return xmlGoals;
    }

    private XmlGoal parseGoalElements(Element goal) {
        Integer timeInSec = parseTimeToSec(goal.getAttribute("Laiks"));
        Integer playerId = Integer.parseInt(goal.getAttribute("Nr"));
        Boolean sitiens = goal.getAttribute("Sitiens").equals("J");
        List<Integer> goalHelpers = parseGoalHelpers(new IterableNodeList(goal.getElementsByTagName("P")));
        return new XmlGoal(timeInSec, playerId, sitiens, goalHelpers);
    }

    private List<Integer> parseGoalHelpers(IterableNodeList goalHelpers) {
        List<Integer> xmlGoalHelpers = new ArrayList<>();
        goalHelpers.forEach(el -> xmlGoalHelpers.add(Integer.parseInt(el.getAttribute("Nr"))));
        return xmlGoalHelpers;
    }

    private Integer parseTimeToSec(String time) {
        String[] hm = time.split(":");
        return Integer.parseInt(hm[0]) * 60 + Integer.parseInt(hm[1]);
    }

    private Integer parseDateToSec(String formattedDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = dateFormat.parse(formattedDate);
        long time = (long) date.getTime() / 1000;
        return (int) time;
    }


}
