package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 08-09-2017.
 */

public class SurveySubmitResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

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

}
