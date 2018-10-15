package com.lifecyclehealth.lifecyclehealth.dto;

/**
 * Created by satyam on 27/04/2017.
 */

public class AuthenticateForgotCodeDTO {
    private String message;
    private String status;
    private String hashOTP;
    private String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHashOTP() {
        return hashOTP;
    }

    public void setHashOTP(String hashOTP) {
        this.hashOTP = hashOTP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuthenticateForgotCodeDTO{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", hashOTP='" + hashOTP + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
