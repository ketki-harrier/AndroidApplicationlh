package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 24-08-2017.
 */

public class SubmitParticipantDto {

    private String session_key;



    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }


    public ArrayList<String> getUserIDs() {
        return UserIDs;
    }

    public void setUserIDs(ArrayList<String> userIDs) {
        UserIDs = userIDs;
    }

    private ArrayList<String> UserIDs;

}
