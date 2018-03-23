package com.lifecyclehealth.lifecyclehealth.model;

/**
 * Created by satyam on 19/04/2017.
 */

public class AuthenticateUser {
    private String username;
    private String password;
    private String rol;
    private boolean isFirstTimeLogin;

    public boolean isIAcceptTermsAndConditions() {
        return IAcceptTermsAndConditions;
    }

    public void setIAcceptTermsAndConditions(boolean IAcceptTermsAndConditions) {
        this.IAcceptTermsAndConditions = IAcceptTermsAndConditions;
    }

    private boolean IAcceptTermsAndConditions;
    private String timezone;

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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


    @Override
    public String toString() {
        return "AuthenticateUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                ", isFirstTimeLogin=" + isFirstTimeLogin +
                ", IAcceptTermsAndConditions=" + IAcceptTermsAndConditions +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
