package com.dlouchansky.pd2.service.xml.data;

public class XmlPlayer {
    public final String firstName;
    public final String lastName;
    public final Integer number;

    @Override
    public String toString() {
        return "\n  XmlPlayer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", number=" + number +
                ", role='" + role + '\'' +
                '}';
    }

    public final String role;

    public XmlPlayer(String firstName, String lastName, Integer number, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.role = role;
    }
}
