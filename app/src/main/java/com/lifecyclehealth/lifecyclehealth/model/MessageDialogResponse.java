package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaibhavi on 25-10-2017.
 */

public class MessageDialogResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("unread_feeds")
    private String unread_feeds;
    @SerializedName("Unread_Messages")
    private List<Unread_Message> Unread_Messages;

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

    public String getUnread_feeds() {
        return unread_feeds;
    }

    public void setUnread_feeds(String unread_feeds) {
        this.unread_feeds = unread_feeds;
    }

    public List<Unread_Message> getUnread_Messages() {
        return Unread_Messages;
    }

    public void setUnread_Messages(List<Unread_Message> unread_Messages) {
        Unread_Messages = unread_Messages;
    }

    public class Unread_Message {
        @SerializedName("category")
        private String category;
        @SerializedName("binder")
        private Binder binder;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Binder getBinder() {
            return binder;
        }

        public void setBinder(Binder binder) {
            this.binder = binder;
        }
    }


    public class Binder {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("created_time")
        private String created_time;
        @SerializedName("updated_time")
        private String updated_time;
        @SerializedName("total_comments")
        private String total_comments;
        @SerializedName("total_members")
        private String total_members;
        @SerializedName("total_pages")
        private String total_pages;
        @SerializedName("total_todos")
        private String total_todos;
        @SerializedName("revision")
        private String revision;
        @SerializedName("thumbnail_uri")
        private String thumbnail_uri;
        @SerializedName("conversation")
        private boolean conversation;
        @SerializedName("restricted")
        private boolean restricted;
        @SerializedName("team")
        private boolean team;
        @SerializedName("favorite")
        private boolean favorite;
        @SerializedName("description")
        private String description;
        @SerializedName("feeds_timestamp")
        private String feeds_timestamp;
        @SerializedName("status")
        private String status;
        @SerializedName("binder_email")
        private String binder_email;
        @SerializedName("unread_feeds")
        private int unread_feeds;
        @SerializedName("tags")
        private List<Tags> tags;
        @SerializedName("last_feed")
        private LastFeed last_feed;

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

        public boolean isFavorite() {
            return favorite;
        }

        public void setFavorite(boolean favorite) {
            this.favorite = favorite;
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

        public int getUnread_feeds() {
            return unread_feeds;
        }

        public void setUnread_feeds(int unread_feeds) {
            this.unread_feeds = unread_feeds;
        }

        public List<Tags> getTags() {
            return tags;
        }

        public void setTags(List<Tags> tags) {
            this.tags = tags;
        }

        public LastFeed getLast_feed() {
            return last_feed;
        }

        public void setLast_feed(LastFeed last_feed) {
            this.last_feed = last_feed;
        }
    }

    public class LastFeed {
        @SerializedName("published")
        private String published;
        @SerializedName("object")
        private Object object;

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    public class Object{
        @SerializedName("published")
        private String published;
        @SerializedName("updated")
        private String updated;
        @SerializedName("objectType")
        private String objectType;
        @SerializedName("content")
        private String content;
        @SerializedName("content_text")
        private String content_text;
        @SerializedName("content_richtext")
        private String content_richtext;
        @SerializedName("id")
        private String id;

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_text() {
            return content_text;
        }

        public void setContent_text(String content_text) {
            this.content_text = content_text;
        }

        public String getContent_richtext() {
            return content_richtext;
        }

        public void setContent_richtext(String content_richtext) {
            this.content_richtext = content_richtext;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
}
