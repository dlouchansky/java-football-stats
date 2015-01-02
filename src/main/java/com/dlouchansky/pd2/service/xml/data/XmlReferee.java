package com.dlouchansky.pd2.service.xml.data;

public class XmlReferee {
    public final String firstName;
    public final String lastName;
    public final Boolean isMain;

    public XmlReferee(String firstName, String lastName, Boolean isMain) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMain = isMain;
    }

    @Override
    public String toString() {
        return "\n XmlReferee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isMain=" + isMain +
                '}';
    }
}
