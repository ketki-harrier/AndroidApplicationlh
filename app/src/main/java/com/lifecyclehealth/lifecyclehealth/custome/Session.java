package com.lifecyclehealth.lifecyclehealth.custome;

import com.moxtra.sdk.chat.model.Chat;

/**
 * Created by vaibhavi on 16-10-2017.
 */

public class Session {
    final public boolean isMeet;
    public Chat chat;

    public Session(Chat chat) {
        this.chat = chat;
        this.isMeet = false;
    }
}
