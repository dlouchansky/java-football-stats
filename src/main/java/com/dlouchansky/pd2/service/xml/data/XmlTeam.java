package com.dlouchansky.pd2.service.xml.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XmlTeam {
    public final String name;
    public final List<XmlPlayer> players;
    public final Map<Integer, Integer> gamePlayerTimeByNumber;
    public final List<XmlCard> cards;
    public final List<XmlGoal> goals;
    public final Map<Integer, Integer> goalkeeperNumberByTime;

    public XmlTeam(String name, List<XmlPlayer> players, List<XmlCard> cards, List<XmlGoal> goals, Map<Integer, Integer> gamePlayerTimeByNumber, Map<Integer, Integer> goalkeeperNumberByTime) {
        this.name = name;
        this.goalkeeperNumberByTime = Collections.unmodifiableMap(goalkeeperNumberByTime);
        this.players = Collections.unmodifiableList(players);
        this.cards = Collections.unmodifiableList(cards);
        this.goals = Collections.unmodifiableList(goals);
        this.gamePlayerTimeByNumber = Collections.unmodifiableMap(gamePlayerTimeByNumber);
    }

    @Override
    public String toString() {
        return "XmlTeam{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", gamePlayerTimeByNumber=" + gamePlayerTimeByNumber +
                ", cards=" + cards +
                ", goals=" + goals +
                '}';
    }
}
