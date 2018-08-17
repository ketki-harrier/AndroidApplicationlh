package com.lifecyclehealth.lifecyclehealth.model;

import java.util.ArrayList;

public class GlobalCheckProviderResponse {

    private String status;
    private String message;
    private DesignateList designateList;
   // private String role[];

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

    public DesignateList getDesignateList() {
        return designateList;
    }

    public void setDesignateList(DesignateList designateList) {
        this.designateList = designateList;
    }

    public class DesignateList{

        private String Designate_UserID;
        private String Provider_UserID;
        private String Designate_Preference;
        private String Organisation_Designate_Preference;
        private String Organisation_DesignateID;
        private boolean Is_Provider_Designate ;
        private boolean Is_Organisation_Designate;
        private String Organisation_Preference_Message;
        private String Provider_Preference_Message;

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

        public void setDesignate_Preference(String designate_Preference) {
            Designate_Preference = designate_Preference;
        }

        public String getOrganisation_Designate_Preference() {
            return Organisation_Designate_Preference;
        }

        public void setOrganisation_Designate_Preference(String organisation_Designate_Preference) {
            Organisation_Designate_Preference = organisation_Designate_Preference;
        }

        public String getOrganisation_DesignateID() {
            return Organisation_DesignateID;
        }

        public void setOrganisation_DesignateID(String organisation_DesignateID) {
            Organisation_DesignateID = organisation_DesignateID;
        }

        public boolean isIs_Provider_Designate() {
            return Is_Provider_Designate;
        }

        public void setIs_Provider_Designate(boolean is_Provider_Designate) {
            Is_Provider_Designate = is_Provider_Designate;
        }

        public boolean isIs_Organisation_Designate() {
            return Is_Organisation_Designate;
        }

        public void setIs_Organisation_Designate(boolean is_Organisation_Designate) {
            Is_Organisation_Designate = is_Organisation_Designate;
        }

        public String getOrganisation_Preference_Message() {
            return Organisation_Preference_Message;
        }

        public void setOrganisation_Preference_Message(String organisation_Preference_Message) {
            Organisation_Preference_Message = organisation_Preference_Message;
        }

        public String getProvider_Preference_Message() {
            return Provider_Preference_Message;
        }

        public void setProvider_Preference_Message(String provider_Preference_Message) {
            Provider_Preference_Message = provider_Preference_Message;
        }
    }

}
