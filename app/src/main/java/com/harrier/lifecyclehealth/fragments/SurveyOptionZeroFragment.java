package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;
import com.harrier.lifecyclehealth.model.SurveyDetailsModel;

/*
 This fragment will represent the option 0=RADIO BUTTON type of questions.
 */
public class SurveyOptionZeroFragment extends BaseFragmentWithOptions {
    private static final String SURVEY_EXTRAS_ZERO_TYPE = "type_zero_survey_extras_data";
    private SurveyDetailsModel surveyDetailsModel;
    MainActivity mainActivity;

    public static SurveyOptionZeroFragment newInstance(String data) {
        SurveyOptionZeroFragment zeroFragment = new SurveyOptionZeroFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_ZERO_TYPE, data);
        zeroFragment.setArguments(bundle);
        return zeroFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveyOptionZeroFragment";
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
        return inflater.inflate(R.layout.fragment_survey_option_zero, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        surveyDetailsModel = new Gson().fromJson(getArguments().getString(SURVEY_EXTRAS_ZERO_TYPE), SurveyDetailsModel.class);
        setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupView(View view) {
        TextView textViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView textViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        String text = "<font color=#cc0029>" + surveyDetailsModel.getQuestionModel().getDescription() +"?"+ " </font>" + " <font color=#ffcc00>*</font>";
        textViewQuestion.setText(Html.fromHtml(text));
        textViewName.setText(surveyDetailsModel.getName());
        addRadioButtons();
    }

    private void addRadioButtons() {
        int number = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();

        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(mainActivity);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i <= number; i++) {
                String optionsToadd = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getName();

                AppCompatRadioButton rdbtn = new AppCompatRadioButton(mainActivity);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 5, 0, 5);
                rdbtn.setLayoutParams(layoutParams);
                rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                rdbtn.setSupportButtonTintList(getColorList());
                rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                rdbtn.setId((row * 2) + i);
                rdbtn.setText(optionsToadd);
//
//                if (optionsToadd.equalsIgnoreCase(selectedOption))
//                    rdbtn.setSelected(true);

                rdbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        selectedAns = getSelectedOption(view);
                    }
                });
                ((ViewGroup) getView().findViewById(R.id.radioGroup)).addView(rdbtn);
            }
        }
    }

    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(mainActivity, R.color.colorPrimary)}
        );
    }
}
