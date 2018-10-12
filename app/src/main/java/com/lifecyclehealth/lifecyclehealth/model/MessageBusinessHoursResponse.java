package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

public class MessageBusinessHoursResponse {


    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("confirmText")
    public String confirmText;

    @SerializedName("showCancelButton")
    public boolean showCancelButton;

    @SerializedName("isShowPopup")
    public boolean isShowPopup;

    /*@SerializedName("business_hours")
    public String business_hours;*/


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public boolean isShowCancelButton() {
        return showCancelButton;
    }

    public boolean isShowPopup() {
        return isShowPopup;
    }
}
