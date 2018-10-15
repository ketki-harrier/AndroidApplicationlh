package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.meet.model.Meet;

/**
 * Created by vaibhavi on 29-11-2017.
 */

public class MessageMeetModel1 {
    public int chatType;
    public Chat chatList;

    public Meet getMeetList() {
        return meetList;
    }

    public void setMeetList(Meet meetList) {
        this.meetList = meetList;
    }

    private Meet meetList;


    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public Chat getChatList() {
        return chatList;
    }

    public void setChatList(Chat chatList) {
        this.chatList = chatList;
    }


}
