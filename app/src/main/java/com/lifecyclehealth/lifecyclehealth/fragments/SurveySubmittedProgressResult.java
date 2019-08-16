package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.SurveyElectronicSubmitResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.OnSwipeTouchListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveySubmittedProgressResult extends BaseFragmentWithOptions {


    MainActivity mainActivity;
    SurveyElectronicSubmitResponse.SubmittedScoreData scoreData;
    private static final String SURVEY_LIST_EXTRAS_HOLDER = "submittedScoreData";
    private static final String SURVEY_ID = "survey_id";
    private ColorCode colorCode;
    String Stringcode;

    public static SurveySubmittedProgressResult newInstance(SurveyElectronicSubmitResponse.SubmittedScoreData submittedScoreData) {

        SurveySubmittedProgressResult holderFragment = new SurveySubmittedProgressResult();
        Bundle bundle = new Bundle();
        String data = new Gson().toJson(submittedScoreData);
        bundle.putString(SURVEY_LIST_EXTRAS_HOLDER, data);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveySubmittedProgressResult";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_submitted_progress_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scoreData = new Gson().fromJson(getArguments().getString(SURVEY_LIST_EXTRAS_HOLDER), SurveyElectronicSubmitResponse.SubmittedScoreData.class);

        view.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
               // Toast.makeText(getContext(),"Left swipe",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                fm.popBackStack("SurveyDetailsListFragment", 0);
            }
        });

        initView(view);
    }

    private void initView(View view) {
        //try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcode = "";
            String hashcode = "";

            if(demo == null){
                hashcode = "Green";
                Stringcode = "259b24";
            }
            else if(demo !=null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
           /* }
            else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
       // }catch (Exception e){e.printStackTrace();}
        Analytics.with(getContext()).screen("ScoreOnSurveySubmit");
        CircularProgressBar mProgress = (CircularProgressBar) view.findViewById(R.id.progressBar1);
        mProgress.setColor(Color.parseColor("#"+Stringcode));
        TextView txtPrecentProgress = (TextView) view.findViewById(R.id.txtPrecentProgress);
        txtPrecentProgress.setTextColor(Color.parseColor("#"+Stringcode));
        TextView tx_score_details = (TextView) view.findViewById(R.id.tx_score_details);
        txtPrecentProgress.setText(scoreData.getPatient_total_survey_score());
        tx_score_details.setText("Submitted and e-signed by " + scoreData.getUser() + " on " + scoreData.getSubmission_datetime() + ".");
        int score = 0;
        if (scoreData.getPatient_total_survey_score() != null) {
            Float a = Float.parseFloat(scoreData.getPatient_total_survey_score());
            score = (int) Math.round(a);
            mProgress.setProgress(score);
        }
        Analytics.with(getContext()).track("Score On Survey Submit", new Properties().putValue("category", "Mobile").putValue("score",score+""));
        TextView back = (TextView) view.findViewById(R.id.back);

        back.setTextColor(Color.parseColor("#"+Stringcode));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    backPage();
                    return true;
                }
                return false;
            }
        });

    }

    private void backPage() {
        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        //fm.popBackStack("SurveyDetailsListFragment", 0);
        fm.popBackStack("com.lifecyclehealth.lifecyclehealth.fragments.SurveyFragment", 0);
        // mainActivity.backFromSurveyDetailsView();
    }
}
