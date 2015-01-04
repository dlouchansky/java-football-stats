package com.dlouchansky.pd2.application.dtos;

public class RefereeDTO {

    private Integer nr;
    private String name;
    private Double cardGameAverage;

    public Integer getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public Double getCardGameAverage() {
        return cardGameAverage;
    }

    public RefereeDTO(Integer nr, String name, Double cardGameAverage) {

        this.nr = nr;
        this.name = name;
        this.cardGameAverage = cardGameAverage;
    }
}
