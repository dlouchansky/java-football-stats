package com.dlouchansky.pd2.service.xml.data;

import java.util.Collections;
import java.util.List;

public class XmlGame {
    public final String venue;
    public final Integer watchers;
    public final List<XmlTeam> teams;
    public final List<XmlReferee> referees;
    public final Integer date;

    public XmlGame(String venue, Integer watchers, List<XmlTeam> teams, List<XmlReferee> referees, Integer date) {
        this.venue = venue;
        this.watchers = watchers;
        this.teams = Collections.unmodifiableList(teams);
        this.referees = Collections.unmodifiableList(referees);
        this.date = date;
    }

    @Override
    public String toString() {
        return "XmlGame{" +
                "venue='" + venue + '\'' +
                ", watchers=" + watchers +
                ", teams=" + teams +
                ", referees=" + referees +
                ", date=" + date +
                '}';
    }
}
