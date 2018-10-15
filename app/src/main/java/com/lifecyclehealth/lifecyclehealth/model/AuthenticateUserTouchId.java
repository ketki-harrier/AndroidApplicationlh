package com.lifecyclehealth.lifecyclehealth.model;

/**
 * Created by vaibhavi on 25-09-2017.
 */

public class AuthenticateUserTouchId {
    private String username;
    private String password;
    private String role;
    private boolean isFirstTimeLogin;
    private String timezone;

    public boolean isTouchID() {
        return isTouchID;
    }

    @Override
    public String toString() {
        return "AuthenticateUserTouchId{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isFirstTimeLogin=" + isFirstTimeLogin +
                ", timezone='" + timezone + '\'' +
                ", isTouchID=" + isTouchID +
                '}';
    }

    public void setTouchID(boolean touchID) {
        isTouchID = touchID;
    }

    private boolean isTouchID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String rol) {
        this.role = rol;
    }

    public boolean isFirstTimeLogin() {
        return isFirstTimeLogin;
    }

    public void setFirstTimeLogin(boolean firstTimeLogin) {
        isFirstTimeLogin = firstTimeLogin;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


}
