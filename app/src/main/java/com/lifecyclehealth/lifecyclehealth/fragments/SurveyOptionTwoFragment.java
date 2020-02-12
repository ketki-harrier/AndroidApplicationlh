package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomViewPager;
import com.lifecyclehealth.lifecyclehealth.adapters.SurveyPagerAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.utils.VerticalSeekBar;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyOptionTwoFragment extends BaseFragmentWithOptions {

    private static final String SURVEY_EXTRAS_TWO_TYPE = "type_two_survey_extras_data";
    private SurveyDetailsModel surveyDetailsModel;
    MainActivity mainActivity;
    LinearLayout linear_deliminater;
    RelativeLayout vertical_bar, horizontal_bar;
    TextView vertical_start_value, vertical_end_value, horizontal_start_value, horizontal_end_value;
    VerticalSeekBar seekBarVertical;
    SeekBar seek_bar_horizontal;
    ArrayList<Integer> arrayMinProgress = new ArrayList<>();
    ArrayList<Integer> arrayMaxProgress = new ArrayList<>();
    String new_widget, widget_name;
    String widget_size;
    Button prev,next;
    SurveyPagerAdapter surveyPager ;
    View mVideoLayout;
    private static int pagePosition;

    public static SurveyOptionTwoFragment newInstance(Serializable data, int position) {
        SurveyOptionTwoFragment oneFragment = new SurveyOptionTwoFragment();
        Bundle bundle = new Bundle();
        pagePosition = position;
       // bundle.putString(SURVEY_EXTRAS_TWO_TYPE, data);
        bundle.putSerializable(SURVEY_EXTRAS_TWO_TYPE, data);
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
        View rootView = inflater.inflate(R.layout.fragment_survey_option_two, container,         false);

        return rootView;
       // return inflater.inflate(R.layout.fragment_survey_option_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        surveyDetailsModel = new Gson().fromJson(getArguments().getString(SURVEY_EXTRAS_TWO_TYPE), SurveyDetailsModel.class);
        setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupView(View view) {
        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        //String text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
        String text;
        if (surveyDetailsModel.getQuestionModel().isRequired()) {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "" + " </font>" + " <font color=#ffcc00>*</font>";
        } else {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "";
        }
        TextView TextViewForName = (TextView) view.findViewById(R.id.surveyForName);
        if (MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT)) {
            TextViewForName.setVisibility(View.GONE);
        } else {
            TextViewForName.setVisibility(View.VISIBLE);
            TextViewForName.setText("Survey related to patient:" + surveyDetailsModel.getFirstName() + " " + surveyDetailsModel.getLastName());
        }

        TextViewQuestion.setText(Html.fromHtml(text));
        TextViewName.setText(surveyDetailsModel.getName());
        seek_bar_horizontal = (SeekBar) view.findViewById(R.id.seek_bar_horizontal);
        vertical_start_value = (TextView) view.findViewById(R.id.vertical_start_value);
        vertical_end_value = (TextView) view.findViewById(R.id.vertical_end_value);
        horizontal_start_value = (TextView) view.findViewById(R.id.horizontal_start_value);
        horizontal_end_value = (TextView) view.findViewById(R.id.horizontal_end_value);
        //vertical_progress_value = (TextView) view.findViewById(R.id.vertical_progress_value);
        //horizontal_progress_value = (TextView) view.findViewById(R.id.horizontal_progress_value);
        seekBarVertical = (VerticalSeekBar) view.findViewById(R.id.seekBarVertical);
        vertical_bar = (RelativeLayout) view.findViewById(R.id.vertical_bar);
        horizontal_bar = (RelativeLayout) view.findViewById(R.id.horizontal_bar);
        linear_deliminater = (LinearLayout) view.findViewById(R.id.linear_deliminater);
        if (SurveyDetailsListFragment.isToDo) {
        } else {
            seek_bar_horizontal.setEnabled(false);
            seek_bar_horizontal.setClickable(false);
            seek_bar_horizontal.setFocusable(false);
            seekBarVertical.setClickable(false);
            seekBarVertical.setFocusable(false);
            seekBarVertical.setEnabled(false);
        }

        // widget_size = String.valueOf(surveyDetailsModel.getQuestionModel().getQuestionOptions().size());
        widget_name = surveyDetailsModel.getQuestionModel().getWidget_Name();
        new_widget = surveyDetailsModel.getQuestionModel().getWidget_Type();

        try {
            if (widget_name.equals("10cm Visual Analogue Scale Horizontal") || widget_name.equals("VAS Pain Slider With Faces")) {
                //    if (new_widget.equals("10cm Visual Analogue Scale")) {

                for (int i = 0; i < surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); i++) {
                    arrayMaxProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_To());
                    arrayMinProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_From());
                }
                if (surveyDetailsModel.getQuestionModel().getWidget_Orientation().trim().equals("Vertical")) {
                    editVerticalSeekBar();
                } else {
                    editHorizontalSeekBar();
                }
            } else if (new_widget.equals("10cm Visual Analogue Scale") || new_widget.equals("VAS Pain Slider With Faces")) {
                //    if (new_widget.equals("10cm Visual Analogue Scale")) {

                for (int i = 0; i < surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); i++) {
                    arrayMaxProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_To());
                    arrayMinProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_From());
                }
                if (surveyDetailsModel.getQuestionModel().getWidget_Orientation().trim().equals("Vertical")) {
                    editVerticalSeekBar();
                } else {
                    editHorizontalSeekBar();
                }
            } else if (new_widget.equals("VAS Pain Slider With Faces")) {
                for (int i = 0; i < surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); i++) {
                    arrayMaxProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_To());
                    arrayMinProgress.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getQuestion_Option_Value_From());
                }

                if (surveyDetailsModel.getQuestionModel().getWidget_Orientation().trim().equals("Vertical")) {
                    editVerticalSeekBar();
                } else {
                    editHorizontalSeekBar();
                }
            } else if (surveyDetailsModel.getQuestionModel().getWidget_Type().trim().equals("Deliminater And Boxes")) {
                createViewDeliminator(view);
            } else {
                Toast.makeText(mainActivity, "error widget", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){e.printStackTrace();}

    }


    private void createViewDeliminator(View view) {
        horizontal_bar.setVisibility(View.GONE);
        vertical_bar.setVisibility(View.GONE);
        linear_deliminater.setVisibility(View.VISIBLE);
        addEditTexts();
    }


    private void addEditTexts() {
        int number = 0, before = 0;
        if (surveyDetailsModel.getQuestionModel().getBox_After_Deliminater() != null) {
            number = Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_After_Deliminater());
        }
        if (surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater() != null) {
            number = number + Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater());
            before = Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater());
        }


        for (int i = 1; i <= number; i++) {
            // String optionsToadd = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getName();


            if (i == before) {
                final TextView textView = new TextView(mainActivity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 5, 5, 5);
                textView.setPadding(10, 10, 10, 10);
                textView.setLayoutParams(layoutParams);

                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
                textView.setTypeface(font);
                textView.setText(".");
                linear_deliminater.addView(textView);
            }


            final EditText editText = new EditText(mainActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 5, 5);
            layoutParams.weight = 1.0f;
            editText.setPadding(10, 10, 10, 10);
            editText.setLayoutParams(layoutParams);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
            editText.setTypeface(font);
            //editText.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
            editText.setText("1");
            //editText.setText(optionsToadd);
            if (SurveyDetailsListFragment.isToDo) {
                editText.setFocusable(true);
                editText.setClickable(true);
                editText.setCursorVisible(true);
            } else {
                editText.setFocusable(false);
                editText.setClickable(false);
                editText.setCursorVisible(false);
            }

            final boolean isRequiredEditText = surveyDetailsModel.getQuestionModel().isRequired();
            if (isRequiredEditText) {
                if (!surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().trim().equals("")) {
                    editText.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer());
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                } else {
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                }
            }


            linear_deliminater.addView(editText);
        }
    }

    private void editVerticalSeekBar() {

        horizontal_bar.setVisibility(View.GONE);
        linear_deliminater.setVisibility(View.GONE);
        vertical_bar.setVisibility(View.VISIBLE);

        seekBarVertical.setMax(surveyDetailsModel.getQuestionModel().getMax_Value());
        vertical_start_value.setText(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(0).getName());
        int size = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
        vertical_end_value.setText(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(size - 1).getName());
        if (surveyDetailsModel.getQuestionModel().getScoreValue() != null) {
            int score = Integer.parseInt(surveyDetailsModel.getQuestionModel().getScoreValue());
            seekBarVertical.setProgress(score);
            //vertical_progress_value.setText(getProgressName(score));
        } else {
            seekBarVertical.setProgress(0);
            //ertical_progress_value.setText(getProgressName(0));
        }
        seekBarVertical.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                printLog("progress" + progress);
                String name = getProgressName(progress);
                //vertical_progress_value.setText(name);
                if (SurveyDetailsListFragment.isToDo) {
                    submitSurveyAnswer(progress + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void editHorizontalSeekBar() {
        vertical_bar.setVisibility(View.GONE);
        horizontal_bar.setVisibility(View.VISIBLE);

        seek_bar_horizontal.setMax(surveyDetailsModel.getQuestionModel().getMax_Value());
        horizontal_end_value.setText(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(0).getName());
        int size = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
        horizontal_start_value.setText(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(size - 1).getName());
        if (surveyDetailsModel.getQuestionModel().getScoreValue() != null) {
            int score = Integer.parseInt(surveyDetailsModel.getQuestionModel().getScoreValue());
            seek_bar_horizontal.setProgress(score);
            //horizontal_progress_value.setText(getProgressName(score));
        } else {
            seek_bar_horizontal.setProgress(0);
            // horizontal_progress_value.setText(getProgressName(0));
        }
        seek_bar_horizontal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // horizontal_progress_value.setText(getProgressName(progress));
                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                SurveyDetailsItemFragment.scrollViewPager();
                if (SurveyDetailsListFragment.isToDo) {
                    submitSurveyAnswer(progress + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });
    }

    public String getProgressName(int progressValue) {

        for (int i = 0; i < arrayMaxProgress.size(); i++) {
            if (arrayMinProgress.get(i) <= progressValue && progressValue <= arrayMaxProgress.get(i)) {
                String name = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i).getName();
                return name;
            }
        }

        return "";
    }


    private void submitSurveyAnswer(String request) {
        if (isConnectedToNetwork(mainActivity)) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                params.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                params.put("OptionId", "");
                params.put("Score", request);
                params.put("Type_Of_Section", "2");
                params.put("Descriptive_Answer", "");

                HashMap<String, HashMap<String, String>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            printLog("Response:survey submit" + response);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        } else {
            showNoNetworkMessage();
        }
    }

}
