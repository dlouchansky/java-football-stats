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

    public GoalPlayer(Goal goal, Player player, Boolean isGoalScorer) {
        this.goal = goal;
        this.player = player;
        this.isGoalScorer = isGoalScorer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goals_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "players_id")
    private Player player;

    @Column(name="isGoalScorer")
    private Boolean isGoalScorer;

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

    public Boolean getIsGoalScorer() {
        return isGoalScorer;
    }

    public void setIsGoalScorer(Boolean isGoalScorer) {
        this.isGoalScorer = isGoalScorer;
    }

    public GoalPlayer() {

    }
}
