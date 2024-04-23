package com.leong.SDTP.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Admission {
    private final int id;
    private final String admissionDate;
    private final String dischargeDate;
    private final int patientID;

    // Constructor
    @JsonCreator
    public Admission(@JsonProperty("id") int id,
                     @JsonProperty("admissionDate") String admissionDate,
                     @JsonProperty("dischargeDate") String dischargeDate,
                     @JsonProperty("patientID") int patientID) {
        this.id = id;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.patientID = patientID;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public int getPatientID() {
        return patientID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admission admission = (Admission) o;
        return id == admission.id &&
                Objects.equals(admissionDate, admission.admissionDate) &&
                Objects.equals(dischargeDate, admission.dischargeDate) &&
                patientID == admission.patientID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, admissionDate, dischargeDate, patientID);
    }
}

