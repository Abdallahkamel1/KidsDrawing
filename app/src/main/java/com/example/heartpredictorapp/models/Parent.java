package com.example.heartpredictorapp.models;

public class Parent {
    private String parentID;
    private String parentName;
    private String parentEmail;
    private String parentPhone;
    private String parentPass;

    public Parent() {
    }

    public Parent(String parentID, String parentName, String parentEmail, String parentPhone, String parentPass) {
        this.parentID = parentID;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentPhone = parentPhone;
        this.parentPass = parentPass;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentPass() {
        return parentPass;
    }

    public void setParentPass(String parentPass) {
        this.parentPass = parentPass;
    }
}
