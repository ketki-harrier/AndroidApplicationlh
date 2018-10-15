package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 18-12-2017.
 */

public class MessageAcknowledgementResponse {
    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("IsMessage_Acknowledged")
    public boolean IsMessage_Acknowledged;

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

    public boolean getIsMessage_Acknowledged() {
        return IsMessage_Acknowledged;
    }

    public void setIsMessage_Acknowledged(boolean isMessage_Acknowledged) {
        IsMessage_Acknowledged = isMessage_Acknowledged;
    }
}
