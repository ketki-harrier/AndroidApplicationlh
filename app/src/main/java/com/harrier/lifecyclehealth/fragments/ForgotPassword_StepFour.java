package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.ForgotPasswordHolderActivity;
import com.harrier.lifecyclehealth.application.MyApplication;

import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_STEP_FOUR;
import static com.harrier.lifecyclehealth.utils.AppConstants.PREF_FORGOT_PASS_CURRENT_STEP;

/**
 * Created by satyam on 24/04/2017.
 */

public class ForgotPassword_StepFour extends BaseFragmentWithOptions {
    public static final String EXTRA_FRAGMENT_FOUR = "step_four";
    private ForgotPasswordHolderActivity holderActivity;
    private TextView textViewcountDown;


    public static ForgotPassword_StepFour newInstance() {
        return new ForgotPassword_StepFour();
    }

    @Override
    String getFragmentTag() {
        return "ForgotPassword_StepFour";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        holderActivity = (ForgotPasswordHolderActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_forgot_pass_step_four, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbarTitle(holderActivity.getToolbar(), getString(R.string.forgot_pass_title_step_four));
        setView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addIntToSharedPreference(PREF_FORGOT_PASS_CURRENT_STEP, CONST_STEP_FOUR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setView(View view) {
        setCountDown();
        countDownTimer.start();
        textViewcountDown = (TextView) view.findViewById(R.id.instructionRedirectTv);
        Button buttonSignIn = (Button) view.findViewById(R.id.logIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                holderActivity.handelBackPressed();
            }
        });

    }

    private CountDownTimer countDownTimer;

    private void setCountDown() {
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewcountDown.setText(getString(R.string.step_four_instruction_redirect_prefix) + "  " + millisUntilFinished / 1000 + " " + getString(R.string.step_four_instruction_redirect_suffix));
            }

            public void onFinish() {
                holderActivity.handelBackPressed();
            }
        };
    }

}
