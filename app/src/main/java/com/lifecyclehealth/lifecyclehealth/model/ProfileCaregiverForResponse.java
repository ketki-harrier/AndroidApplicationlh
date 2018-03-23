package com.lifecyclehealth.lifecyclehealth.model;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 06-02-2018.
 */

public class ProfileCaregiverForResponse {

    private String status;
    private String message;
    private ArrayList<PatientList> patientList;

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

    public class PatientList{
        private String CaregiverID;
        private String UserID;
        private String PatientID;
        private String FirstName;
        private String LastName;
        private String User_FullName;
        private String Patient_FirstName;
        private String Patient_LastName;
        private String Patient_FullName;
        private String Status;
        private boolean Accepted_Status;
        private ArrayList<String> HealthCare_OrgID;
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

        public boolean isAccepted_Status() {
            return Accepted_Status;
        }

        public void setAccepted_Status(boolean accepted_Status) {
            Accepted_Status = accepted_Status;
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
    }

}
