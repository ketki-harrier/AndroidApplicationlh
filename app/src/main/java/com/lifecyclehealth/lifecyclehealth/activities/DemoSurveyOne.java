/*
package com.lifecyclehealth.lifecyclehealth.activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsItemFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsListFragment;
import com.lifecyclehealth.lifecyclehealth.model.ChangePasswordResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;

import org.json.JSONObject;

import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER;

public class DemoSurveyOne extends AppCompatActivity {
    private static int pagePosition;
    private boolean isToDo = false, isCompleted = false;
    private SurveyDetailsModel surveyDetailsModel;
    ImageView imageView;
    int typeOfSurvey;
    ScrollView mainLinear;
    MainActivity mainActivity;
    Bundle bundle = new Bundle();
    private static final String SURVEY_EXTRAS_ONE_TYPE = "type_one_survey_extras_data";
    static int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_survey_one);
        pagePosition = position;

        isToDo = SurveyDetailsListFragment.isToDo;
        isCompleted = SurveyDetailsListFragment.isCompleted;
        surveyDetailsModel = new Gson().fromJson(bundle.getString(SURVEY_EXTRAS_ONE_TYPE), SurveyDetailsModel.class);


        setupView(view);
    }

    private void setupView(View view) {
        typeOfSurvey = surveyDetailsModel.getQuestionModel().getTypeOfAnswer();
        mainLinear = (ScrollView) view.findViewById(R.id.mainLinear);
        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        // String text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
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
        imageView = (ImageView) view.findViewById(R.id.imageView);
        if (surveyDetailsModel.getQuestionModel().getImage_Url() != null) {
            addImage();
        } else {
            imageView.setVisibility(View.GONE);
        }
        addEditText();
    }


    private void addImage() {

        com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

        imageLoader.get(surveyDetailsModel.getQuestionModel().getImage_Url(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Log", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    private void addEditText() {
        LinearLayout linearLayout = new LinearLayout(mainActivity);
        final EditText editTextView = new EditText(mainActivity);
        editTextView.setGravity(Gravity.CENTER);
        editTextView.setSingleLine(true);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        editTextView.setLayoutParams(params);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        editTextView.setTypeface(font);
        editTextView.setHint("Please type here...");
        editTextView.setBackgroundResource(R.color.white);
        //editTextView.setBackgroundResource(R.drawable.edittext_border);

        if (SurveyDetailsListFragment.isToDo) {
            editTextView.setFocusable(true);
            editTextView.setClickable(true);
            editTextView.setCursorVisible(true);
        } else {
            editTextView.setFocusable(false);
            editTextView.setClickable(false);
            editTextView.setCursorVisible(false);
        }

        final boolean isRequiredEditText = surveyDetailsModel.getQuestionModel().isRequired();
        if (isRequiredEditText) {
            if (!surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().trim().equals("")) {
                editTextView.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer());
                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
            } else {
                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
            }
        }
        else {
            if (!surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().trim().equals("")) {
                editTextView.setText(surveyDetailsModel.getQuestionModel().getDescriptiveAnswer());
                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
            } else {
                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
            }
        }


        mainLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!editTextView.getText().toString().trim().equals("")) {
                    if (status == 1) {
                        // if (!surveyDetailsModel.getQuestionModel().getDescriptiveAnswer().equals(editTextView.getText().toString().trim())) {
                        status = 0;
                        submitSurveyAnswer(editTextView.getText().toString());
                        printLog("on focus");
                    }
                } else if (isRequiredEditText) {
                    SurveyDetailsItemFragment.disableScrollViewPager();
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                }

                printLog("on focus mainListener");
                return false;
            }
        });


        editTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                status = 1;
                if (isRequiredEditText) {
                    if (editTextView.getText().toString().trim().length() > 0) {
                        SurveyDetailsItemFragment.disableScrollViewPager();
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                    } else {
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                //SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                //SurveyDetailsItemFragment.scrollViewPager();
                if (isRequiredEditText) {
                    if (editTextView.getText().toString().trim().length() > 0) {
                        SurveyDetailsItemFragment.disableScrollViewPager();
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                    } else {
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                    }
                }
            }
        });


        ((ViewGroup) getView().findViewById(R.id.linearLayout)).addView(editTextView);


    }

    private void submitSurveyAnswer(String request) {

        if (isConnectedToNetwork(mainActivity)) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                params.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                params.put("OptionId", "");
                params.put("Score", "");
                params.put("Type_Of_Section", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
                params.put("Descriptive_Answer", request);

                HashMap<String, HashMap<String, String>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            printLog("Response:survey submit" + response);
                            ChangePasswordResponse surveySubmitted = new Gson().fromJson(response.toString(), ChangePasswordResponse.class);
                            if (surveySubmitted != null) {
                                if (surveySubmitted.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                    SurveyDetailsItemFragment.scrollViewPager();
                                } else {
                                    status = 1;
                                    showDialogWithOkButton(surveySubmitted.getMessage());
                                    SurveyDetailsItemFragment.disableScrollViewPager();
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
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


    private void callElectronicSignature(String data, String surveyId) {
        mainActivity.changeToSurveyElectronicSubmit(data, surveyId);
    }
}
*/
