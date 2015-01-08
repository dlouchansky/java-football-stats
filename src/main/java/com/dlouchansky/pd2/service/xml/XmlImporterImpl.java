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
import java.util.stream.Collectors;

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
                .reduce(60 * 60, (time1, time2) -> time1 > time2 ? time1 : time2);
    }

    private XmlTeam parseTeamElement(Element teamElement) {
        String name = teamElement.getAttribute("Nosaukums");
        Integer gameDuration = parseGameDuration();
        List<XmlGoal> xmlGoals = getGoals(teamElement);
        List<XmlCard> xmlCards = getCards(teamElement);
        List<XmlPlayer> xmlPlayers = getPlayers(teamElement);
        Map<Integer, Integer> gamePlayerTimesByNumbers = getGamePlayerNumbersWithTime(teamElement, gameDuration);
        Map<Integer, Integer> goalkeeperNumbersByTime = getGoalkeepersOrderedByTime(teamElement, xmlPlayers, gamePlayerTimesByNumbers, gameDuration);

        return new XmlTeam(name, xmlPlayers, xmlCards, xmlGoals, gamePlayerTimesByNumbers, goalkeeperNumbersByTime);
    }

    private Map<Integer, Integer> getGamePlayerNumbersWithTime(Element team, Integer gameDuration) {
        Element gamePlayers = (Element) team.getElementsByTagName("Pamatsastavs").item(0);
        IterableNodeList domPlayerNumbers = new IterableNodeList(gamePlayers.getElementsByTagName("Speletajs"));
        Map<Integer, Integer> gamePlayerTimesByNumbers = parseGamePlayerNumbersAndTime(domPlayerNumbers, gameDuration);

        Element switcherGamePlayers = (Element) team.getElementsByTagName("Mainas").item(0);
        if (switcherGamePlayers != null) {
            IterableNodeList domChanges = new IterableNodeList(switcherGamePlayers.getElementsByTagName("Maina"));
            gamePlayerTimesByNumbers.putAll(parseSwitcherGamePlayerIdsAndTime(domChanges, gameDuration));
        }

        return gamePlayerTimesByNumbers;
    }

    private List<XmlPlayer> getPlayers(Element team) {
        Element players = (Element) team.getElementsByTagName("Speletaji").item(0);
        IterableNodeList domPlayers = new IterableNodeList(players.getElementsByTagName("Speletajs"));
        return parsePlayers(domPlayers);
    }

    private List<XmlGoal> getGoals(Element team) {
        Element goals = (Element) team.getElementsByTagName("Varti").item(0);
        List<XmlGoal> xmlGoals = new ArrayList<>();
        if (goals != null) {
            IterableNodeList domGoals = new IterableNodeList(goals.getElementsByTagName("VG"));
            xmlGoals.addAll(parseGoals(domGoals));
        } else {
            xmlGoals.addAll(new ArrayList<>());
        }
        return xmlGoals;
    }

    private List<XmlCard> getCards(Element team) {
        Element cards = (Element) team.getElementsByTagName("Sodi").item(0);
        List<XmlCard> xmlCards = new ArrayList<>();
        if (cards != null) {
            IterableNodeList domCards = new IterableNodeList(cards.getElementsByTagName("Sods"));
            xmlCards.addAll(parseCards(domCards));
        } else {
            xmlCards.addAll(new ArrayList<>());
        }
        return xmlCards;
    }

    private Map<Integer, Integer> getGoalkeepersOrderedByTime(Element team, List<XmlPlayer> xmlPlayers, Map<Integer, Integer> gamePlayerTimesByNumber, Integer gameDuration) {
        List<XmlPlayer> gameGoalkeepers = xmlPlayers
                .stream()
                .filter(xmlPlayer -> "V".equals(xmlPlayer.role) && gamePlayerTimesByNumber.get(xmlPlayer.number) != null)
                .collect(Collectors.toList());
        Map<Integer, Integer> goalkeeperNumbersByTime = new TreeMap<>();
        if (gameGoalkeepers.size() == 1) {
            goalkeeperNumbersByTime.put(gameDuration, gameGoalkeepers.get(0).number);
        } else if (gameGoalkeepers.size() > 1) {
            Element switcherGamePlayers = (Element) team.getElementsByTagName("Mainas").item(0);
            if (switcherGamePlayers != null) {
                IterableNodeList domChanges = new IterableNodeList(switcherGamePlayers.getElementsByTagName("Maina"));
                gameGoalkeepers.forEach(goalkeeper -> {
                    domChanges.forEach(el -> {
                        Integer nr1 = Integer.parseInt(el.getAttribute("Nr1"));
                        Integer nr2 = Integer.parseInt(el.getAttribute("Nr2"));
                        Integer laiks = parseTimeToSec(el.getAttribute("Laiks"));

                        if (nr1.equals(goalkeeper.number))
                            goalkeeperNumbersByTime.put(laiks, goalkeeper.number);

                        if (nr2.equals(goalkeeper.number))
                            goalkeeperNumbersByTime.put(gameDuration, goalkeeper.number);
                    });

                    goalkeeperNumbersByTime.put(gameDuration, goalkeeper.number);
                });
            }
        }

        return goalkeeperNumbersByTime;
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

    private Map<Integer, Integer> parseGamePlayerNumbersAndTime(IterableNodeList speletaji, Integer gameDuration) {
        Map<Integer, Integer> xmlPlayerTimeByNumber = new HashMap<>();
        speletaji.forEach(el -> xmlPlayerTimeByNumber.put(Integer.parseInt(el.getAttribute("Nr")), gameDuration));
        return xmlPlayerTimeByNumber;
    }

    private Map<Integer, Integer> parseSwitcherGamePlayerIdsAndTime(IterableNodeList mainas, Integer gameDuration) {
        Map<Integer, Integer> xmlPlayerTimeByNumber = new HashMap<>();
        mainas.forEach(el -> {
            int nr1 = Integer.parseInt(el.getAttribute("Nr1"));
            int nr2 = Integer.parseInt(el.getAttribute("Nr2"));
            int laiks = parseTimeToSec(el.getAttribute("Laiks"));
            if (xmlPlayerTimeByNumber.get(nr1) == null) {
                xmlPlayerTimeByNumber.put(nr1, laiks);
            } else {
                xmlPlayerTimeByNumber.put(nr1, xmlPlayerTimeByNumber.get(nr1) - (gameDuration - laiks));
            }

            if (xmlPlayerTimeByNumber.get(nr2) == null) {
                xmlPlayerTimeByNumber.put(nr2, gameDuration - laiks);
            } else {
                xmlPlayerTimeByNumber.put(nr2, xmlPlayerTimeByNumber.get(nr2) + (gameDuration - laiks));
            }
        });
        return xmlPlayerTimeByNumber;
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
