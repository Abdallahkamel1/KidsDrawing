package com.example.heartpredictorapp.models;

public class Child {

    private String childID;
    private String parentID;
    private String childName;
    private String childAge;
    private String childPass;
    private String childSex;

    public Child() {
    }

    public Child(String childID, String parentID, String childName, String childAge, String childPass, String childSex) {
        this.childID = childID;
        this.parentID = parentID;
        this.childName = childName;
        this.childAge = childAge;
        this.childPass = childPass;
        this.childSex = childSex;
    }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getChildPass() {
        return childPass;
    }

    public void setChildPass(String childPass) {
        this.childPass = childPass;
    }

    public String getChildSex() {
        return childSex;
    }

    public void setChildSex(String childSex) {
        this.childSex = childSex;
    }
}
