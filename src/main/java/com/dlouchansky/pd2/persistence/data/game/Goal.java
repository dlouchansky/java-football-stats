package com.dlouchansky.pd2.persistence.data.game;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="goals")
public class Goal {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="time")
    private Integer time;

    @Column(name="type")
    @Enumerated(EnumType.ORDINAL)
    private GoalType goalType;

    @Column(name="gamePart")
    @Enumerated(EnumType.ORDINAL)
    private GamePart gamePart;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="goal")
    private List<GoalPlayer> goalPlayers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "games_id")
    private Game game;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public Goal(GoalType goalType, Integer time, GamePart gamePart, Game game) {
        this.goalType = goalType;
        this.time = time;
        this.gamePart = gamePart;
        this.game = game;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    public GamePart getGamePart() {
        return gamePart;
    }

    public void setGamePart(GamePart gamePart) {
        this.gamePart = gamePart;
    }

    public List<GoalPlayer> getGoalPlayers() {
        return goalPlayers;
    }

    public void setGoalPlayers(List<GoalPlayer> goalPlayers) {
        this.goalPlayers = goalPlayers;
    }

    public Goal() {

    }
}
