package com.dlouchansky.pd2.service.xml.data;

import java.util.Collections;
import java.util.List;

public class XmlGoal {

    public final List<Integer> goalHelperIds;
    public final Integer time;
    public final Integer playerId;
    public final Boolean sitiens;

    @Override
    public String toString() {
        return "\n  XmlGoal{" +
                "goalHelperIds=" + goalHelperIds +
                ", time=" + time +
                ", playerId=" + playerId +
                ", sitiens=" + sitiens +
                '}';
    }

    public XmlGoal(Integer time, Integer playerId, Boolean sitiens, List<Integer> goalHelperIds) {
        this.time = time;
        this.playerId = playerId;
        this.sitiens = sitiens;
        this.goalHelperIds = Collections.unmodifiableList(goalHelperIds);
    }
}
