package com.example.heartpredictorapp.models;

public class Appointment {
    String appID, parentID, doctorID, doctorName, appDate, appTime, appStatus;

    public Appointment(){

    }
    public Appointment(String appID, String parentID, String doctorID, String doctorName, String appDate, String appTime, String appStatus) {
        this.appID = appID;
        this.parentID = parentID;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.appDate = appDate;
        this.appTime = appTime;
        this.appStatus = appStatus;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
}
