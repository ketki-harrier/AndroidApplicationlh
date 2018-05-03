package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by satyam on 19/04/2017.
 */

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    @SerializedName("Username")
    private String userName;
    private String mobileNo;
    private String countryCode;
    private ArrayList<String> role;
    private ArrayList<String> clinicId;
    @SerializedName("HealthCare_OrgID")
    private ArrayList<String> healthCareOrgId;
    @SerializedName("HealthCare_Org_Name")
    private ArrayList<String> healthCareOrgName;
    @SerializedName("PatientID")
    private String patientId;
    @SerializedName("PhysicianID")
    private String physicianID;
    @SerializedName("IsPassword_Autogenerated")
    private boolean isPasswordAutogenerated;
    @SerializedName("Is_MobilePhone_Verify")
    private boolean isMobilePhoneVerify;
    @SerializedName("IsMobile_Carousal_Saw")
    private boolean isMobileCarousalSaw;
    @SerializedName("IsWeb_Carousal_Saw")
    private boolean isWebCarousalSaw;
    @SerializedName("IsTouch_Enable")
    private boolean isTouchEnable;
    @SerializedName("Moxtra_UserID")
    private String moxtraUserID;
    @SerializedName("Moxtra_UniqueID")
    private String moxtraUniqueID;
    @SerializedName("Moxtra_Org_id")
    private String moxtraOrgid;
    @SerializedName("Moxtra_Access_Token")
    private String MoxtraAccessToken;
    @SerializedName("IsPassword_Expired")
    private boolean IsPassword_Expired;

    public boolean isPassword_Expired() {
        return IsPassword_Expired;
    }

    public void setPassword_Expired(boolean password_Expired) {
        IsPassword_Expired = password_Expired;
    }

    public boolean isIncomplete_Profile() {
        return IsIncomplete_Profile;
    }

    public void setIncomplete_Profile(boolean incomplete_Profile) {
        IsIncomplete_Profile = incomplete_Profile;
    }

    @SerializedName("IsIncomplete_Profile")
    private boolean IsIncomplete_Profile;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public ArrayList<String> getRole() {
        return role;
    }

    public void setRole(ArrayList<String> role) {
        this.role = role;
    }

    public ArrayList<String> getClinicId() {
        return clinicId;
    }

    public void setClinicId(ArrayList<String> clinicId) {
        this.clinicId = clinicId;
    }

    public ArrayList<String> getHealthCareOrgId() {
        return healthCareOrgId;
    }

    public void setHealthCareOrgId(ArrayList<String> healthCareOrgId) {
        this.healthCareOrgId = healthCareOrgId;
    }

    public ArrayList<String> getHealthCareOrgName() {
        return healthCareOrgName;
    }

    public void setHealthCareOrgName(ArrayList<String> healthCareOrgName) {
        this.healthCareOrgName = healthCareOrgName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPhysicianID() {
        return physicianID;
    }

    public void setPhysicianID(String physicianID) {
        this.physicianID = physicianID;
    }

    public boolean isPasswordAutogenerated() {
        return isPasswordAutogenerated;
    }

    public void setPasswordAutogenerated(boolean passwordAutogenerated) {
        isPasswordAutogenerated = passwordAutogenerated;
    }

    public boolean isMobilePhoneVerify() {
        return isMobilePhoneVerify;
    }

    public void setMobilePhoneVerify(boolean mobilePhoneVerify) {
        isMobilePhoneVerify = mobilePhoneVerify;
    }

    public boolean isMobileCarousalSaw() {
        return isMobileCarousalSaw;
    }

    public void setMobileCarousalSaw(boolean mobileCarousalSaw) {
        isMobileCarousalSaw = mobileCarousalSaw;
    }

    public boolean isWebCarousalSaw() {
        return isWebCarousalSaw;
    }

    public void setWebCarousalSaw(boolean webCarousalSaw) {
        isWebCarousalSaw = webCarousalSaw;
    }

    public boolean isTouchEnable() {
        return isTouchEnable;
    }

    public void setTouchEnable(boolean touchEnable) {
        isTouchEnable = touchEnable;
    }

    public String getMoxtraUserID() {
        return moxtraUserID;
    }

    public void setMoxtraUserID(String moxtraUserID) {
        this.moxtraUserID = moxtraUserID;
    }

    public String getMoxtraUniqueID() {
        return moxtraUniqueID;
    }

    public void setMoxtraUniqueID(String moxtraUniqueID) {
        this.moxtraUniqueID = moxtraUniqueID;
    }

    public String getMoxtraOrgid() {
        return moxtraOrgid;
    }

    public void setMoxtraOrgid(String moxtraOrgid) {
        this.moxtraOrgid = moxtraOrgid;
    }

    public String getMoxtraAccessToken() {
        return MoxtraAccessToken;
    }

    public void setMoxtraAccessToken(String moxtraAccessToken) {
        MoxtraAccessToken = moxtraAccessToken;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", role=" + role +
                ", clinicId=" + clinicId +
                ", healthCareOrgId=" + healthCareOrgId +
                ", healthCareOrgName=" + healthCareOrgName +
                ", patientId='" + patientId + '\'' +
                ", physicianID='" + physicianID + '\'' +
                ", isPasswordAutogenerated=" + isPasswordAutogenerated +
                ", isMobilePhoneVerify=" + isMobilePhoneVerify +
                ", isMobileCarousalSaw=" + isMobileCarousalSaw +
                ", isWebCarousalSaw=" + isWebCarousalSaw +
                ", isTouchEnable=" + isTouchEnable +
                ", moxtraUserID='" + moxtraUserID + '\'' +
                ", moxtraUniqueID='" + moxtraUniqueID + '\'' +
                ", moxtraOrgid='" + moxtraOrgid + '\'' +
                ", MoxtraAccessToken='" + MoxtraAccessToken + '\'' +

                '}';
    }
}
