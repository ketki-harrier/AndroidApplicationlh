package com.lifecyclehealth.lifecyclehealth.fingerprint;

/**
 * Created by satyam on 25/11/2016.
 */

/********************************************
 * MODEL CLASS STARTS
 **********************************/
public class TouchModel {
    private String mobileNumber;
    private boolean touchStatusOnDevice;
    private boolean touchStatusOnServer;
    private String touchSaltValue;
    private String mobilePin;
    private String dateOfAdded;
    private String _Id;
    private boolean isUserActive;
    private boolean isDontAskMeAgainOn;

    public TouchModel() {
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isTouchStatusOnDevice() {
        return touchStatusOnDevice;
    }

    public void setTouchStatusOnDevice(boolean touchStatusOnDevice) {
        this.touchStatusOnDevice = touchStatusOnDevice;
    }

    public boolean isTouchStatusOnServer() {
        return touchStatusOnServer;
    }

    public void setTouchStatusOnServer(boolean touchStatusOnServer) {
        this.touchStatusOnServer = touchStatusOnServer;
    }

    public String getTouchSaltValue() {
        return touchSaltValue;
    }

    public void setTouchSaltValue(String touchSaltValue) {
        this.touchSaltValue = touchSaltValue;
    }

    public String getMobilePin() {
        return mobilePin;
    }

    public void setMobilePin(String mobilePin) {
        this.mobilePin = mobilePin;
    }

    public String getDateOfAdded() {
        return dateOfAdded;
    }

    public void setDateOfAdded(String dateOfAdded) {
        this.dateOfAdded = dateOfAdded;
    }

    public String get_Id() {
        return _Id;
    }

    public void set_Id(String _Id) {
        this._Id = _Id;
    }

    public boolean isUserActive() {
        return isUserActive;
    }

    public void setUserActive(boolean userActive) {
        isUserActive = userActive;
    }

    public boolean isDontAskMeAgainOn() {
        return isDontAskMeAgainOn;
    }

    public void setDontAskMeAgainOn(boolean dontAskMeAgainOn) {
        isDontAskMeAgainOn = dontAskMeAgainOn;
    }

    @Override
    public String toString() {
        return "TouchModel{" +
                "mobileNumber='" + mobileNumber + '\'' +
                ", touchStatusOnDevice=" + touchStatusOnDevice +
                ", touchStatusOnServer=" + touchStatusOnServer +
                ", touchSaltValue='" + touchSaltValue + '\'' +
                ", mobilePin='" + mobilePin + '\'' +
                ", dateOfAdded='" + dateOfAdded + '\'' +
                ", _Id='" + _Id + '\'' +
                ", isUserActive=" + isUserActive +
                ", isDontAskMeAgainOn=" + isDontAskMeAgainOn +
                '}';
    }
}
