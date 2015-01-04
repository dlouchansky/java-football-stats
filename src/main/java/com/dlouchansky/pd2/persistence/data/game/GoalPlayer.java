package com.dlouchansky.pd2.persistence.data.game;

import com.dlouchansky.pd2.persistence.data.Player;

import javax.persistence.*;

@Entity
@Table(name="goalPlayers")
public class GoalPlayer {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    public GoalPlayer(Goal goal, Player player, GoalPlayerType goalPlayerType) {
        this.goal = goal;
        this.player = player;
        this.goalPlayerType = goalPlayerType;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goals_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "players_id")
    private Player player;

    @Column(name="role")
    @Enumerated(EnumType.ORDINAL)
    private GoalPlayerType goalPlayerType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GoalPlayerType getGoalPlayerType() {
        return goalPlayerType;
    }

    public void setGoalPlayerType(GoalPlayerType goalPlayerType) {
        this.goalPlayerType = goalPlayerType;
    }

    public GoalPlayer() {

    }
}
