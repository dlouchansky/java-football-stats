package com.dlouchansky.pd2.service.xml.data;

import java.util.Collections;
import java.util.List;

public class XmlTeam {
    public final String name;
    public final List<XmlPlayer> players;
    public final List<Integer> gamePlayerIds;
    public final List<XmlCard> cards;
    public final List<XmlGoal> goals;

    public XmlTeam(String name, List<XmlPlayer> players, List<Integer> gamePlayerIds, List<XmlCard> cards, List<XmlGoal> goals) {
        this.name = name;
        this.players = Collections.unmodifiableList(players);
        this.gamePlayerIds = Collections.unmodifiableList(gamePlayerIds);
        this.cards = Collections.unmodifiableList(cards);
        this.goals = Collections.unmodifiableList(goals);
    }

    @Override
    public String toString() {
        return "\n XmlTeam{" +
                "name='" + name + '\'' +
                ", players=" + players +
                ",\n  gamePlayerIds=" + gamePlayerIds +
                ",\n  cards=" + cards +
                ",\n  goals=" + goals +
                '}';
    }
}
