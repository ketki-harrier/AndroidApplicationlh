package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaibhavi on 01-11-2017.
 */

public class CheckProviderResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("designateList")
    private List<DesignateList> designateList;

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

    public List<DesignateList> getDesignateList() {
        return designateList;
    }

    public void setDesignateList(List<DesignateList> designateList) {
        this.designateList = designateList;
    }

    public class DesignateList{

        @SerializedName("Designate_UserID")
        private String Designate_UserID;
        @SerializedName("Provider_UserID")
        private String Provider_UserID;
        @SerializedName("Designate_Preference")
        private String Designate_Preference;

        public String getDesignate_UserID() {
            return Designate_UserID;
        }

        public void setDesignate_UserID(String designate_UserID) {
            Designate_UserID = designate_UserID;
        }

        public String getProvider_UserID() {
            return Provider_UserID;
        }

        public void setProvider_UserID(String provider_UserID) {
            Provider_UserID = provider_UserID;
        }

        public String getDesignate_Preference() {
            return Designate_Preference;
        }
    }

}
