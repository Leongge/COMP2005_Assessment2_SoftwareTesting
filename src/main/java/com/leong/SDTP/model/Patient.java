package com.leong.SDTP.model;

public class Patient {
    private final int id;
    private final String surname;
    private final String forename;
    private final String nhsNumber;

    public Patient(int id, String surname, String forename, String nhsNumber) {
        this.id = id;
        this.surname = surname;
        this.forename = forename;
        this.nhsNumber = nhsNumber;
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

    public String getNhsNumber() {
        return nhsNumber;
    }
}