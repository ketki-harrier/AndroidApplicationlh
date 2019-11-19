package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;
import java.util.List;

public class CarousalDTO {

    private float status;
    private String message;
    ArrayList<String> Urls = new ArrayList<String>();
    ArrayList<Object> imageArray = new ArrayList<Object>();
    private float count;


    // Getter Methods

    public float getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public float getCount() {
        return count;
    }

    public ArrayList<String> getUrls() {
        return Urls;
    }

    // Setter Methods

    public void setStatus(float status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public void setUrls(ArrayList<String> Urls) {
        this.Urls = Urls;
    }

    @Override
    public String toString() {
        return "CarousalDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", Urls='" + Urls + '\'' +
                '}';
    }

}




