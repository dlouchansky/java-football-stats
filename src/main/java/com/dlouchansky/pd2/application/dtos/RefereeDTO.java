package com.dlouchansky.pd2.application.dtos;

public class RefereeDTO {

    private Integer nr;
    private String name;
    private Double cardGameAverage;
    private Integer cards;
    private Integer games;

    public Integer getNr() {
        return nr;
    }

    public String getName() {
        return name;
    }

    public Double getCardGameAverage() {
        return cardGameAverage;
    }

    public RefereeDTO(Integer nr, String name, Double cardGameAverage, Integer cards, Integer games) {
        this.nr = nr;
        this.name = name;
        this.cardGameAverage = cardGameAverage;
        this.cards = cards;
        this.games = games;
    }

    public Integer getCards() {

        return cards;
    }

    public Integer getGames() {
        return games;
    }
}
