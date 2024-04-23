package com.leong.SDTP.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Patient {
    private final int id;
    private final String surname;
    private final String forename;
    private final String nhsNumber;

    @JsonCreator
    public Patient(@JsonProperty("id") int id,
                   @JsonProperty("surname") String surname,
                   @JsonProperty("forename") String forename,
                   @JsonProperty("nhsNumber") String nhsNumber) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id == patient.id &&
                Objects.equals(surname, patient.surname) &&
                Objects.equals(forename, patient.forename) &&
                Objects.equals(nhsNumber, patient.nhsNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, surname, forename, nhsNumber);
    }
}