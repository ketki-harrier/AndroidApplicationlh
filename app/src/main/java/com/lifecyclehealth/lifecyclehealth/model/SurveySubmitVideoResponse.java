package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 11-11-2017.
 */

public class SurveySubmitVideoResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("Attachment_Url")
    private String Attachment_Url;
    @SerializedName("Attachment_Type")
    private String Attachment_Type;

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

    public String getAttachment_Url() {
        return Attachment_Url;
    }

    public void setAttachment_Url(String attachment_Url) {
        Attachment_Url = attachment_Url;
    }

    public String getAttachment_Type() {
        return Attachment_Type;
    }

    public void setAttachment_Type(String attachment_Type) {
        Attachment_Type = attachment_Type;
    }
}
