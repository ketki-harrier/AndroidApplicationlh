package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 27-10-2017.
 */

public class SubmitChatParticipantDto {
    private ArrayList<String> UserIDs;
    private String binder_id;

    public ArrayList<String> getUserIDs() {
        return UserIDs;
    }

    public void setUserIDs(ArrayList<String> userIDs) {
        UserIDs = userIDs;
    }

    public String getBinder_id() {
        return binder_id;
    }

    public void setBinder_id(String binder_id) {
        this.binder_id = binder_id;
    }

    @Override
    public String toString() {
        return "SubmitChatParticipantDto{" +
                "UserIDs=" + UserIDs +
                ", binder_id='" + binder_id + '\'' +
                '}';
    }
}
