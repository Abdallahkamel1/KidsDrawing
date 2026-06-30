package com.example.heartpredictorapp.models;

public class Doctor {

    private String doctorID;
    private String doctorName;
    private String doctorEmail;
    private String doctorPhone;
    private String doctorPass;
    private String doctorState;

    public Doctor(){

    }

    public Doctor(String doctorID, String doctorName, String doctorEmail, String doctorPhone, String doctorPass, String doctorState) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.doctorPhone = doctorPhone;
        this.doctorPass = doctorPass;
        this.doctorState = doctorState;
    }

    public String getDoctorState() {
        return doctorState;
    }

    public void setDoctorState(String doctorState) {
        this.doctorState = doctorState;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorPass() {
        return doctorPass;
    }

    public void setDoctorPass(String doctorPass) {
        this.doctorPass = doctorPass;
    }
}
