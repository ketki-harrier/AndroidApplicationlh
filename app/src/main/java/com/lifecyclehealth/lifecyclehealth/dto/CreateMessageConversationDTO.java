package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 24-10-2017.
 */

public class CreateMessageConversationDTO {

    private ArrayList<String> UserIDs;
    private String Message;

    public void setUserIDs(ArrayList<String> userIDs) {
        UserIDs = userIDs;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public BinderDetails getBinderDetails() {
        return binderDetails;
    }

    public void setBinderDetails(BinderDetails binderDetails) {
        this.binderDetails = binderDetails;
    }

    private BinderDetails binderDetails;

    public class BinderDetails{
        private String Title;
        private String Episode_Care_PlanID;
        private String Name;
        private String PatientID;
        private String Patient_UserID;
        private String Message_Type;

        public String getMessage_Type() {
            return Message_Type;
        }

        public void setMessage_Type(String message_Type) {
            Message_Type = message_Type;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

        public String getPatient_UserID() {
            return Patient_UserID;
        }

        public void setPatient_UserID(String patient_UserID) {
            Patient_UserID = patient_UserID;
        }

        @Override
        public String toString() {
            return "BinderDetails{" +
                    "Title='" + Title + '\'' +
                    ", Episode_Care_PlanID='" + Episode_Care_PlanID + '\'' +
                    ", Name='" + Name + '\'' +
                    ", PatientID='" + PatientID + '\'' +
                    ", Patient_UserID='" + Patient_UserID + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "CreateMessageConversationDTO{" +
                "UserIDs='" + UserIDs + '\'' +
                ", Message='" + Message + '\'' +
                ", binderDetails=" + binderDetails +
                '}';
    }
}
