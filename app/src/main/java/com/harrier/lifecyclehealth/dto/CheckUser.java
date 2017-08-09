package com.harrier.lifecyclehealth.dto;

/**
 * Created by satyam on 19/04/2017.
 */

public class CheckUser {
    private String status;
    private String message;
    private boolean isUsernameExist;
    private boolean isFirstTimeLogin;


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

    public boolean isUsernameExist() {
        return isUsernameExist;
    }

    public void setUsernameExist(boolean usernameExist) {
        isUsernameExist = usernameExist;
    }

    public boolean isFirstTimeLogin() {
        return isFirstTimeLogin;
    }

    public void setFirstTimeLogin(boolean firstTimeLogin) {
        isFirstTimeLogin = firstTimeLogin;
    }

    @Override
    public String toString() {
        return "CheckUser{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", isUsernameExist=" + isUsernameExist +
                ", isFirstTimeLogin=" + isFirstTimeLogin +
                '}';
    }
}
