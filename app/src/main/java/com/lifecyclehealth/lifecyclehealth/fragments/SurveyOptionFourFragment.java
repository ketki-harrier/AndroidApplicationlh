package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.utils.VerticalSeekBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyOptionFourFragment extends BaseFragmentWithOptions {


    private static final String SURVEY_EXTRAS_FOUR_TYPE = "type_four_survey_extras_data";
    private SurveyDetailsModel surveyDetailsModel;
    MainActivity mainActivity;
    LinearLayout linear_deliminater;
    Map<Integer, String> dataArray = new HashMap<>();
    ScrollView mainLinear;
    static int progress = 0;

    public static SurveyOptionFourFragment newInstance(String data, int position) {
        SurveyOptionFourFragment oneFragment = new SurveyOptionFourFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_FOUR_TYPE, data);
        oneFragment.setArguments(bundle);
        return oneFragment;
    }


    @Override
    String getFragmentTag() {
        return "SurveyOptionFourFragment";
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
        return inflater.inflate(R.layout.fragment_survey_option_four, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager mProfilesViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        surveyDetailsModel = new Gson().fromJson(getArguments().getString(SURVEY_EXTRAS_FOUR_TYPE), SurveyDetailsModel.class);
        setupView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupView(View view) {
        mainLinear = (ScrollView) view.findViewById(R.id.mainLinear);
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

        linear_deliminater = (LinearLayout) view.findViewById(R.id.linear_deliminater);
        createViewDeliminator(view);
    }


    private void createViewDeliminator(View view) {
        linear_deliminater.setVisibility(View.VISIBLE);
        addEditTexts();
    }

    int number = 0, before = 0;

    private void addEditTexts() {
        progress = 0;
        int maxLengthofEditText = 1;
        if (surveyDetailsModel.getQuestionModel().getBox_After_Deliminater() != null) {
            number = Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_After_Deliminater());
        }
        if (surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater() != null) {
            number = number + Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater());
            before = Integer.parseInt(surveyDetailsModel.getQuestionModel().getBox_Before_Deliminater());
        }

        for (int position = 1; position <= number; position++) {

            final EditText editText = new EditText(mainActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT);
            int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

            layoutParams.setMargins(0, 5, 5, 5);
            layoutParams.weight = 1.0f;
            editText.setPadding(5, 5, 5, 5);
            editText.setLayoutParams(layoutParams);
            editText.setMinimumHeight(minHeight);
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
            editText.setTypeface(font);
            editText.setGravity(Gravity.CENTER);
            //editText.setBackground(gd);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                editText.setBackground(getResources().getDrawable(R.drawable.bg_edittext_square_black));
            }
            int l = surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().length();
            if (l == (number + 1)) {
                if (surveyDetailsModel.getQuestionModel().getDescriptiveAnswer() != null || !surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().equals("")) {
                    if (position <= before) {
                        editText.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().substring(position - 1, position));
                    } else {
                        editText.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().substring(position, position + 1));
                    }
                    dataArray.put(position, editText.getText().toString());
                    editText.requestFocus();
                    editText.setSelection(editText.getText().length());
                }
            }

            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});
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
                    //editText.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer());
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                } else {
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                }
            }

            linear_deliminater.addView(editText);
                   /* for receive code  */
            final int finalPosition = position;
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int a = charSequence.length();
                    editText.setSelection(editText.getText().length());
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (isRequiredEditText) {
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                        //SurveyDetailsItemFragment.scrollViewPager();
                        //editText.setSelection(editText.getText().length());
                    }


                    /*if (isRequiredEditText) {
                        if (editText.getText().toString().trim().length() > 0) {
                            SurveyDetailsItemFragment.disableScrollViewPager();
                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                        } else {
                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                        }
                    }*/

                    EditText text = (EditText) getActivity().getCurrentFocus();
                    progress = 0;
                 /*   if (charSequence.length() == 0) {
                        View back = text.focusSearch(View.FOCUS_LEFT); // or FOCUS_LEFT
                        dataArray.remove(finalPosition);
                        printLog("position size : " + dataArray.size());
                        if (back != null)
                            back.requestFocus();
                    }*/
                    if (charSequence.length() == 1) {
                        progress = 0;
                        View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_LEFT
                        printLog("text position : " + finalPosition + " text : " + editText.getText().toString());
                        dataArray.put(finalPosition, editText.getText().toString());
                        if (next != null)
                            next.requestFocus();
                        text.setSelection(1);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            };
            editText.addTextChangedListener(textWatcher);

            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                 /* if (keyCode == KeyEvent.KEYCODE_DEL) {
                        EditText text = (EditText) getActivity().getCurrentFocus();
                        if (editText.getText().toString().length() == 0) {
                            View back = text.focusSearch(View.FOCUS_LEFT); // or FOCUS_FORWARD
                            if (back != null) {
                                back.requestFocus();
                                EditText textBack = (EditText) getActivity().getCurrentFocus();
                                textBack.setText("");
                            }
                        }
                    }*/

                    if (keyCode == KeyEvent.KEYCODE_DEL)
                    {
                        printLog(editText.getId()+"");
                        if (editText.getText().length()==1){
                            editText.setText("");
                        }
                        else {
                            View back = editText.focusSearch(View.FOCUS_LEFT); // or FOCUS_FORWARD
                            if (back != null) {
                                back.requestFocus();
                                EditText textBack = (EditText) getActivity().getCurrentFocus();
                                textBack.setText("");
                                printLog(textBack.getId()+"");
                            }
                        }
                        return true;
                    }

                    return false;
                }
            });

            if (position == before) {
                final TextView textView = new TextView(mainActivity);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 5, 5, 5);
                textView.setPadding(10, 10, 10, 10);
                textView.setLayoutParams(layoutParams1);

                Typeface font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
                textView.setTypeface(font1);
                textView.setText(surveyDetailsModel.getQuestionModel().getDeliminater());
                linear_deliminater.addView(textView);
            }

        }


        mainLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (dataArray.size() == number) {
                    if (progress == 0) {
                        String answer = "";
                        for (int j = 1; j <= number; j++) {
                            answer = answer + dataArray.get(j);
                            if (j == before) {
                                answer = answer + surveyDetailsModel.getQuestionModel().getDeliminater();
                            }
                        }
                        if (progress != 1)
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                        SurveyDetailsItemFragment.scrollViewPager();
                        submitSurveyAnswer(answer);
                        printLog("on focus");
                    }
                } else {
                    if (progress == 0) {
                        //showDialogWithOkButton("Enter complete information");
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                        //SurveyDetailsItemFragment.scrollViewPager();
                        progress = 2;
                    }else {
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                        //SurveyDetailsItemFragment.scrollViewPager();
                        progress = 2;
                    }
                }
                return false;
            }
        });
    }



    private void submitSurveyAnswer(String request) {
        progress = 1;
        if (isConnectedToNetwork(mainActivity)) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                params.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                params.put("OptionId", "");
                params.put("Score", "");
                params.put("Type_Of_Section", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
                params.put("Descriptive_Answer", request);

                printLog("Descriptive_Answer : " + request);
                HashMap<String, HashMap<String, String>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            printLog("Response:survey submit" + response);

                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                            SurveyDetailsItemFragment.scrollViewPager();
                           /* if (response != null) {
                                if (surveySubmitted.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                    SurveyDetailsItemFragment.scrollViewPager();
                                } else {
                                    //status = 1;
                                    showDialogWithOkButton(surveySubmitted.getMessage());
                                    SurveyDetailsItemFragment.disableScrollViewPager();
                                }
                            }*/


                           /* ChangePasswordResponse surveySubmitted = new Gson().fromJson(response.toString(), ChangePasswordResponse.class);
                            if (surveySubmitted != null) {
                                if (surveySubmitted.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                    SurveyDetailsItemFragment.scrollViewPager();
                                } else {

                                    showDialogWithOkButton(surveySubmitted.getMessage());
                                    SurveyDetailsItemFragment.disableScrollViewPager();
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));*/
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


