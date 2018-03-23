package com.lifecyclehealth.lifecyclehealth.fingerprint;

import java.util.ArrayList;

/**
 * Created by satyam on 25/11/2016.
 */

public interface FingerPrintActionsCallback {
    void addUser(TouchModel model);
    ArrayList<TouchModel> getAllRegisterUser();
    void updateTouchOnServer(TouchModel model, boolean isEnable);
    void updateTouchOnDevice(TouchModel model, boolean isEnable);
    void updateUser(TouchModel model);
    int getDataBaseCount();
}
