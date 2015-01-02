package com.dlouchansky.pd2.service.xml.data;


public class XmlCard {
    public final Integer time;

    @Override
    public String toString() {
        return "\n  XmlCard{" +
                "time=" + time +
                ", playerId=" + playerId +
                '}';
    }

    public final Integer playerId;

    public XmlCard(Integer time, Integer playerId) {
        this.time = time;
        this.playerId = playerId;
    }
}
