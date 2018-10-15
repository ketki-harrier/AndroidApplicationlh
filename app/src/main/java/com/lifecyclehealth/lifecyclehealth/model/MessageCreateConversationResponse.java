package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 24-10-2017.
 */

public class MessageCreateConversationResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("binderID")
    private String binderID;

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

    public String getBinderID() {
        return binderID;
    }

    public void setBinderID(String binderID) {
        this.binderID = binderID;
    }
}
