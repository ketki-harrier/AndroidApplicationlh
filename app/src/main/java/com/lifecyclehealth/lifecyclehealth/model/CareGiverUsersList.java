package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 10-08-2017.
 */

public class CareGiverUsersList {


    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;


    @SerializedName("patientList")
    private ArrayList<PatientList> patientList;

    public PatientList getPatient_Details() {
        return Patient_Details;
    }

    public PatientList setPatient_Details(PatientList patientListPut) {
        this.Patient_Details = patientListPut;
        return patientListPut;
    }

    @SerializedName("Patient_Details")
    private PatientList Patient_Details;


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

    public ArrayList<PatientList> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientList> patientList) {
        this.patientList = patientList;
    }


    @Override
    public String toString() {
        return "CareGiverUsersList{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", patientList=" + patientList +
                '}';
    }

    public class PatientList {

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

        @SerializedName("Status")
        private String Status;

        @SerializedName("Patient_FullName")
        private String Patient_FullName;

        @SerializedName("HealthCare_OrgID")
        private ArrayList<String> HealthCare_OrgID;

        @SerializedName("HealthCare_Org_Name")
        private ArrayList<String> HealthCare_Org_Name;


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

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getPatient_FullName() {
            return Patient_FullName;
        }

        public void setPatient_FullName(String patient_FullName) {
            Patient_FullName = patient_FullName;
        }

        public ArrayList<String> getHealthCare_OrgID() {
            return HealthCare_OrgID;
        }

        public void setHealthCare_OrgID(ArrayList<String> healthCare_OrgID) {
            HealthCare_OrgID = healthCare_OrgID;
        }

        public ArrayList<String> getHealthCare_Org_Name() {
            return HealthCare_Org_Name;
        }

        public void setHealthCare_Org_Name(ArrayList<String> healthCare_Org_Name) {
            HealthCare_Org_Name = healthCare_Org_Name;
        }

        @Override
        public String toString() {
            return "PatientList{" +
                    "CaregiverID='" + CaregiverID + '\'' +
                    ", UserID='" + UserID + '\'' +
                    ", PatientID='" + PatientID + '\'' +
                    ", FirstName='" + FirstName + '\'' +
                    ", LastName='" + LastName + '\'' +
                    ", User_FullName='" + User_FullName + '\'' +
                    ", Patient_FirstName='" + Patient_FirstName + '\'' +
                    ", Patient_LastName='" + Patient_LastName + '\'' +
                    ", Status='" + Status + '\'' +
                    ", Patient_FullName=" + Patient_FullName +
                    ", HealthCare_OrgID=" + HealthCare_OrgID +
                    ", HealthCare_Org_Name=" + HealthCare_Org_Name +
                    '}';
        }
    }

}


