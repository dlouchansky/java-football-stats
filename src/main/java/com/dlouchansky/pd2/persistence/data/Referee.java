package com.dlouchansky.pd2.persistence.data;

import com.dlouchansky.pd2.persistence.data.game.GameReferee;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="referees")
public class Referee {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

    public List<GameReferee> getGameReferees() {
        return gameReferees;
    }

    public void setGameReferees(List<GameReferee> gameReferees) {
        this.gameReferees = gameReferees;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy="referee")
    private List<GameReferee> gameReferees;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Referee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Referee() {

    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
