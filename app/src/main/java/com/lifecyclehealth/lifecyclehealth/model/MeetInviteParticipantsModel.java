package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 23-08-2017.
 */

public class MeetInviteParticipantsModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public ArrayList<EpisodeParticipantList> getEpisodeParticipantList() {
        return episodeParticipantList;
    }

    public void setEpisodeParticipantList(ArrayList<EpisodeParticipantList> episodeParticipantList) {
        this.episodeParticipantList = episodeParticipantList;
    }

    @SerializedName("episodeParticipantList")
    private ArrayList<EpisodeParticipantList> episodeParticipantList;

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


    public class EpisodeParticipantList {

        private boolean isPatientSelected;

        public boolean isPatientSelected() {
            return isPatientSelected;
        }

        public void setPatientSelected(boolean patientSelected) {
            isPatientSelected = patientSelected;
        }


        @SerializedName("UserID")
        private String UserID;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("LastName")
        private String LastName;
        @SerializedName("FullName")
        private String FullName;
        @SerializedName("EmailID")
        private String EmailID;
        @SerializedName("Profile_Pic_Path")
        private String Profile_Pic_Path;
        @SerializedName("Profile_Picture_Content_Type")
        private String Profile_Picture_Content_Type;
        @SerializedName("MoxtraID")
        private String MoxtraID;
        @SerializedName("PatientID")
        private String PatientID;
        @SerializedName("Episode_Care_PlanID")
        private String Episode_Care_PlanID;
        @SerializedName("Episode_Care_Plan_Name")
        private String Episode_Care_Plan_Name;
        @SerializedName("Designate_FullName")
        private String Designate_FullName;
        @SerializedName("Designate_Exist")
        private boolean Designate_Exist;
        @SerializedName("LoggedInUser")
        private boolean LoggedInUser;
        @SerializedName("RoleName")
        private List<String> RoleName;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private boolean isChecked;

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
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

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getEmailID() {
            return EmailID;
        }

        public void setEmailID(String emailID) {
            EmailID = emailID;
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

        public String getMoxtraID() {
            return MoxtraID;
        }

        public void setMoxtraID(String moxtraID) {
            MoxtraID = moxtraID;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }

        public String getEpisode_Care_Plan_Name() {
            return Episode_Care_Plan_Name;
        }

        public void setEpisode_Care_Plan_Name(String episode_Care_Plan_Name) {
            Episode_Care_Plan_Name = episode_Care_Plan_Name;
        }

        public String getDesignate_FullName() {
            return Designate_FullName;
        }

        public void setDesignate_FullName(String designate_FullName) {
            Designate_FullName = designate_FullName;
        }

        public boolean isDesignate_Exist() {
            return Designate_Exist;
        }

        public void setDesignate_Exist(boolean designate_Exist) {
            Designate_Exist = designate_Exist;
        }

        public boolean isLoggedInUser() {
            return LoggedInUser;
        }

        public void setLoggedInUser(boolean loggedInUser) {
            LoggedInUser = loggedInUser;
        }

        public List<String> getRoleName() {
            return RoleName;
        }

        public void setRoleName(List<String> roleName) {
            RoleName = roleName;
        }


        private String Designate_Id;

        public String getDesignate_Id() {
            return Designate_Id;
        }

        public void setDesignate_Id(String designate_Id) {
            Designate_Id = designate_Id;
        }
    }


}
