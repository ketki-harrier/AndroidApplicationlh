package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 23-10-2017.
 */

public class MessageEpisodeListResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("episodePlanList")
    private ArrayList<EpisodePlanList> episodePlanList;

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

    public ArrayList<EpisodePlanList> getEpisodePlanList() {
        return episodePlanList;
    }

    public void setEpisodePlanList(ArrayList<EpisodePlanList> episodePlanList) {
        this.episodePlanList = episodePlanList;
    }

    public class EpisodePlanList {
        @SerializedName("PatientID")
        private String PatientID;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("LastName")
        private String LastName;
        @SerializedName("Profile_Pic_Path")
        private String Profile_Pic_Path;
        @SerializedName("Profile_Picture_Content_Type")
        private String Profile_Picture_Content_Type;
        @SerializedName("FullName")
        private String FullName;
        @SerializedName("HealthCare_OrgID")
        private String HealthCare_OrgID;
        @SerializedName("Episode_Care_PlanID")
        private String Episode_Care_PlanID;
        @SerializedName("Surgical_ProcedureID")
        private String Surgical_ProcedureID;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Status")
        private String Status;
        @SerializedName("Participant_User")
        private List<String> Participant_User;

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getProfile_Pic_Path() {
            return Profile_Pic_Path;
        }

        public void setProfile_Pic_Path(String profile_Pic_Path) {
            Profile_Pic_Path = profile_Pic_Path;
        }

        public String getProfile_Picture_Content_Type() {
            return Profile_Picture_Content_Type;
        }

        public void setProfile_Picture_Content_Type(String profile_Picture_Content_Type) {
            Profile_Picture_Content_Type = profile_Picture_Content_Type;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getHealthCare_OrgID() {
            return HealthCare_OrgID;
        }

        public void setHealthCare_OrgID(String healthCare_OrgID) {
            HealthCare_OrgID = healthCare_OrgID;
        }

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }

        public String getSurgical_ProcedureID() {
            return Surgical_ProcedureID;
        }

        public void setSurgical_ProcedureID(String surgical_ProcedureID) {
            Surgical_ProcedureID = surgical_ProcedureID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public List<String> getParticipant_User() {
            return Participant_User;
        }

        public void setParticipant_User(List<String> participant_User) {
            Participant_User = participant_User;
        }
    }


}
