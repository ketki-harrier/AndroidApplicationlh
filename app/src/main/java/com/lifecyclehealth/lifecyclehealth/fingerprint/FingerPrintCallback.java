package com.lifecyclehealth.lifecyclehealth.fingerprint;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * Created by satyam on 21/11/2016.
 */

public interface FingerPrintCallback {
    void onSuccess(FingerprintManagerCompat.AuthenticationResult result);
    void onError(String error);
    void onMaxAttemptReach();
    void onCancelDialog();
}
