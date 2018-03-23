package com.lifecyclehealth.lifecyclehealth.fingerprint;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by satyam on 21/11/2016.
 */

public class FingerPrintUtil {
    private static final String TAG = FingerPrintUtil.class.getSimpleName();
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final int ENCODE_FLAG = Base64.NO_PADDING;

    public static final int FLAG_REGISTER = 0;
    public static final int FLAG_LOGIN = 1;
    public static final int FLAG_MAX_ALLOW_ERROR = 2;

    private static final String KEY_ALIAS = "LifeCycleHealth";
    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String PREFERENCES_KEY_PASS = "pass";
    private static final String PREFERENCES_KEY_MOB = "mobileNumber";
    private static final String PREFERENCES_KEY_IV = "iv";

    private static final String FLAG_USER_ALLOW = "flag_user_allow";
    private static final String FLAG_USER_FING_ENABLED = "flag_user_fimgerprint_enabled";
    private static final String FLAG_USER_FING_DONTASK = "flag_user_fimgerprint_dontAsk";
    private static final String FLAG_USER_FING_ENCRYPTION = "flag_user_fingerprint_encryption";
    private static final String FLAG_USER_FING_SERVICE_FLAG = "flag_user_touch_enable_service";
    private static final String FLAG_USER_FING_TRADITIONAL_LOGIN = "flag_user_want_traditional_login";
    private static final String FLAG_USER_FING_UPDATE_TOUCH = "flag_user_update_touch";
    private static final String FLAG_USER_FING_UPDATE_T_ACTION = "flag_user_update_touch_action";
    private static final String FLAG_USER_FING_UPDATE_T_PRO_ACTION = "flag_user_update_touch_action";
    private static final String FLAG_USER_FING_UPDATE_T_PROFILE = "flag_user_update_prof_touch_action";
    private static final String FLAG_USER_FING_UPDATE_T_ENABLE = "flag_user_update_prof_touch_enable";
    private static final String FLAG_ACTIVE_USER = "flag_active_mobile";

    /* INTENT EXTRA */
    private static final String FLAG_EXTRA_RESPONSE = "flag_intent_extra_response";
    private static final String FLAG_EXTRA_IS_EMAIL = "flag_intent_extra_is_email";
    private static final String FLAG_EXTRA_BASE_AUTH = "flag_intent_extra_base_auth";

    public static final int CLICK_EVENT_YES = 2;
    public static final int CLICK_EVENT_NO = 3;
    public static final int CLICK_EVENT_OK = 4;
    public static final int CLICK_EVENT_DONT_ASK = 5;

    public static final String UPDATE_ENABLE = "ENABLE";
    public static final String UPDATE_DISABLE = "DISABLE";
    public static final String UPDATE_NONE = "NONE";
    public static final String UPDATE_PROFILE = "PROFILE";

    public static final String SYSTEM_MESSAGE = "Too many attempts. Try again later.";

    private UserRegisterNLoginTask task;
    //FingerPrintCallback printCallback;
    /* ****************************for key related Stuff*******************************/
    private KeyStore keyStore;
    private KeyGenerator generator;
    private Cipher cipher;
    private FingerprintManagerCompat fingerprintManager;
    private KeyguardManager keyguardManager;
    private FingerprintManagerCompat.CryptoObject cryptoObject;
    private FingerPrintCallback printCallback;
    private NextActivityCallback nextActivityCallback;
    private FingerprintStorage fingerprintStorage;
    private String DIALOG_FRAGMENT_TAG;
    private FragmentManager mFragmentManager;
    FingerPrintAuthDialog dialogFragment;
    // private HashMap<String, String> hashMapOfUser;

    /*Constructor*/

    public FingerPrintUtil() {
    }

    public FingerPrintUtil(Context context, FingerPrintCallback printCallback) {
        this.context = context;
        this.printCallback = printCallback;
        initValues();
    }

    /*
       for VerifyFourDigit Pin Activity added extra callback
     */
    public FingerPrintUtil(Context context, FingerPrintCallback printCallback, NextActivityCallback nextActivityCallback) {
        this.context = context;
        this.printCallback = printCallback;
        this.nextActivityCallback = nextActivityCallback;
        initValues();
    }
    /*Interface implementation */


