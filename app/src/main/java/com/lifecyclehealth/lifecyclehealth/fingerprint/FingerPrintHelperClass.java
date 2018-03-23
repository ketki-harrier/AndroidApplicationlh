package com.lifecyclehealth.lifecyclehealth.fingerprint;

import android.annotation.TargetApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;


/**
 * Created by satyam on 18/11/2016.
 */
@TargetApi(23)
public class FingerPrintHelperClass extends
        FingerprintManagerCompat.AuthenticationCallback {
    private FingerprintHelperListener listener;
    private CancellationSignal cancellationSignal;
    private ImageView mIcon;
    private TextView mErrorTextView;
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;
    private boolean mSelfCancelled;
    private int numberOfAttempt = 0;

    /* constructor*/
    public FingerPrintHelperClass(FingerprintHelperListener listener) {
        this.listener = listener;
    }

    public FingerPrintHelperClass(FingerprintHelperListener listener, ImageView icon,
                                  TextView errorTextView) {
        this.listener = listener;
        this.mErrorTextView = errorTextView;
        this.mIcon = icon;
    }

    public interface FingerprintHelperListener {
        public void authenticationFailed(String error);

        public void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result);

        public void onMaxAttemptReach();

        public void onCancelDialog();
    }


    public void startListening(FingerprintManagerCompat managerCompat
            , FingerprintManagerCompat.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        mIcon.setImageResource(R.drawable.ic_fp_40px);
        try {
            managerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null);
        } catch (SecurityException e) {
            listener.authenticationFailed(e.getMessage());
        }
    }

    public void stopListening() {
        if (cancellationSignal != null) {
            mSelfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    public FingerPrintHelperClass() {
        super();
    }

    @Override
    public void onAuthenticationError(int errMsgId, final CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        if (!mSelfCancelled) {
            showError(errString);
            mIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listener != null)
                        listener.authenticationFailed(errString.toString());
                }
            }, ERROR_TIMEOUT_MILLIS);
        } else {
            if (numberOfAttempt == FingerPrintUtil.FLAG_MAX_ALLOW_ERROR) {
               // printLog("On Max Called onCancelCalled");
            } else {
                //printLog("onCancelCalled");
                if (listener != null)
                    listener.onCancelDialog();
            }
        }
       // printLog("in Error: " + errString);
        // listener.authenticationFailed(errString.toString());
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        //printLog("in Help: " + helpString);
        showError(helpString);
    }

    @Override
    public void onAuthenticationSucceeded(final FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));
        mIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.authenticationSucceeded(result);
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    @Override
    public void onAuthenticationFailed() {
        //printLog("in failed: ");
        showError(mIcon.getResources().getString(
                R.string.fingerprint_not_recognized));
        numberOfAttempt = numberOfAttempt + 1;
        if (numberOfAttempt == FingerPrintUtil.FLAG_MAX_ALLOW_ERROR) {
            listener.onMaxAttemptReach();
            stopListening();
        }
        //listener.authenticationFailed("Authentication failed");
    }

    private void showError(CharSequence error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }
    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    };
}
