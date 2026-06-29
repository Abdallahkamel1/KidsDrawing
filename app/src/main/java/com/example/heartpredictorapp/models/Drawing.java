package com.example.heartpredictorapp.models;

public class Drawing {
    String drawID, parentID, childID, childName, drawFile, uploadDate, analyzeState;

    public Drawing(){

    }

    public Drawing(String drawID, String parentID, String childID, String childName, String drawFile, String uploadDate, String analyzeState) {
        this.drawID = drawID;
        this.parentID = parentID;
        this.childID = childID;
        this.childName = childName;
        this.drawFile = drawFile;
        this.uploadDate = uploadDate;
        this.analyzeState = analyzeState;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
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

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }

    public String getDrawFile() {
        return drawFile;
    }

    public void setDrawFile(String drawFile) {
        this.drawFile = drawFile;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getAnalyzeState() {
        return analyzeState;
    }

    public void setAnalyzeState(String analyzeState) {
        this.analyzeState = analyzeState;
    }
}
