package com.harrier.lifecyclehealth.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.application.MyApplication;
import com.harrier.lifecyclehealth.fragments.ForgotPassword_StepFour;
import com.harrier.lifecyclehealth.fragments.ForgotPassword_StepOne;
import com.harrier.lifecyclehealth.fragments.ForgotPassword_StepThree;
import com.harrier.lifecyclehealth.fragments.ForgotPassword_StepTwo;
import com.harrier.lifecyclehealth.utils.KeyBoardHandler;
import com.harrier.lifecyclehealth.utils.NetworkRequestUtil;

import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_FOUR;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_ONE;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_RESET;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_THREE;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_TWO;
import static com.harrier.lifecyclehealth.utils.AppConstants.EXTRA_FORGOT_PASS_USERNAME;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_FORGOT_PASS_CURRENT_STEP;

/**
 * Created by satyam on 24/04/2017.
 */

public class ForgotPasswordHolderActivity extends BaseActivity {
    private Toolbar toolbar;
    public NetworkRequestUtil networkRequestUtil;
    public String userId;
    private KeyBoardHandler keyBoardHandler;
    private RelativeLayout rootLayout;

    @Override
    String getTag() {
        return "ForgotPasswordHolderActivity";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        networkRequestUtil = new NetworkRequestUtil(this);
        keyBoardHandler = new KeyBoardHandler(this, rootLayout);
        userId = getIntent().getStringExtra(EXTRA_FORGOT_PASS_USERNAME);
        setupToolbar();
        replaceFragment(new ForgotPassword_StepOne());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        handelBackPressed();
        printLog("OnBackpressCalled");

    }

    public void handelBackPressed() {
        switch (MyApplication.getInstance().getIntFromSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP)) {
            case CONST_STEP_RESET:
                finishActivity();
                break;
            case CONST_STEP_ONE:
                finishActivity();
                break;
            case CONST_STEP_TWO:
                replaceFragment(ForgotPassword_StepOne.newInstance());
                break;
            case CONST_STEP_THREE:
                showWarningDialog();
                break;
            case CONST_STEP_FOUR:
                finishActivity();
                break;
        }
    }


    public void finishActivity() {
        MyApplication.getInstance().addIntToSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP, CONST_STEP_RESET);
        finish();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

    }

    /* for changing fragments*/
    protected void replaceFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    /* For step 2*/
    public void proceedToStepTwo() {
        keyBoardHandler.hideKeyboard();
        replaceFragment(ForgotPassword_StepTwo.newInstance());
    }

    /* for step 3*/
    public void proceedToStepTHREE(String verificationCode, String userId) {
        keyBoardHandler.hideKeyboard(true);
        replaceFragment(ForgotPassword_StepThree.newInstance(verificationCode, userId));
    }

    /* For step4*/
    public void proceedToStepFour() {
        replaceFragment(ForgotPassword_StepFour.newInstance());
    }

    /* this will call from back button press as well as cancel button*/
    private void showWarningDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ForgotPasswordHolderActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_two_btn, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView textViewContaint = (TextView) dialogView.findViewById(R.id.dialogContainTv);
        textViewContaint.setText(getString(R.string.step_four_cancel_message_contain));
        Button buttonCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        buttonCancel.setText(getString(R.string.step_four_cancel_message_btn_No));
        Button buttonSignOut = (Button) dialogView.findViewById(R.id.btnSignOut);
        buttonSignOut.setText(getString(R.string.step_four_cancel_message_btn_back));

        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finishActivity();
            }
        });
        alertDialog.show();
    }
}
