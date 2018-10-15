package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 04-09-2017.
 */

public class ProfileGetResponse {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private User user;

    public class User{

        public UserDetails getUser_details() {
            return user_details;
        }

        public void setUser_details(UserDetails user_details) {
            this.user_details = user_details;
        }

        @SerializedName("user_details")
        private UserDetails user_details;
    }

    public class UserDetails{

        @SerializedName("FirstName")
        private String FirstName;
        @SerializedName("LastName")
        private String LastName ;
        @SerializedName("EmailID")
        private String EmailID;
        @SerializedName("Username")
        private String Username;
        @SerializedName("Phone")
        private String Phone;
        @SerializedName("MobilePhone")
        private String MobilePhone;
        @SerializedName("Country_Code")
        private String Country_Code;
        @SerializedName("Street1")
        private String Street1;
        @SerializedName("Street2")
        private String Street2;
        @SerializedName("City")
        private String City;
        @SerializedName("State")
        private String State;
        @SerializedName("Name_Title")
        private String Name_Title;
        @SerializedName("Preferred_Name")
        private String Preferred_Name;
        @SerializedName("Name_Suffix")
        private String Name_Suffix;
        @SerializedName("Country")
        private String Country;
        @SerializedName("Birth_Date")
        private String Birth_Date;
        @SerializedName("Age")
        private String Age;
        @SerializedName("Modified_Age")
        private String Modified_Age;
        @SerializedName("Gender")
        private String Gender;
        @SerializedName("ZipCode")
        private String ZipCode;
        @SerializedName("UserID")
        private String UserID;
        @SerializedName("National_Provider_Identifier")
        private String National_Provider_Identifier;
        @SerializedName("SMS_Preference")
        private boolean SMS_Preference;
        @SerializedName("Auto_Join_Audio")
        private boolean Auto_Join_Audio;
        @SerializedName("Designate_UserID")
        private String Designate_UserID;

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

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String phone) {
            Phone = phone;
        }

        public String getMobilePhone() {
            return MobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            MobilePhone = mobilePhone;
        }

        public String getCountry_Code() {
            return Country_Code;
        }

        public void setCountry_Code(String country_Code) {
            Country_Code = country_Code;
        }

        public String getStreet1() {
            return Street1;
        }

        public void setStreet1(String street1) {
            Street1 = street1;
        }

        public String getStreet2() {
            return Street2;
        }

        public void setStreet2(String street2) {
            Street2 = street2;
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

        public String getCountry() {
            return Country;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public String getBirth_Date() {
            return Birth_Date;
        }

        public void setBirth_Date(String birth_Date) {
            Birth_Date = birth_Date;
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

        public String getGender() {
            return Gender;
        }

        public void setGender(String gender) {
            Gender = gender;
        }

        public String getZipCode() {
            return ZipCode;
        }

        public void setZipCode(String zipCode) {
            ZipCode = zipCode;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String userID) {
            UserID = userID;
        }

        public String getNational_Provider_Identifier() {
            return National_Provider_Identifier;
        }

        public void setNational_Provider_Identifier(String national_Provider_Identifier) {
            National_Provider_Identifier = national_Provider_Identifier;
        }

        public boolean isSMS_Preference() {
            return SMS_Preference;
        }

        public void setSMS_Preference(boolean SMS_Preference) {
            this.SMS_Preference = SMS_Preference;
        }

        public boolean isAuto_Join_Audio() {
            return Auto_Join_Audio;
        }

        public void setAuto_Join_Audio(boolean auto_Join_Audio) {
            Auto_Join_Audio = auto_Join_Audio;
        }

        public String getDesignate_UserID() {
            return Designate_UserID;
        }

        public void setDesignate_UserID(String designate_UserID) {
            Designate_UserID = designate_UserID;
        }
    }

}
