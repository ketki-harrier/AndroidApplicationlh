package com.lifecyclehealth.lifecyclehealth.model;

/**
 * Created by vaibhavi on 30-10-2017.
 */

public class MeetNowResponse {

    private String status;
    private String message;
    private String session_key;
    private String schedule_binder_id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getSchedule_binder_id() {
        return schedule_binder_id;
    }

    public void setSchedule_binder_id(String schedule_binder_id) {
        this.schedule_binder_id = schedule_binder_id;
    }
}
