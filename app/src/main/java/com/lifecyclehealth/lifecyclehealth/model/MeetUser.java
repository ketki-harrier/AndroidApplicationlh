package com.lifecyclehealth.lifecyclehealth.model;

/**
 * Created by vaibhavi on 21-08-2017.
 */

public class MeetUser {

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }


    public String getOffSet() {
        return OffSet;
    }

    public void setOffSet(String offSet) {
        OffSet = offSet;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    private String OffSet;
    private String start;
    private String days;


    @Override
    public String toString() {
        return "MeetUser{" +
                "OffSet='" + OffSet + '\'' +
                ", start='" + start + '\'' +
                ", days='" + days + '\'' +
                '}';
    }
}