    public Cipher getCipherAfterOperation(FingerprintManagerCompat.AuthenticationResult result) {
        return cipher = result.getCryptoObject().getCipher();
    }

    /* UTIL METHODS*/
    /* get Print Log on console */

    /* UTIL METHODS*/
    /* get Print Log on console */
    private void printDbLog(String logMessage) {
    }
    /*
        for handeling shared preference
     */




    /* Initialisation Of Members*/

    private void initValues() {
        keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = FingerprintManagerCompat.from(context);
        fingerprintStorage = new FingerprintStorage(context);
        //hashMapOfUser = prepareHashMap();
    }

    /*Get KeyStore*/
    private boolean getKeyStore() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);// creating empty key store
            return true;
        } catch (KeyStoreException | NoSuchAlgorithmException |
                CertificateException | IOException e) {
        }
        return false;
    }


    @TargetApi(23)
    private boolean createNewKey(boolean forceCreate) {
        try {
            // when invalidException thron then recreate new with alias
            if (forceCreate) keyStore.deleteEntry(KEY_ALIAS);
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE);
                generator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(true)
                        .build()
                );
                generator.generateKey();
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @TargetApi(23)
    private boolean getCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            return true;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        }
        return false;
    }

    @TargetApi(23)
    private boolean initCipher(int mode) {
        try {
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            if (mode == Cipher.ENCRYPT_MODE) {
                cipher.init(mode, secretKey);
                MyApplication.getInstance().addToSharedPreference(PREFERENCES_KEY_IV,
                        Base64.encodeToString(cipher.getIV(), ENCODE_FLAG));
            } else {
                byte[] iv = Base64.decode
                        ( MyApplication.getInstance().getFromSharedPreference(PREFERENCES_KEY_IV),
                                ENCODE_FLAG);
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(mode, secretKey, ivParameterSpec);
            }

            return true;
        } catch (InvalidKeyException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException |
                InvalidAlgorithmParameterException | IOException | CertificateException
                e) {
        }
        return false;
    }

    @TargetApi(23)
    private boolean iniCryptObject() {
        try {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    /*
       ************************************  OUTER SIDE ACCESSIBLE LOG **********************************************
     */

    /*ENCODING STRING */
    public void encryptString(String initialText) {
        try {
            byte[] bytes = cipher.doFinal(initialText.getBytes());
            String encryptedText = Base64.encodeToString(bytes, ENCODE_FLAG);
            updateOrAddInDb(encryptedText.trim());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
        }
    }

    /*DECODING  STRING */
    public void decryptString(String chipherText) {
        if (chipherText == null) {
            String mobile = getUserMobileNumber();
            if (mobile != null) {
                TouchModel touchModel = getUser(mobile);
                if (touchModel != null) {
                    chipherText = touchModel.getTouchSaltValue();
                }
            }
        }
        try {
            byte[] bytes = Base64.decode(chipherText, ENCODE_FLAG);
            String finalText = new String(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
        }
    }


    /*
       ********************************************************DEVICE CAPABILITIES ********************************************
   */
    public boolean isAndroidMorGreter() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    /*for getting capabilities of mobile phone for fingerprint*/
    public boolean isFingerPrintCapable() {
        if (!fingerprintManager.isHardwareDetected()) {
            return false;
            // Device doesn't support fingerprint authentication
        } else return true;
    }

    @TargetApi(23)
    public boolean isKeyguardSecurityAlright() {
        return keyguardManager.isKeyguardSecure();
    }

    boolean isFingerPrintEnrolled() {
        return fingerprintManager.hasEnrolledFingerprints();
    }

     /*
       ********************************************************DEVICE CAPABILITIES ENDS********************************************
   */


    public class UserRegisterNLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mMobile;
        private final Boolean mRegister;

        public UserRegisterNLoginTask(String mobile) {
            this.mMobile = mobile;
            mRegister = true;
        }

        public UserRegisterNLoginTask() {
            mRegister = false;
            mMobile = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!getKeyStore())
                return false;

            if (!createNewKey(false))
                return false;

            if (!getCipher())
                return false;

            // if everything gone go rite then
            if (mRegister) {
                MyApplication.getInstance().addToSharedPreference(PREFERENCES_KEY_MOB, mMobile);
                updateEncryptionOn(true);
                if (!initCipher(Cipher.ENCRYPT_MODE))
                    return false;// in somthing gone wrong
            } else {
                // login logic
                updateEncryptionOn(false);
                if (!initCipher(Cipher.DECRYPT_MODE))
                    return false;
            }

            if (!iniCryptObject())
                return false;

            return true; // in all come true
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            onCancelled();
            if (!aBoolean) {
            } else {
                startScanFingerPrint();
            }

        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }


    private void startScanFingerPrint() {
        dialogFragment =
                new FingerPrintAuthDialog();
        dialogFragment.setCallbackListener(printCallback);
        dialogFragment.setManagerCompatNCrypto(fingerprintManager, cryptoObject);
        dialogFragment.show(mFragmentManager, DIALOG_FRAGMENT_TAG);
    }

    /*
        for adding configuration in preferences
     */

    public void startDialogOperation(FragmentManager fragmentManager, String dialogTag, int flag) {
        DIALOG_FRAGMENT_TAG = dialogTag;
        mFragmentManager = fragmentManager;
        if (task != null) {
            return;
        }
        switch (flag) {
            case FLAG_REGISTER:
                task = new UserRegisterNLoginTask(getUserMobileNumber());
                task.execute();
                break;
            case FLAG_LOGIN:
                task = new UserRegisterNLoginTask();
                task.execute();
                break;
        }

    }

    void hideDialog() {
        if (dialogFragment != null) {
            if (dialogFragment.isVisible()) dialogFragment.dismiss();
        }
    }

    public void showMaxAttemptDialog(Activity appCompatActivity) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fingerprint_max_limit, null);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button buttonOk = (Button) dialogView.findViewById(R.id.buttonDone);
        buttonOk.setVisibility(View.VISIBLE);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDefaultWayLogin(CLICK_EVENT_OK);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

   /*after user choose no to fingerprint scanner*/

    private void startDefaultWayLogin(int flag) {
        if (nextActivityCallback != null) {
            switch (flag) {
                case CLICK_EVENT_DONT_ASK:
                        /* dont ask me again from configuration dialog*/
                    updateDontAsk();
                    updateFingerPrintDontAskMeEnabled(true);
                    nextActivityCallback.onUserResponse(CLICK_EVENT_DONT_ASK);
                    break;
                case CLICK_EVENT_NO:
                    /* no from configuration dialog*/
                    nextActivityCallback.onUserResponse(CLICK_EVENT_NO);
                    break;
                case CLICK_EVENT_OK:
                       /*after max attempt done*/
                    nextActivityCallback.onUserResponse(CLICK_EVENT_OK);
                    break;
            }

        }
    }

    private void updateDontAsk() {
        String mobileNumber = getUserMobileNumber();
        if (mobileNumber != null) {
            TouchModel touchModel = getUser(mobileNumber);
            if (touchModel != null) {
                touchModel.setDontAskMeAgainOn(true);
                fingerprintStorage.updateUser(touchModel);
            }
        }

    }

    public void showConfigurationDialog(Activity appCompatActivity, final FragmentManager fragmentManager, final int flag) {
        divertToAuthenticateFingerprint(fragmentManager, flag);
      /*  final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fingerprint_configuration, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button buttonYes = (Button) dialogView.findViewById(R.id.yesButton);
        CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.dontAskCheckbox);
        Button buttonNO = (Button) dialogView.findViewById(R.id.noButton);
        //Button buttonDontAsk = (Button) dialogView.findViewById(R.id.dontAskButton);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFingerPrintDontAskMeEnabled(false);
                updateFingerPrintLoginEnabled(false);
                switch (v.getId()) {
                    case R.id.yesButton:
                        divertToAuthenticateFingerprint(fragmentManager, flag);
                        break;
                    case R.id.noButton:
                        startDefaultWayLogin(CLICK_EVENT_NO);
                        break;
//                    case R.id.dontAskButton:
//                        startDefaultWayLogin(CLICK_EVENT_DONT_ASK);
//                        break;
                }
                alertDialog.dismiss();
            }
        };
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startDefaultWayLogin(CLICK_EVENT_DONT_ASK);
                } else {

                }
            }
        });
        buttonYes.setOnClickListener(onClickListener);
        buttonNO.setOnClickListener(onClickListener);
        // buttonDontAsk.setOnClickListener(onClickListener);
        alertDialog.show();*/
    }

    /* after choosing YES diver user to fingerprint auth*/
    private void divertToAuthenticateFingerprint(FragmentManager fragmentManager, int flag) {
        startDialogOperation(fragmentManager, DIALOG_FRAGMENT_TAG, flag);
    }


    private void showNoGuardSettings(Activity appCompatActivity, String message) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fingerprint_max_limit, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();

        TextView TextView = (TextView) dialogView.findViewById(R.id.contain_TextView);
        TextView.setText(message);
        RelativeLayout linearLayout = (RelativeLayout) dialogView.findViewById(R.id.two_btn_layout);
        linearLayout.setVisibility(View.VISIBLE);

        Button buttonOk = (Button) dialogView.findViewById(R.id.buttonDone);
        buttonOk.setVisibility(View.GONE);

        Button buttonOkSetting = (Button) dialogView.findViewById(R.id.OkButton);
        Button buttonCancel = (Button) dialogView.findViewById(R.id.cancelButton);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.OkButton:
                        alertDialog.dismiss();
                        gotoSettings();
                        break;
                    case R.id.cancelButton:
                        alertDialog.dismiss();
                        //TODO fingerprint:=do not allow user to use fingerprint part
                        break;
                }
            }
        };
        buttonCancel.setOnClickListener(onClickListener);
        buttonOkSetting.setOnClickListener(onClickListener);

        alertDialog.show();
    }

    private void gotoSettings() {
        Intent securitySettingIntent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
        context.startActivity(securitySettingIntent);
    }

    public void examineFingerPrintScanner(Activity appCompatActivity) {

        boolean isNeedToShowDialog = true;
      /*  if (appCompatActivity instanceof RegisterMobileNumberActivity) {
            isNeedToShowDialog = false;
        }*/
        // setting value to default first and recheck all flags
        updateUserAllowToUseFingerPrint(false);
        if (isAndroidMorGreter()) {
            if (isFingerPrintCapable()) {
                if (isKeyguardSecurityAlright()) {
                    if (isFingerPrintEnrolled()) {
                        updateUserAllowToUseFingerPrint(true);
                    } else {
                       /* if (isNeedToShowDialog)
                            showNoGuardSettings(appCompatActivity, appCompatActivity.getString(R.string.fingerprint_no_finger_print));*/
                    }
                } else {
                   /* if (isNeedToShowDialog)
                        showNoGuardSettings(appCompatActivity, appCompatActivity.getString(R.string.fingerprint_no_key_guard));*/
                }
            } else {
                //TODO:-fingerprint: handel situation where user has enable fingerprint but current device
                // does not support
            }

        } else {

        }

    }

    /* for calling from Editprofile activity*/
    public void examineFingerPrintScanner(Activity appCompatActivity, boolean showDialog) {
        boolean isNeedToShowDialog = showDialog;
        // setting value to default first and recheck all flags
        updateUserAllowToUseFingerPrint(false);
        if (isAndroidMorGreter()) {
            if (isFingerPrintCapable()) {
                if (isKeyguardSecurityAlright()) {
                    if (isFingerPrintEnrolled()) {
                        updateUserAllowToUseFingerPrint(true);
                    } else {
                        if (isNeedToShowDialog)
                            showNoGuardSettings(appCompatActivity, appCompatActivity.getString(R.string.fingerprint_no_finger_print));
                    }
                } else {
                    if (isNeedToShowDialog)
                        showNoGuardSettings(appCompatActivity, appCompatActivity.getString(R.string.fingerprint_no_key_guard));
                }
            } else {
            }

        } else {

        }

    }


    /* *********************************************** FINGER PRINT SHAERD PREFERENCE**************************************************/

 /*   private String  getFromSharedPreference(String key) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    private void addToSharedPreference(String key, String value) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    private void addBooleanToSharedPreference(String key, boolean value) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value).apply();
    }

    private boolean getBooleanFromSharedPreference(String key) {
        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }*/

    /*
       if fingerPrint added in then only true
     */
    public boolean isUserAllowToUseFingerPrint() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_ALLOW);
    }

    public void updateUserAllowToUseFingerPrint(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_ALLOW, flag);
    }


    // after enabling fingerprint login it will be TRUE else FALSE
    public boolean isFingerPrintLoginEnabled() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_ENABLED);
    }

    // what ever the choise of user update accordingly
    public void updateFingerPrintLoginEnabled(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_ENABLED, flag);
    }

    //this will be used for dont ask me again flag
    public boolean isFingerPrintDontAskMeEnabled() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_DONTASK);
    }

    // what ever the choise of user update accordingly
    public void updateFingerPrintDontAskMeEnabled(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_DONTASK, flag);
    }

    /* to get user mobile number */
    public String getUserMobileNumber() {
        return  MyApplication.getInstance().getFromSharedPreference(PREFERENCES_KEY_MOB);
    }

    /* for update user mobile number */
    public void updateUserMobileNumber(String pin) {
        MyApplication.getInstance().addToSharedPreference(PREFERENCES_KEY_MOB, pin);
    }

    /*  for getting encryption flag*/
    public boolean isTouchEnableInService() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_SERVICE_FLAG);
    }

    /*  for updating encryption flag*/
    public void updateIsTouchEnableInService(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_SERVICE_FLAG, flag);
    }

    /*  for getting encryption flag*/
    public boolean isEncryptionOn() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_ENCRYPTION);
    }

    /*  for updating encryption flag*/
    public void updateEncryptionOn(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_ENCRYPTION, flag);
    }

    /*  for getting fingerPrintKey*/
    public String getFingerPrintKey() {
        return  MyApplication.getInstance().getFromSharedPreference(PREFERENCES_KEY_PASS);
    }

    /*  for updating fingerPrintKey*/
    public void updateFingerPrintKey(String key) {
        MyApplication.getInstance().addToSharedPreference(PREFERENCES_KEY_PASS, key);
    }

    /*  for getting user enabled fingerprint but skin fingerprint login*/
    public boolean isUserWantTraditionalLogin() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_TRADITIONAL_LOGIN);
    }

    /*  for updating skipe fingerprint login flag*/
    public void updateUserWantTraditionalLogin(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_TRADITIONAL_LOGIN, flag);
    }


    // is updating touch enable/disable in editprofile
    public boolean isUpdatingTouchConfiguration() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_UPDATE_TOUCH);
    }

    // update is updating touch enable/disable in editprofile
    public void updateTouchConfiguration(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_UPDATE_TOUCH, flag);
    }

    /*  for getting gettingTouchAction*/
    public String getTouchAction() {
        return  MyApplication.getInstance().getFromSharedPreference(FLAG_USER_FING_UPDATE_T_ACTION);
    }

    /*  for updating Touch Action*/
    public void updateTouchAction(String key) {
        MyApplication.getInstance().addToSharedPreference(FLAG_USER_FING_UPDATE_T_ACTION, key);
    }

    // is updating touch enable/disable in editprofile
    public boolean isUpdatingProfileByTouch() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_UPDATE_T_PROFILE);
    }

    // update is updating touch enable/disable in editprofile
    public void updatingProfileByTouch(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_UPDATE_T_PROFILE, flag);
    }

    /*  for updating Touch Action*/
    public void updateProfileTouchAction(String key) {
        MyApplication.getInstance().addToSharedPreference(FLAG_USER_FING_UPDATE_T_PRO_ACTION, key);
    }

    /*  for getting gettingTouchAction*/
    public String getProfileTouchAction() {
        return  MyApplication.getInstance().getFromSharedPreference(FLAG_USER_FING_UPDATE_T_PRO_ACTION);
    }

    // is touch enable on server
    public boolean isTouchEnableOnServer() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_USER_FING_UPDATE_T_ENABLE);
    }

    // updating touch status on server
    public void updatingTouchEnableOnServer(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_USER_FING_UPDATE_T_ENABLE, flag);
    }

    /* for getting active mobile*/
    public TouchModel getActiveUser() {
        return new Gson().fromJson( MyApplication.getInstance().getFromSharedPreference(FLAG_ACTIVE_USER), TouchModel.class);
    }

    /* for updating active mobile*/
    public void updateActiveUser(TouchModel mobile) {
        MyApplication.getInstance().addToSharedPreference(FLAG_ACTIVE_USER, new Gson().toJson(mobile));
    }

