package com.harrier.lifecyclehealth.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;

/*
    This fragment will represent the option 1=EditText type of questions.

 */
public class SurveyOptionOneFragment extends BaseFragmentWithOptions {
    private static final String SURVEY_EXTRAS_ONE_TYPE = "type_one_survey_extras_data";
    private MainActivity mainActivity;

    public static SurveyOptionOneFragment newInstance(String data) {
        SurveyOptionOneFragment oneFragment = new SurveyOptionOneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_ONE_TYPE, data);
        oneFragment.setArguments(bundle);
        return oneFragment;
    }


    @Override
    String getFragmentTag() {
        return "SurveyOptionOneFragment";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey_option_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
