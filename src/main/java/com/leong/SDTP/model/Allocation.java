package com.leong.SDTP.model;

public class Allocation {
    private final int id;
    private final int admissionID;
    private final int employeeID;
    private final String startTime;
    private final String endTimel;

    public Allocation(int id, int admissionID, int employeeID, String startTime, String endTimel) {
        this.id = id;
        this.admissionID = admissionID;
        this.employeeID = employeeID;
        this.startTime = startTime;
        this.endTimel = endTimel;
    }

    public int getId() {
        return id;
    }

    public int getAdmissionID() {
        return admissionID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTimel() {
        return endTimel;
    }
}