/*
    public void updateSharedPreferenceBaseKey(String userBaseKey, String mobileNumber) {
          */
/* backup data imp for intent extra to pass when user login using fingerprint Auth*//*

        sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userStatus", "loggedIn");
        editor.putString("mobileNumber", mobileNumber);
        if (!userBaseKey.equalsIgnoreCase("") || userBaseKey != "") {
            editor.putString("Auth_Base_Key", userBaseKey);
        }
        editor.apply();
    }
*/


    /* intent extra for redirecting direct to dashboard*/
    public String getIntentExtraResponse() {
        return  MyApplication.getInstance().getFromSharedPreference(FLAG_EXTRA_RESPONSE);
    }

    /* update intent extra for redirecting direct to dashboard*/
    public void updateIntentExtraResponse(String response) {
        MyApplication.getInstance().addToSharedPreference(FLAG_EXTRA_RESPONSE, response);
    }

    /* intent extra for redirecting direct to dashboard*/
    public boolean getIntentExtraEmailVerification() {
        return MyApplication.getInstance().getBooleanFromSharedPreference(FLAG_EXTRA_IS_EMAIL);
    }

    /* update intent extra for redirecting direct to dashboard*/
    public void updateIntentExtraEmailVerification(boolean flag) {
        MyApplication.getInstance().addBooleanToSharedPreference(FLAG_EXTRA_IS_EMAIL, flag);
    }

    /* intent extra for redirecting direct to dashboard*/
    public String getIntentExtraBaseAuthKey() {
        return  MyApplication.getInstance().getFromSharedPreference(FLAG_EXTRA_BASE_AUTH);
    }

    /* update intent extra for redirecting direct to dashboard*/
    public void updateIntentExtraBaseAuthKey(String auth) {
        MyApplication.getInstance().addToSharedPreference(FLAG_EXTRA_BASE_AUTH, auth);
    }
      /* *********************************************** FINGER PRINT SHAERD PREFERENCE END**************************************************/


    public void handleCallback(String pinCode) {
        if (isEncryptionOn()) {
            encryptString(pinCode);
        } else {
            decryptString(pinCode);
        }
    }

    /* from redirecting to home from dialog*/
    public interface NextActivityCallback {
        void onUserResponse(int flag);
    }

    public void showMaxAttemptDialogFromSystem(Activity appCompatActivity, String message) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = appCompatActivity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_fingerprint_max_limit, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        Button buttonOk = (Button) dialogView.findViewById(R.id.buttonDone);
        buttonOk.setVisibility(View.VISIBLE);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDefaultWayLogin(CLICK_EVENT_OK);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
