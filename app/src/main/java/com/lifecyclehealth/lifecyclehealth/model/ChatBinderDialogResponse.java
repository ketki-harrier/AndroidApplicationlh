package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 27-10-2017.
 */

public class ChatBinderDialogResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("binderDetails")
    private BinderDetails binderDetails;

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

    public BinderDetails getBinderDetails() {
        return binderDetails;
    }

    public void setBinderDetails(BinderDetails binderDetails) {
        this.binderDetails = binderDetails;
    }

    public class BinderDetails {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("created_time")
        private String created_time;
        private String updated_time;
        private String total_comments;
        private String total_members;
        private String total_pages;
        private String total_todos;
        private String revision;
        private String thumbnail_uri;
        private boolean conversation;
        private boolean restricted;
        private boolean team;
        private String description;
        private String feeds_timestamp;
        private String status;
        private String binder_email;
        @SerializedName("unread_feeds")
        private String unread_feeds;
        private boolean favorite;
        @SerializedName("PatientID")
        private String PatientID;
        @SerializedName("Patient_UserID")
        private String Patient_UserID;
        @SerializedName("FullName")
        private String FullName;
        @SerializedName("Episode_Care_PlanID")
        private String Episode_Care_PlanID;
        @SerializedName("invitees")
        private ArrayList<Invitees> invitees;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }

        public void setUpdated_time(String updated_time) {
            this.updated_time = updated_time;
        }

        public String getTotal_comments() {
            return total_comments;
        }

        public void setTotal_comments(String total_comments) {
            this.total_comments = total_comments;
        }

        public String getTotal_members() {
            return total_members;
        }

        public void setTotal_members(String total_members) {
            this.total_members = total_members;
        }

        public String getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(String total_pages) {
            this.total_pages = total_pages;
        }

        public String getTotal_todos() {
            return total_todos;
        }

        public void setTotal_todos(String total_todos) {
            this.total_todos = total_todos;
        }

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }

        public String getThumbnail_uri() {
            return thumbnail_uri;
        }

        public void setThumbnail_uri(String thumbnail_uri) {
            this.thumbnail_uri = thumbnail_uri;
        }

        public boolean isConversation() {
            return conversation;
        }

        public void setConversation(boolean conversation) {
            this.conversation = conversation;
        }

        public boolean isRestricted() {
            return restricted;
        }

        public void setRestricted(boolean restricted) {
            this.restricted = restricted;
        }

        public boolean isTeam() {
            return team;
        }

        public void setTeam(boolean team) {
            this.team = team;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFeeds_timestamp() {
            return feeds_timestamp;
        }

        public void setFeeds_timestamp(String feeds_timestamp) {
            this.feeds_timestamp = feeds_timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBinder_email() {
            return binder_email;
        }

        public void setBinder_email(String binder_email) {
            this.binder_email = binder_email;
        }

        public String getUnread_feeds() {
            return unread_feeds;
        }

        public void setUnread_feeds(String unread_feeds) {
            this.unread_feeds = unread_feeds;
        }

        public boolean isFavorite() {
            return favorite;
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

        public String getPatient_UserID() {
            return Patient_UserID;
        }

        public void setPatient_UserID(String patient_UserID) {
            Patient_UserID = patient_UserID;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public ArrayList<Invitees> getInvitees() {
            return invitees;
        }

        public void setInvitees(ArrayList<Invitees> invitees) {
            this.invitees = invitees;
        }
    }

    public class Invitees {

        @SerializedName("id")
        private String id;
        @SerializedName("email")
        private String email;
        @SerializedName("name")
        private String name;
        @SerializedName("picture_uri")
        private String picture_uri;
        @SerializedName("phone_number")
        private String phone_number;
        @SerializedName("unique_id")
        private String unique_id;
        @SerializedName("org_id")
        private String org_id;
        @SerializedName("member_type")
        private String member_type;
        @SerializedName("role")
        private ArrayList<String> role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture_uri() {
            return picture_uri;
        }

        public void setPicture_uri(String picture_uri) {
            this.picture_uri = picture_uri;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getUnique_id() {
            return unique_id;
        }

        public void setUnique_id(String unique_id) {
            this.unique_id = unique_id;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public String getMember_type() {
            return member_type;
        }

        public void setMember_type(String member_type) {
            this.member_type = member_type;
        }

        public ArrayList<String> getRole() {
            return role;
        }

        public void setRole(ArrayList<String> role) {
            this.role = role;
        }
    }

}
