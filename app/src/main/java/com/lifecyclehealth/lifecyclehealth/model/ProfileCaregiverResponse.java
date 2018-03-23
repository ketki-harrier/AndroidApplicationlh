package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by vaibhavi on 20-09-2017.
 */

public class ProfileCaregiverResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

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

    public List<CaregiverLists> getCaregiverList() {
        return CaregiverList;
    }

    public void setCaregiverList(List<CaregiverLists> caregiverList) {
        CaregiverList = caregiverList;
    }

    @SerializedName("CaregiverList")
    private List<CaregiverLists> CaregiverList;

    public class CaregiverLists {

        @SerializedName("CaregiverID")
        private String CaregiverID;
        @SerializedName("UserID")
        private String UserID;
        @SerializedName("PatientID")
        private String PatientID;
        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("LastName")
        private String LastName;
        @SerializedName("User_FullName")
        private String User_FullName;
        @SerializedName("Patient_FirstName")
        private String Patient_FirstName;
        @SerializedName("Patient_LastName")
        private String Patient_LastName;

        public boolean isAccepted_Status() {
            return Accepted_Status;
        }

        public void setAccepted_Status(boolean accepted_Status) {
            Accepted_Status = accepted_Status;
        }

        @SerializedName("Accepted_Status")
        private boolean Accepted_Status;


        public String getCaregiverID() {
            return CaregiverID;
        }

        public void setCaregiverID(String caregiverID) {
            CaregiverID = caregiverID;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

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

        public String getUser_FullName() {
            return User_FullName;
        }

        public void setUser_FullName(String user_FullName) {
            User_FullName = user_FullName;
        }

        public String getPatient_FirstName() {
            return Patient_FirstName;
        }

        public void setPatient_FirstName(String patient_FirstName) {
            Patient_FirstName = patient_FirstName;
        }

        public String getPatient_LastName() {
            return Patient_LastName;
        }

        public void setPatient_LastName(String patient_LastName) {
            Patient_LastName = patient_LastName;
        }

        public String getPatient_FullName() {
            return Patient_FullName;
        }

        public void setPatient_FullName(String patient_FullName) {
            Patient_FullName = patient_FullName;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

  /*      public Array getHealthCare_OrgID() {
            return HealthCare_OrgID;
        }

        public void setHealthCare_OrgID(Array healthCare_OrgID) {
            HealthCare_OrgID = healthCare_OrgID;
        }

        public Array getHealthCare_Org_Name() {
            return HealthCare_Org_Name;
        }

        public void setHealthCare_Org_Name(Array healthCare_Org_Name) {
            HealthCare_Org_Name = healthCare_Org_Name;
        }
*/
        @SerializedName("Patient_FullName")
        private String Patient_FullName;
        @SerializedName("Status")
        private String Status;
    /*   // @SerializedName("HealthCare_OrgID")
        private Array HealthCare_OrgID;
        //@SerializedName("HealthCare_Org_Name")
        private Array HealthCare_Org_Name;*/


    }

}