//    /* for updating sharedPreference after validating 4 digit pin
//    shifted from @#ACTIVITY:----VerifyFourDigitPinActivity after successfull redirection to dashboard
//    also called from
//     */
//    public void updateSharedPreference(){
//          /* backup data imp for intent extra to pass when user login using fingerprint Auth*/
//         SharedPreferences   sharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("userStatus", "loggedIn");
//            editor.putString("mobileNumber", getUserMobileNumber());
//            String userBaseKey = getIntentExtraBaseAuthKey();
//            if (!userBaseKey.equalsIgnoreCase("") || userBaseKey != "") {
//                editor.putString("Auth_Base_Key", userBaseKey);
//            }
//            editor.apply();
//        }

    /* saving values usingDatabase*/

    private void updateKey(String mobileNumber, String key) {
        TouchModel model = getTouchModel(mobileNumber);
        if (model != null) {
            model.setTouchSaltValue(key);
        }
    }

    private String getKey(String mobileNumber) {
        TouchModel model = getTouchModel(mobileNumber);
        if (model != null) {
            return model.getTouchSaltValue();
        }
        return "";
    }

    private TouchModel getTouchModel(String propertyToCompare) {
        ArrayList<TouchModel> modelList = fingerprintStorage.getAllRegisterUser();
        int sizeOfList = modelList.size();
        for (int i = 0; i < sizeOfList; i++) {
            TouchModel model = modelList.get(i);
            if (model.get_Id().equalsIgnoreCase(propertyToCompare))
                return model;
            if (model.getMobileNumber().equalsIgnoreCase(propertyToCompare))
                return model;
            if (model.getTouchSaltValue().equalsIgnoreCase(propertyToCompare))
                return model;

        }
        return null;
    }

    private boolean isUserExist(String mobileNumber) {
        ArrayList<TouchModel> arrayList = fingerprintStorage.getAllRegisterUser();
        printDbLog("ArrayList" + arrayList);
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (mobileNumber.equalsIgnoreCase(arrayList.get(i).getMobileNumber())) {
                    return true;
                }
            }
        }
        return false;
    }

    private TouchModel getUserIfExist(String mobileNumber) {
        ArrayList<TouchModel> arrayList = fingerprintStorage.getAllRegisterUser();
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (mobileNumber.equalsIgnoreCase(arrayList.get(i).getMobileNumber())) {
                    return arrayList.get(i);
                }
            }
        }
        return null;
    }

    public void updateAllFlagsForUser() {
        String mobile = getUserMobileNumber();
        ArrayList<TouchModel> arrayList = fingerprintStorage.getAllRegisterUser();
        TouchModel touchModel = null;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (mobile.equalsIgnoreCase(arrayList.get(i).getMobileNumber())) {
                    touchModel = arrayList.get(i);
                }
            }
        }
        // if model is null then it must be new user
        if (touchModel != null) {
            // update sharepreference
            updateFingerPrintDontAskMeEnabled(touchModel.isDontAskMeAgainOn());
            updateFingerPrintLoginEnabled(touchModel.isTouchStatusOnDevice());
            updateUserMobileNumber(touchModel.getMobileNumber());
            updateFingerPrintKey(touchModel.getTouchSaltValue());
        } else {
            // reset all shared preference
            updateFingerPrintDontAskMeEnabled(false);
            updateFingerPrintLoginEnabled(false);
        }

    }

    private void updateOrAddInDb(String key) {
        String userMobileNumber = getUserMobileNumber();
        if (isUserExist(userMobileNumber)) {
            updateUser(userMobileNumber, key);
        } else addUser(userMobileNumber, key);
    }

    public TouchModel getUser(String mobile) {
        return getUserIfExist(mobile);
    }

    private void addUser(String mobile, String key) {
        TouchModel model = new TouchModel();
        model.setTouchSaltValue(key);
        model.setUserActive(true);
        model.setDateOfAdded(new Date().toString());
        model.setTouchStatusOnServer(true);
        model.setTouchStatusOnDevice(true);
        model.setMobileNumber(mobile);
        fingerprintStorage.addUser(model);
        updateOperations(mobile, key);
        updateActiveUser(model);
    }

    private void updateUser(String mobile, String key) {
        TouchModel model = getUser(mobile);
        model.setTouchSaltValue(key);
        model.setUserActive(true);
        model.setDateOfAdded(new Date().toString());
        model.setTouchStatusOnServer(true);
        model.setTouchStatusOnDevice(true);
        model.setMobileNumber(mobile);
        fingerprintStorage.updateUser(model);
        updateActiveUser(model);
        updateOperations(mobile, key);
    }

    private void updateOperations(String mobile, String key) {
        updateFingerPrintKey(key);
        updateUserMobileNumber(mobile);

    }

    public void disableTouchStatus() {
        TouchModel touchModel = getUser(getUserMobileNumber());
        if (touchModel != null) {
            touchModel.setTouchStatusOnDevice(false);
            touchModel.setTouchStatusOnServer(false);
            touchModel.setDontAskMeAgainOn(false);
            fingerprintStorage.updateUser(touchModel);
        }
    }

    private boolean isLoginEnable() {
        String mobile = getUserMobileNumber();
        if (mobile != null) {
            TouchModel touchModel = getUser(mobile);
            if (touchModel != null) {
                if (touchModel.getTouchSaltValue() != null && !touchModel.getTouchSaltValue().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
