package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.moxtra.sdk.chat.model.Chat;


import java.util.List;

/**
 * Created by vaibhavi on 09-11-2017.
 */

public class MessageMeetModel {



    public int chatType;

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    //public List<Chat> chatList;
    public Chat chatList;

    public MeetList getMeetList() {
        return meetList;
    }

    public void setMeetList(MeetList meetList) {
        this.meetList = meetList;
    }

    @SerializedName("meetList")
    private MeetList meetList;



    public Chat getChatList() {
        return chatList;
    }

    public void setChatList(Chat chatList) {
        this.chatList = chatList;
    }



    public class MeetList {

        @SerializedName("topic")
        private String topic;
        @SerializedName("session_key")
        private String session_key;
        @SerializedName("binder_id")
        private String binder_id;
        @SerializedName("status")
        private String status;
        @SerializedName("starts")
        private String starts;
        @SerializedName("ends")
        private String ends;
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
        @SerializedName("self_hosted")
        private boolean self_hosted;
        @SerializedName("invitees")
        private List<Invites> invitees;
        @SerializedName("participants")
        private List<Participants> participants;
        @SerializedName("desktopshares")
        private List<Desktopshares> desktopshares;


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

        public String getStarts() {
            return starts;
        }

        public void setStarts(String starts) {
            this.starts = starts;
        }

        public String getEnds() {
            return ends;
        }

        public void setEnds(String ends) {
            this.ends = ends;
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

        public boolean isSelf_hosted() {
            return self_hosted;
        }

        public void setSelf_hosted(boolean self_hosted) {
            this.self_hosted = self_hosted;
        }

        public List<Invites> getInvitees() {
            return invitees;
        }

        public void setInvitees(List<Invites> invitees) {
            this.invitees = invitees;
        }

        public List<Participants> getParticipants() {
            return participants;
        }

        public void setParticipants(List<Participants> participants) {
            this.participants = participants;
        }

        public List<Desktopshares> getDesktopshares() {
            return desktopshares;
        }

        public void setDesktopshares(List<Desktopshares> desktopshares) {
            this.desktopshares = desktopshares;
        }

        public List<Tags> getTags() {
            return tags;
        }

        public void setTags(List<Tags> tags) {
            this.tags = tags;
        }

        @SerializedName("tags")
        private List<Tags> tags;

    }


    public class Invites {

        @SerializedName("email")
        private String email;
        @SerializedName("name")
        private String name;
        @SerializedName("host")
        private boolean host;
        @SerializedName("unique_id")
        private String unique_id;
        @SerializedName("id")
        private String id;
        @SerializedName("status")
        private String status;
        @SerializedName("picture_uri")
        private String picture_uri;
        @SerializedName("team")
        private String team;

    }

    public class Participants {

        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("picture_uri")
        private String picture_uri;
        @SerializedName("starts")
        private String starts;
        @SerializedName("unique_id")
        private String unique_id;
        @SerializedName("roster_id")
        private String roster_id;
        @SerializedName("id")
        private String id;
        @SerializedName("participant_number")
        private String participant_number;
        @SerializedName("phone_number")
        private String phone_number;
        @SerializedName("host")
        private boolean host;
        @SerializedName("org_id")
        private String org_id;
        @SerializedName("status")
        private String status;
        @SerializedName("presenter")
        private boolean presenter;
        @SerializedName("invisible")
        private boolean invisible;

    }

    public class Desktopshares {

    }

    public class Tags {

        @SerializedName("name")
        private String name;
        @SerializedName("value")
        private String value;

    }

}

