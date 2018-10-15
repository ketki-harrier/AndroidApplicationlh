package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 24-10-2017.
 */

public class MessagePatientListModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("patientList")
    private ArrayList<PatientList> patientList;

    public ArrayList<PatientList> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientList> patientList) {
        this.patientList = patientList;
    }

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



    public class PatientList {
        @SerializedName("patient_get_patient_list")
        private Patient_get_patient_list patient_get_patient_list;

        public Patient_get_patient_list getPatient_get_patient_list() {
            return patient_get_patient_list;
        }

        public void setPatient_get_patient_list(Patient_get_patient_list patient_get_patient_list) {
            this.patient_get_patient_list = patient_get_patient_list;
        }
    }

    public class Patient_get_patient_list {

        @SerializedName("Patient_UserID")
        private String Patient_UserID;
        @SerializedName("PatientID")
        private String PatientID;
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
        @SerializedName("MobilePhone")
        private String MobilePhone;
        @SerializedName("Phone")
        private String Phone;
        @SerializedName("Country_Code")
        private String Country_Code;
        @SerializedName("Status")
        private String Status;
        @SerializedName("Name_Title")
        private String Name_Title;
        @SerializedName("Preferred_Name")
        private String Preferred_Name;
        @SerializedName("Name_Suffix")
        private String Name_Suffix;
        @SerializedName("Last_Login_DateTime")
        private String Last_Login_DateTime;
        @SerializedName("Activation_Status")
        private String Activation_Status;
        @SerializedName("Street1")
        private String Street1;
        @SerializedName("City")
        private String City;
        @SerializedName("State")
        private String State;
        @SerializedName("Country")
        private String Country;
        @SerializedName("ZipCode")
        private String ZipCode;
        @SerializedName("Gender")
        private String Gender;
        @SerializedName("Birth_Date")
        private String Birth_Date;
        @SerializedName("Note")
        private String Note;
        @SerializedName("Social_Security_Number")
        private String Social_Security_Number;
        @SerializedName("Last_Digit_Social_Security_Number")
        private String Last_Digit_Social_Security_Number;
        @SerializedName("Age")
        private String Age;
        @SerializedName("Modified_Age")
        private String Modified_Age;
        @SerializedName("SMS_Preference")
        private boolean SMS_Preference;

        public String getPatient_UserID() {
            return Patient_UserID;
        }

        public void setPatient_UserID(String patient_UserID) {
            Patient_UserID = patient_UserID;
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

        public String getMobilePhone() {
            return MobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            MobilePhone = mobilePhone;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getCountry_Code() {
            return Country_Code;
        }

        public void setCountry_Code(String country_Code) {
            Country_Code = country_Code;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
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

        public String getLast_Login_DateTime() {
            return Last_Login_DateTime;
        }

        public void setLast_Login_DateTime(String last_Login_DateTime) {
            Last_Login_DateTime = last_Login_DateTime;
        }

        public String getActivation_Status() {
            return Activation_Status;
        }

        public void setActivation_Status(String activation_Status) {
            Activation_Status = activation_Status;
        }

        public String getStreet1() {
            return Street1;
        }

        public void setStreet1(String street1) {
            Street1 = street1;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public String getZipCode() {
            return ZipCode;
        }

        public void setZipCode(String zipCode) {
            ZipCode = zipCode;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getBirth_Date() {
            return Birth_Date;
        }

        public void setBirth_Date(String birth_Date) {
            Birth_Date = birth_Date;
        }

        public String getNote() {
            return Note;
        }

        public void setNote(String note) {
            Note = note;
        }

        public String getSocial_Security_Number() {
            return Social_Security_Number;
        }

        public void setSocial_Security_Number(String social_Security_Number) {
            Social_Security_Number = social_Security_Number;
        }

        public String getLast_Digit_Social_Security_Number() {
            return Last_Digit_Social_Security_Number;
        }

        public void setLast_Digit_Social_Security_Number(String last_Digit_Social_Security_Number) {
            Last_Digit_Social_Security_Number = last_Digit_Social_Security_Number;
        }

        public String getAge() {
            return Age;
        }

        public void setAge(String age) {
            Age = age;
        }

        public String getModified_Age() {
            return Modified_Age;
        }

        public void setModified_Age(String modified_Age) {
            Modified_Age = modified_Age;
        }

        public boolean isSMS_Preference() {
            return SMS_Preference;
        }

        public void setSMS_Preference(boolean SMS_Preference) {
            this.SMS_Preference = SMS_Preference;
        }
    }


}
