package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.db.LifecycleDatabase;
import com.lifecyclehealth.lifecyclehealth.dto.AuthenticateUserDTO;
import com.lifecyclehealth.lifecyclehealth.model.CareGiverUsersList;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;

import org.json.JSONObject;

import java.util.HashMap;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_NAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_Access_Token;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_ORG_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.Moxtra_uniqueId;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_GET_CAREGIVERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_LOGIN_AS_CAREGIVERNAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_LOGIN_AS_SELF;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.USER_TOKEN;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class CareGiverNew extends BaseActivity implements View.OnClickListener {
    NetworkRequestUtil networkRequestUtil;
    private CareGiverUsersList careGiverUsersList;
    //public ColorCode colorCode;
    static int selectedUser = 0;
    int login = 0;
    LifecycleDatabase lifecycleDatabase;
    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;
    Button btnContinue;
    // RadioGroup ll;
    String Stringcode = "42b039";
    ViewGroup v;
    AppCompatRadioButton rdbtn;

    @Override
    String getTag() {
        return "CareGiverActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_giver);
        stopService(new Intent(this, BackgroundTrackingService.class));
        start();
        initView();
    }

    private void initView() {
      //  try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            if (resposne != null) {
                ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);
                String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
                String Stringcode = "";
                String hashcode = "";

                if (demo == null) {
                    hashcode = "Green";
                    Stringcode = "259b24";
                } else if (demo != null) {
                    String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                    hashcode = arr[0].trim();
                    Stringcode = arr[1].trim();
                /*}
               else*/
                    if (hashcode.equals("Black") && Stringcode.length() < 6) {
                        Stringcode = "#333333";
                    }
                }
            }


            lifecycleDatabase = new LifecycleDatabase(getApplicationContext());
            // ll = (RadioGroup) findViewById(R.id.radioGroup);
            v = (ViewGroup) findViewById(R.id.radioGroup);

            btnContinue = (Button) findViewById(R.id.btnContinue);
            Button btnCancel = (Button) findViewById(R.id.btnCancel);
            TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setText("Login As");

            mDelegater = NotificationDelegater.getInstance();
            mLocal = mDelegater.local();
            mLocal.setView((NotificationView) findViewById(R.id.nv));
            btnContinue.setBackgroundColor(Color.parseColor("#42b039" /*+ Stringcode*/));
            btnContinue.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
        //getColorCode();
        checkUserName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue: {
                careGiverLogin();
                break;
            }
            case R.id.btnCancel: {
                divertToLoginScreen();
                break;
            }
        }
    }

    public void checkUserName() {
        showProgressDialog(true);
        if (isConnectedToNetwork(this)) {
            networkRequestUtil.getDataSecure(BASE_URL + URL_GET_CAREGIVERNAME, new VolleyCallback() {

                @Override
                public void onSuccess(JSONObject response) {
                    String testString = String.valueOf(response);

                    careGiverUsersList = new Gson().fromJson(testString, CareGiverUsersList.class);

                    //     setNameList();
                    //       String newone = String.valueOf(careGiverUsersList.getStatus().equalsIgnoreCase(STATUS_SUCCESS));
                    showProgressDialog(false);
                    printLog("ResponseCareGiverData" + response);
                    //  careGiverUsersList = new Gson().fromJson(response.toString(), CareGiverUsersList.class);
                    if (careGiverUsersList != null) {
                        if (careGiverUsersList.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                            setNameList();
                        } /*else showDialogWithOkButton(questionDto.getMessage());*/

                    } else {
                        showDialogWithOkButton(getString(R.string.error_someting_went_wrong));

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    showProgressDialog(false);
                }
            });
        } else

        {
        showProgressDialog(false);
        showDialogWithOkButton(getString(R.string.error_no_network));

    }
}

    private void careGiverLogin() {
        showProgressDialog(true);
        try {

            HashMap<String, CareGiverUsersList.PatientList> hashMap = new HashMap<>();
            final JSONObject requestJson;
            String url;
            if (selectedUser == 0) {
                url = BASE_URL + URL_LOGIN_AS_SELF;
                requestJson = null;
            } else {
                CareGiverUsersList.PatientList patientList = careGiverUsersList.setPatient_Details(careGiverUsersList.getPatientList().get(selectedUser - 1));
                hashMap.put("Patient_Details", patientList);
                url = BASE_URL + URL_LOGIN_AS_CAREGIVERNAME;
                requestJson = new JSONObject(new Gson().toJson(hashMap));
            }

            if (isConnectedToNetwork(this)) {
                networkRequestUtil.putDataSecure(url, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response:CheckUserCareGiver" + response);
                        AuthenticateUserDTO userDTO = new Gson().fromJson(response.toString(), AuthenticateUserDTO.class);
                        if (userDTO != null) {
                            if (userDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                proceedToNextStep(userDTO);

                            } /*else showDialogWithOkButton(userDTO.getMessage());*/
                        } else {
                            showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } else {
                showProgressDialog(false);
                showDialogWithOkButton(getString(R.string.error_no_network));
            }
        } catch (Exception e) {

        }
    }

    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{Color.parseColor("#42b039" /*+ Stringcode*/)}
        );
    }

    private void setNameList() {
       // try {

            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            if (resposne != null) {
                ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);
                String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
                String Stringcode = "";
                String hashcode = "";

                if (demo == null) {
                    hashcode = "Green";
                    Stringcode = "259b24";
                } else if (demo != null) {
                    String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                    hashcode = arr[0].trim();
                    Stringcode = arr[1].trim();

                    btnContinue.setBackgroundColor(Color.parseColor("#42b039" /*+ Stringcode*/));
                    if (hashcode.equals("Black") && Stringcode.length() < 6) {
                        Stringcode = "#333333";
                    }
                }
            }
       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
        // addColor();

        final int number = careGiverUsersList.getPatientList().size();

        //ll = new RadioGroup(CareGiverActivity.this);
        // ll.setOrientation(LinearLayout.VERTICAL);

        for (int row = 0; row <= number; row++) {

            rdbtn = new AppCompatRadioButton(CareGiverNew.this);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 0, 5);
            rdbtn.setLayoutParams(layoutParams);
            //rdbtn.setTextSize(getResources().getDimension(R.dimen.size_9dp));
            // rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
         //   rdbtn.setSupportButtonTintList(getColorList());
            rdbtn.setTextColor(ContextCompat.getColor(this, R.color.white));
            rdbtn.setHighlightColor(ContextCompat.getColor(CareGiverNew.this, R.color.colorPrimary));
            rdbtn.setButtonDrawable(R.drawable.radio_check_box);
            //       v.addView(rdbtn);


            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
            rdbtn.setTypeface(font);
            // rdbtn.setTextAppearance(getApplicationContext(), R.style.Theme_AppCompat.);
            if (row == 0) {
                rdbtn.setId(row);
                rdbtn.setChecked(true);
                //rdbtn.setText(MyApplication.getInstance().getFromSharedPreference(LOGIN_NAME));
                rdbtn.setText("Self Account");
            } else {
                rdbtn.setId(row);
                rdbtn.setChecked(false);
                rdbtn.setText("Caregiver for " + careGiverUsersList.getPatientList().get(row - 1).getPatient_FullName());
            }

            rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    printLog("ButtonView:" + buttonView.getId() + "||" + isChecked);
                    if (isChecked) {
                        if (buttonView.getText().equals("Self Account")) {
                            login = 0;
                        } else {
                            login = 1;
                        }
                        selectedUser = buttonView.getId();
                    }
                }
            });

            v.addView(rdbtn);
            //((ViewGroup) findViewById(R.id.radioGroup)).addView(rdbtn);
        }
        //v.addView(rdbtn);

    }

    private void proceedToNextStep(AuthenticateUserDTO user) {
        try {
            MyApplication.getInstance().updateCurrentUser(user);
            String LOGIN_ID_ENCRY = AESHelper.encrypt(seedValue, user.getUser().getPatientId());
            String LOGIN_NAME_ENCRY = AESHelper.encrypt(seedValue, user.getUser().getName());
            MyApplication.getInstance().addToSharedPreference(LOGIN_ID, LOGIN_ID_ENCRY);
            MyApplication.getInstance().addToSharedPreference(LOGIN_NAME, LOGIN_NAME_ENCRY);

            MyApplication.getInstance().addToSharedPreference(USER_TOKEN, user.getToken());
            MyApplication.getInstance().addToSharedPreference(Moxtra_ORG_ID, user.getUser().getMoxtraOrgid());
            MyApplication.getInstance().addToSharedPreference(Moxtra_uniqueId, user.getUser().getMoxtraUniqueID());
            MyApplication.getInstance().addToSharedPreference(Moxtra_Access_Token, user.getUser().getMoxtraAccessToken());

        } catch (Exception e) {
        }
        if (login == 1) {
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOGIN_NAMECAREGIVER, true);
        } else {
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.LOGIN_NAMECAREGIVER, false);
        }

        lifecycleDatabase.deleteData();
        lifecycleDatabase.addData(user.getToken());
        printLog("User:" + user);


        if (user.getUser().getRole().size() > 0) {
            for (int i = 0; i < user.getUser().getRole().size(); i++) {
                if (user.getUser().getRole().get(i).equals("Provider")) {
                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, true);
                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, false);
                }
                if (user.getUser().getRole().get(i).equals("Patient")) {
                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.PREF_IS_PATIENT, true);
                    MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.Is_Care_Giver, false);
                }
            }
        }
        divertToHomeScreen();
    }

    /* For diverting to homeScreen*/
    private void divertToHomeScreen() {
        //startActivity(new Intent(CareGiverActivity.this, MainActivity.class));
        Intent intent = new Intent(CareGiverNew.this, MainActivity.class);
        intent.putExtra("from_notification", "0");
        startActivity(intent);
        finish();
    }

    private void divertToLoginScreen() {
        startActivity(new Intent(CareGiverNew.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        printLog("touch onUserInteraction");
        TOUCH_TIME = System.currentTimeMillis();
        stop();
        restart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BaseActivity.active=false;
        stop();
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(CareGiverNew.this, LoginActivity.class);
            startActivity(i);
        }
    };
    private Handler myHandler;
    private void start() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
        stopService(new Intent(this, BackgroundTrackingService.class));
    }

    private void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    private void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }
}
