package com.example.heartpredictorapp.models;

public class GroupParticipation {
    String partID;
    String groupID;
    String userType;
    String userID;
    String userName;
    String partDate;
    String partMsg;

    public GroupParticipation(){

    }

    public GroupParticipation(String partID, String groupID, String userType, String userID, String userName, String partDate, String partMsg) {
        this.partID = partID;
        this.groupID = groupID;
        this.userType = userType;
        this.userID = userID;
        this.userName = userName;
        this.partDate = partDate;
        this.partMsg = partMsg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPartID() {
        return partID;
    }

    public void setPartID(String partID) {
        this.partID = partID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPartDate() {
        return partDate;
    }

    public void setPartDate(String partDate) {
        this.partDate = partDate;
    }

    public String getPartMsg() {
        return partMsg;
    }

    public void setPartMsg(String partMsg) {
        this.partMsg = partMsg;
    }
}
