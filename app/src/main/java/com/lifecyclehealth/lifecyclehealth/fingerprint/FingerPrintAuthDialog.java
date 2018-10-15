package com.lifecyclehealth.lifecyclehealth.fingerprint;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;


/**
 * Created by satyam on 21/11/2016.
 */
//@TargetApi(23)
public class FingerPrintAuthDialog extends DialogFragment implements
        FingerPrintHelperClass.FingerprintHelperListener {

    private Button mCancelButton;
    FingerPrintHelperClass fingerPrintHelperClass;
    private FingerprintManagerCompat mManagerCompat;
    private FingerprintManagerCompat.CryptoObject cryptoObject;
    FingerPrintCallback mPrintCallback;
    @Override
    public void authenticationFailed(String error) {
        if (mPrintCallback != null) mPrintCallback.onError(error);
        fingerPrintHelperClass.stopListening();
        if (error.equalsIgnoreCase(FingerPrintUtil.SYSTEM_MESSAGE)){
            dismiss();
        }
    }

    @Override
    public void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        if (mPrintCallback != null) mPrintCallback.onSuccess(result);
        dismiss();
    }

    @Override
    public void onMaxAttemptReach() {
        dismiss();
        if (mPrintCallback != null)
            mPrintCallback.onMaxAttemptReach();
        fingerPrintHelperClass.stopListening();
    }

    @Override
    public void onCancelDialog() {
        if (mPrintCallback!=null)
            mPrintCallback.onCancelDialog();
        dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.fingerprint_main_dia_title));
        View v = inflater.inflate(R.layout.dialog_fingerprint_container, container, false);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPrintCallback!=null)
                    mPrintCallback.onCancelDialog();
                dismiss();
            }
        });
        fingerPrintHelperClass = new FingerPrintHelperClass(this,
                (ImageView) v.findViewById(R.id.fingerprint_icon),
                (TextView) v.findViewById(R.id.fingerprint_status)
        );
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        fingerPrintHelperClass.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fingerPrintHelperClass != null) {
            if (mManagerCompat != null && cryptoObject != null)
                fingerPrintHelperClass.startListening(mManagerCompat, cryptoObject);
        }
    }

    /*
        this is important to set method
     */
    public void setManagerCompatNCrypto(FingerprintManagerCompat managerCompat, FingerprintManagerCompat.CryptoObject object) {
        mManagerCompat = managerCompat;
        cryptoObject = object;
    }

    public void setCallbackListener(FingerPrintCallback printCallback) {
        mPrintCallback = printCallback;
    }

}
