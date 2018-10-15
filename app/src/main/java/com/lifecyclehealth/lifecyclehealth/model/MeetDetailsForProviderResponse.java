package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 10-10-2017.
 */

public class MeetDetailsForProviderResponse {

    @SerializedName("status")
    private String status;

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

    public MeetDetails getMeetDetails() {
        return meetDetails;
    }

    public void setMeetDetails(MeetDetails meetDetails) {
        this.meetDetails = meetDetails;
    }

    @SerializedName("message")

    private String message;
    @SerializedName("meetDetails")
    private MeetDetails meetDetails;


    public class MeetDetails {
        @SerializedName("topic")
        private String topic;
        @SerializedName("Patient_Name")
        private String Patient_Name;
        @SerializedName("Episode_Care_Plan_Name")
        private String Episode_Care_Plan_Name;

        public String getPatient_Name() {
            return Patient_Name;
        }

        public void setPatient_Name(String patient_Name) {
            Patient_Name = patient_Name;
        }

        public String getEpisode_Care_Plan_Name() {
            return Episode_Care_Plan_Name;
        }

        public void setEpisode_Care_Plan_Name(String episode_Care_Plan_Name) {
            Episode_Care_Plan_Name = episode_Care_Plan_Name;
        }

        @SerializedName("session_key")
        private String session_key;
        @SerializedName("binder_id")
        private String binder_id;
        @SerializedName("status")
        private String status;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getBinder_id() {
            return binder_id;
        }

        public void setBinder_id(String binder_id) {
            this.binder_id = binder_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getScheduled_starts() {
            return scheduled_starts;
        }

        public void setScheduled_starts(String scheduled_starts) {
            this.scheduled_starts = scheduled_starts;
        }

        public String getScheduled_ends() {
            return scheduled_ends;
        }

        public void setScheduled_ends(String scheduled_ends) {
            this.scheduled_ends = scheduled_ends;
        }

        public String getAgenda() {
            return agenda;
        }

        public void setAgenda(String agenda) {
            this.agenda = agenda;
        }

        public String getJoin_before_minutes() {
            return join_before_minutes;
        }

        public void setJoin_before_minutes(String join_before_minutes) {
            this.join_before_minutes = join_before_minutes;
        }

        public boolean isRecord_multiple_video_channel() {
            return record_multiple_video_channel;
        }

        public void setRecord_multiple_video_channel(boolean record_multiple_video_channel) {
            this.record_multiple_video_channel = record_multiple_video_channel;
        }

        public String getTotal_participants() {
            return total_participants;
        }

        public void setTotal_participants(String total_participants) {
            this.total_participants = total_participants;
        }

        public String getTotal_invitees() {
            return total_invitees;
        }

        public void setTotal_invitees(String total_invitees) {
            this.total_invitees = total_invitees;
        }

        public boolean isHas_recording() {
            return has_recording;
        }

        public void setHas_recording(boolean has_recording) {
            this.has_recording = has_recording;
        }

        public boolean isStarted_recording() {
            return started_recording;
        }

        public void setStarted_recording(boolean started_recording) {
            this.started_recording = started_recording;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getUpdated_time() {
            return updated_time;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }

        public String getOriginal_binder_id() {
            return original_binder_id;
        }

        public void setOriginal_binder_id(String original_binder_id) {
            this.original_binder_id = original_binder_id;
        }

        public boolean isAuto_recording() {
            return auto_recording;
        }

        public void setAuto_recording(boolean auto_recording) {
            this.auto_recording = auto_recording;
        }

        public String getMeeting_Type() {
            return Meeting_Type;
        }

        public void setMeeting_Type(String meeting_Type) {
            Meeting_Type = meeting_Type;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public List<Invitees> getInvitees() {
            return invitees;
        }

        public void setInvitees(List<Invitees> invitees) {
            this.invitees = invitees;
        }

        public List<Tags> getTags() {
            return tags;
        }

        public void setTags(List<Tags> tags) {
            this.tags = tags;
        }

        public List<DialingNumbers> getDialin_numbers() {
            return dialin_numbers;
        }

        public void setDialin_numbers(List<DialingNumbers> dialin_numbers) {
            this.dialin_numbers = dialin_numbers;
        }

        @SerializedName("scheduled_starts")
        private String scheduled_starts;
        @SerializedName("scheduled_ends")
        private String scheduled_ends;
        @SerializedName("agenda")
        private String agenda;
        @SerializedName("join_before_minutes")
        private String join_before_minutes;
        @SerializedName("record_multiple_video_channel")
        private boolean record_multiple_video_channel;
        @SerializedName("total_participants")
        private String total_participants;
        @SerializedName("total_invitees")
        private String total_invitees;
        @SerializedName("has_recording")
        private boolean has_recording;
        @SerializedName("started_recording")
        private boolean started_recording;
        @SerializedName("created_time")
        private String created_time;
        @SerializedName("updated_time")
        private String updated_time;
        @SerializedName("original_binder_id")
        private String original_binder_id;
        @SerializedName("auto_recording")
        private boolean auto_recording;
        @SerializedName("Meeting_Type")
        private String Meeting_Type;
        @SerializedName("duration")
        private String duration;
        @SerializedName("invitees")
        private List<Invitees> invitees;
        @SerializedName("tags")
        private List<Tags> tags;
        @SerializedName("dialin_numbers")
        private List<DialingNumbers> dialin_numbers;
    }

    public class Invitees {
        @SerializedName("UserID")
        public String UserID;
        @SerializedName("FirstName")
        public String FirstName;
        @SerializedName("LastName")
        public String LastName;
        @SerializedName("FullName")
        public String FullName;
        @SerializedName("EmailID")
        public String EmailID;
        @SerializedName("Profile_Pic_Path")
        public String Profile_Pic_Path;
        @SerializedName("Profile_Picture_Content_Type")
        public String Profile_Picture_Content_Type;
        @SerializedName("MoxtraID")
        public String MoxtraID;
        @SerializedName("Designate_FullName")
        public String Designate_FullName;
        @SerializedName("LoggedInUser")
        public boolean LoggedInUser;
        @SerializedName("Designate_Exist")
        public boolean Designate_Exist;
        @SerializedName("RoleName")
        public ArrayList<String> RoleName;

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

        public String getDesignate_FullName() {
            return Designate_FullName;
        }

        public void setDesignate_FullName(String designate_FullName) {
            Designate_FullName = designate_FullName;
        }

        public boolean isLoggedInUser() {
            return LoggedInUser;
        }

        public void setLoggedInUser(boolean loggedInUser) {
            LoggedInUser = loggedInUser;
        }

        public boolean isDesignate_Exist() {
            return Designate_Exist;
        }

        public void setDesignate_Exist(boolean designate_Exist) {
            Designate_Exist = designate_Exist;
        }

        public ArrayList<String> getRoleName() {
            return RoleName;
        }

        public void setRoleName(ArrayList<String> roleName) {
            RoleName = roleName;
        }
    }

    public class Tags {
        @SerializedName("name")
        private String name;
        @SerializedName("value")
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public class DialingNumbers {
        @SerializedName("phone_number")
        private String phone_number;
        @SerializedName("location")
        private String location;

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

}
