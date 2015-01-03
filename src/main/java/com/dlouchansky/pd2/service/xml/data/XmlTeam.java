package com.dlouchansky.pd2.service.xml.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XmlTeam {
    public final String name;
    public final List<XmlPlayer> players;
    public final Map<Integer, Integer> gamePlayerTimeByIds;
    public final List<XmlCard> cards;
    public final List<XmlGoal> goals;

    public XmlTeam(String name, List<XmlPlayer> players, List<XmlCard> cards, List<XmlGoal> goals, Map<Integer, Integer> gamePlayerTimeByIds) {
        this.name = name;
        this.players = Collections.unmodifiableList(players);
        this.cards = Collections.unmodifiableList(cards);
        this.goals = Collections.unmodifiableList(goals);
        this.gamePlayerTimeByIds = Collections.unmodifiableMap(gamePlayerTimeByIds);
    }

    @Override
    public String toString() {
        return "XmlTeam{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ", gamePlayerTimeByIds=" + gamePlayerTimeByIds +
                ", cards=" + cards +
                ", goals=" + goals +
                '}';
    }
}
