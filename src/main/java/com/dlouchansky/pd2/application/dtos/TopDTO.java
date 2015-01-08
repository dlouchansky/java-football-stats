package com.dlouchansky.pd2.application.dtos;

public class TopDTO {

    private Integer nr;
    private Integer id;
    private String title;
    private Integer points;
    private Integer winsMain;
    private Integer lossesMain;
    private Integer winsExtra;
    private Integer lossesExtra;
    private Integer goalsScored;
    private Integer goalsMissed;

    public Integer getNr() {
        return nr;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getWinsMain() {
        return winsMain;
    }

    public Integer getLossesMain() {
        return lossesMain;
    }

    public Integer getWinsExtra() {
        return winsExtra;
    }

    public Integer getLossesExtra() {
        return lossesExtra;
    }

    public Integer getGoalsScored() {
        return goalsScored;
    }

    public Integer getGoalsMissed() {
        return goalsMissed;
    }

    public void setNr(Integer nr) {
        this.nr = nr;
    }

    public TopDTO(
            Integer nr,
            Integer id,
            String title,
            Integer points,
            Integer winsMain,
            Integer lossesMain,
            Integer winsExtra,
            Integer lossesExtra,
            Integer goalsScored,
            Integer goalsMissed
    ) {
        this.nr = nr;
        this.id = id;
        this.title = title;
        this.points = points;
        this.winsMain = winsMain;
        this.lossesMain = lossesMain;
        this.winsExtra = winsExtra;
        this.lossesExtra = lossesExtra;
        this.goalsScored = goalsScored;
        this.goalsMissed = goalsMissed;
    }


}
