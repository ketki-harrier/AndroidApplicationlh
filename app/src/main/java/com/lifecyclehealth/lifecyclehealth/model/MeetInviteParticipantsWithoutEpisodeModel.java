package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 24-10-2017.
 */

public class MeetInviteParticipantsWithoutEpisodeModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("userList")
    private ArrayList<UserList> userList;

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

    public ArrayList<UserList> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<UserList> userList) {
        this.userList = userList;
    }

    public class UserList{
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
        @SerializedName("Username")
        private String Username;
        @SerializedName("Name_Title")
        private String Name_Title;
        @SerializedName("Preferred_Name")
        private String Preferred_Name;
        @SerializedName("Name_Suffix")
        private String Name_Suffix;
        @SerializedName("LoggedInUser")
        private boolean LoggedInUser;
        @SerializedName("FromLoggedInOrganisation")
        private boolean FromLoggedInOrganisation ;
        @SerializedName("Designate_Exist")
        private boolean Designate_Exist ;
        @SerializedName("Designate_FullName")
        private String Designate_FullName;
        @SerializedName("RoleName")
        private ArrayList<String> RoleName;

        private boolean isPatientSelected;

        public boolean isPatientSelected() {
            return isPatientSelected;
        }

        public void setPatientSelected(boolean patientSelected) {
            isPatientSelected = patientSelected;
        }

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

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            Username = username;
        }

        public String getName_Title() {
            return Name_Title;
        }

        public void setName_Title(String name_Title) {
            Name_Title = name_Title;
        }

        public String getPreferred_Name() {
            return Preferred_Name;
        }

        public void setPreferred_Name(String preferred_Name) {
            Preferred_Name = preferred_Name;
        }

        public String getName_Suffix() {
            return Name_Suffix;
        }

        public void setName_Suffix(String name_Suffix) {
            Name_Suffix = name_Suffix;
        }

        public boolean isLoggedInUser() {
            return LoggedInUser;
        }

        public void setLoggedInUser(boolean loggedInUser) {
            LoggedInUser = loggedInUser;
        }

        public boolean isFromLoggedInOrganisation() {
            return FromLoggedInOrganisation;
        }

        public void setFromLoggedInOrganisation(boolean fromLoggedInOrganisation) {
            FromLoggedInOrganisation = fromLoggedInOrganisation;
        }

        public boolean isDesignate_Exist() {
            return Designate_Exist;
        }

        public void setDesignate_Exist(boolean designate_Exist) {
            Designate_Exist = designate_Exist;
        }

        public String getDesignate_FullName() {
            return Designate_FullName;
        }

        public void setDesignate_FullName(String designate_FullName) {
            Designate_FullName = designate_FullName;
        }

        public ArrayList<String> getRoleName() {
            return RoleName;
        }

        public void setRoleName(ArrayList<String> roleName) {
            RoleName = roleName;
        }



        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        private boolean isChecked;

        private String Designate_Id;

        public String getDesignate_Id() {
            return Designate_Id;
        }

        public void setDesignate_Id(String designate_Id) {
            Designate_Id = designate_Id;
        }

    }

}
