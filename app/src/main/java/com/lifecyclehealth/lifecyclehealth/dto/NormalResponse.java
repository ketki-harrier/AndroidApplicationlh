package com.lifecyclehealth.lifecyclehealth.dto;

/**
 * Created by satyam on 21/04/2017.
 */

public class NormalResponse {
    private String message;
    private String status;


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

    @Override
    public String toString() {
        return "NormalResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
