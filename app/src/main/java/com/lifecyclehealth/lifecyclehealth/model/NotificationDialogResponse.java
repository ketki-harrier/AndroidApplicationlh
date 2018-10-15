package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 18-01-2018.
 */

public class NotificationDialogResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("alertList")
    private ArrayList<AlertList> alertList;

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

    public ArrayList<AlertList> getAlertList() {
        return alertList;
    }

    public void setAlertList(ArrayList<AlertList> alertList) {
        this.alertList = alertList;
    }

    public class AlertList {

        @SerializedName("AlertID")
        private String AlertID;
        @SerializedName("PatientID")
        private String PatientID;
        @SerializedName("UserID")
        private String UserID;
        @SerializedName("Title")
        private String Title;
        @SerializedName("Description")
        private String Description;
        @SerializedName("Alert_Type")
        private String Alert_Type;
        @SerializedName("DateTime")
        private String DateTime;
        @SerializedName("Date_Formatted")
        private String Date_Formatted;
        @SerializedName("RefID")
        private String RefID;
        @SerializedName("Patient_Survey_Scheduled_ResponseID")
        private String Patient_Survey_Scheduled_ResponseID;
        @SerializedName("Is_Deletable")
        private boolean Is_Deletable;
        @SerializedName("Status")
        private String Status;
        @SerializedName("Super_RefID")
        private String Super_RefID;
        @SerializedName("NewAlertCount")
        private String NewAlertCount;

        public String getAlertID() {
            return AlertID;
        }

        public void setAlertID(String alertID) {
            AlertID = alertID;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getAlert_Type() {
            return Alert_Type;
        }

        public void setAlert_Type(String alert_Type) {
            Alert_Type = alert_Type;
        }

        public String getDateTime() {
            return DateTime;
        }

        public void setDateTime(String dateTime) {
            DateTime = dateTime;
        }

        public String getDate_Formatted() {
            return Date_Formatted;
        }

        public void setDate_Formatted(String date_Formatted) {
            Date_Formatted = date_Formatted;
        }

        public String getRefID() {
            return RefID;
        }

        public void setRefID(String refID) {
            RefID = refID;
        }

        public String getPatient_Survey_Scheduled_ResponseID() {
            return Patient_Survey_Scheduled_ResponseID;
        }

        public void setPatient_Survey_Scheduled_ResponseID(String patient_Survey_Scheduled_ResponseID) {
            Patient_Survey_Scheduled_ResponseID = patient_Survey_Scheduled_ResponseID;
        }

        public boolean is_Deletable() {
            return Is_Deletable;
        }

        public void setIs_Deletable(boolean is_Deletable) {
            Is_Deletable = is_Deletable;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getSuper_RefID() {
            return Super_RefID;
        }

        public void setSuper_RefID(String super_RefID) {
            Super_RefID = super_RefID;
        }

        public String getNewAlertCount() {
            return NewAlertCount;
        }

        public void setNewAlertCount(String newAlertCount) {
            NewAlertCount = newAlertCount;
        }
    }

}
