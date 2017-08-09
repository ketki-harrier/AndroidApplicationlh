package com.harrier.lifecyclehealth.dto;

import com.google.gson.annotations.SerializedName;
import com.harrier.lifecyclehealth.model.User;

/**
 * Created by satyam on 19/04/2017.
 */

public class AuthenticateUserDTO {
    private String status;
    private String message;
    private String token;
    private User user;
    @SerializedName("Password")
    private String password;

    public AuthenticateUserDTO() {
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticateUserDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", password='" + password + '\'' +
                '}';
    }
}
