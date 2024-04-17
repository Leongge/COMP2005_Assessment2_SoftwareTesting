package com.leong.SDTP.model;

public class Employee {
    private final int id;
    private final String surname;
    private final String forename;

    public Employee(int id, String surname, String forename) {
        this.id = id;
        this.surname = surname;
        this.forename = forename;
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getForename() {
        return forename;
    }
}
