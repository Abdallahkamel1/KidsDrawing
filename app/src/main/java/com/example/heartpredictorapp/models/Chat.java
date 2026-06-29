package com.example.heartpredictorapp.models;

public class Chat {
    String chatID, parentID, doctorID, chatMsg, chatDate, senderType;

    public Chat() {
    }

    public Chat(String chatID, String parentID, String doctorID, String chatMsg, String chatDate, String senderType) {
        this.chatID = chatID;
        this.parentID = parentID;
        this.doctorID = doctorID;
        this.chatMsg = chatMsg;
        this.chatDate = chatDate;
        this.senderType = senderType;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
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

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }
}
