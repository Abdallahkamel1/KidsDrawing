package com.example.heartpredictorapp.models;

public class DiscussionGroup {
    String groupID;
    String groupTitle;
    String groupDesc;
    String groupState;

    public DiscussionGroup(){

    }

    public DiscussionGroup(String groupID, String groupTitle, String groupDesc, String groupState) {
        this.groupID = groupID;
        this.groupTitle = groupTitle;
        this.groupDesc = groupDesc;
        this.groupState = groupState;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupState() {
        return groupState;
    }

    public void setGroupState(String groupState) {
        this.groupState = groupState;
    }
}
