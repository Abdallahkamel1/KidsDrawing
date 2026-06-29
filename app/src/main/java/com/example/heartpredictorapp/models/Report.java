package com.example.heartpredictorapp.models;

public class Report {
    String reportID, drawID, parentID, doctorID, drawFile, diagnose, recommend, reportDate;

    public Report() {
    }

    public Report(String reportID, String drawID, String parentID, String doctorID, String drawFile, String diagnose, String recommend, String reportDate) {
        this.reportID = reportID;
        this.drawID = drawID;
        this.parentID = parentID;
        this.doctorID = doctorID;
        this.drawFile = drawFile;
        this.diagnose = diagnose;
        this.recommend = recommend;
        this.reportDate = reportDate;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getDrawID() {
        return drawID;
    }

    public void setDrawID(String drawID) {
        this.drawID = drawID;
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

    public String getDrawFile() {
        return drawFile;
    }

    public void setDrawFile(String drawFile) {
        this.drawFile = drawFile;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
}
