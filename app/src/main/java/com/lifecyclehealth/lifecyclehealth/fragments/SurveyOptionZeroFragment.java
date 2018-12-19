package com.lifecyclehealth.lifecyclehealth.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomViewPager;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.application.TinyDB;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;

import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_CHECKED;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER;

/*
 This fragment will represent the option 0=RADIO BUTTON type of questions.
 */
public class SurveyOptionZeroFragment extends BaseFragmentWithOptions {
    private static final String SURVEY_EXTRAS_ZERO_TYPE = "type_zero_survey_extras_data";
    private SurveyDetailsModel surveyDetailsModel;
    MainActivity mainActivity;
    static int status = 0;
    private static int pagePosition;
    ImageView imageView;
    int id;
    HashMap<String, String> requestParameter = new HashMap<String, String>();
    final ArrayList<Integer> arrayList = new ArrayList<>();
//    TinyDB tinydb;

    public static SurveyOptionZeroFragment newInstance(String data, int position) {
        SurveyOptionZeroFragment zeroFragment = new SurveyOptionZeroFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_ZERO_TYPE, data);
        zeroFragment.setArguments(bundle);
        pagePosition = position;
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
        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        String text;
        if (surveyDetailsModel.getQuestionModel().isRequired()) {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?" + " </font>" + " <font color=#ffcc00>*</font>";
        } else {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "?";
        }

       /* tinydb = new TinyDB(getActivity().getApplicationContext());

        if(tinydb.getListInt(PREF_IS_CHECKED) != null && tinydb.getListInt(PREF_IS_CHECKED).size() > 0){
            for (int i = 0; i < tinydb.getListInt(PREF_IS_CHECKED).size(); i++) {
                arrayList.add(tinydb.getListInt(PREF_IS_CHECKED).get(i));
            }
        }*/

        TextView TextViewForName = (TextView) view.findViewById(R.id.surveyForName);
        if (MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT)) {
            TextViewForName.setVisibility(View.GONE);
        } else {
            TextViewForName.setVisibility(View.VISIBLE);
            TextViewForName.setText("Survey related to patient:" + surveyDetailsModel.getFirstName() + " " + surveyDetailsModel.getLastName());
        }
        TextViewQuestion.setText(Html.fromHtml(text));
        TextViewName.setText(surveyDetailsModel.getName());
        if (surveyDetailsModel.getQuestionModel().getImage_Url() != null) {
            addImage();
        } else {
            imageView.setVisibility(View.GONE);
        }

        if (surveyDetailsModel.getQuestionModel().getTypeOfAnswer() == 5) {
            addCheckBoxes();
        } else {
            addRadioButtons();
        }
    }

    @SuppressLint("RestrictedApi")
    private void addCheckBoxes() {
        final int number = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
        for (int row = 0; row < 1; row++) {
            final RadioGroup ll = new RadioGroup(mainActivity);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i <= number; i++) {
                String optionsToadd = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getName();
                final AppCompatCheckBox rdbtn = new AppCompatCheckBox(mainActivity);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, 10, 0, 5);
                rdbtn.setLayoutParams(layoutParams);
                rdbtn.setPadding(5, 0, 0, 0);
                rdbtn.setTextColor(ContextCompat.getColor(mainActivity, R.color.black));
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
                rdbtn.setTypeface(font);

                rdbtn.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                rdbtn.setText(optionsToadd);
                if (SurveyDetailsListFragment.isToDo) {
                    rdbtn.setButtonDrawable(R.drawable.selector_radiobutton1);
                    rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                    rdbtn.setSupportButtonTintList(getColorList());
                    rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                } else {
                    rdbtn.setButtonDrawable(R.drawable.deselector_radiobutton1);
                    rdbtn.setBackgroundResource(R.drawable.deselector_radiobutton);
                    rdbtn.setSupportButtonTintList(getDeColorList());
                    rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.black));
                    rdbtn.setFocusable(false);
                    rdbtn.setClickable(false);
                    rdbtn.setCursorVisible(false);
                }

                try {
                    if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).isSelected()) {
                        rdbtn.setChecked(true);
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                        arrayList.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i-1).getOptionId());
                    }else{
                        rdbtn.setChecked(false);
                        arrayList.remove(arrayList.indexOf(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i-1).getOptionId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            id = buttonView.getId();
                            for (int loop = 1; loop <= surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); loop++) {
                                if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionId() == id) {
                                    requestParameter.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                                    requestParameter.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                                    requestParameter.put("TypeOfSection", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
                                    requestParameter.put("Score", surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionValue() + "");
                                    arrayList.add(id);
                                }
                            }

                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                            SurveyDetailsItemFragment.scrollViewPager();
                            status = 1;
                            if (SurveyDetailsListFragment.isToDo) {
                                submitMultipleSelectedAnswerOfSurvey(requestParameter, arrayList);
                            }
                        } else {
                           id = buttonView.getId();

                            for (int loop = 1; loop <= surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); loop++) {
                                if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionId() == id) {
                                    requestParameter.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                                    requestParameter.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                                    requestParameter.put("TypeOfSection", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
                                    arrayList.remove(arrayList.indexOf(id));
                                    requestParameter.put("Score", surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionValue() + "");
                                }
                            }

                            if(arrayList.size() > 0){
                                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                SurveyDetailsItemFragment.scrollViewPager();
                            }else{
                                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                                SurveyDetailsItemFragment.scrollViewPager();
                            }
                            status = 1;
                            if (SurveyDetailsListFragment.isToDo) {
                                submitMultipleSelectedAnswerOfSurvey(requestParameter,arrayList);
                            }
                        }
                    }
                });

                ((ViewGroup) getView().findViewById(R.id.radioGroup)).addView(rdbtn);

            }
        }
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

    @SuppressLint("RestrictedApi")
    private void addRadioButtons() {
        final int number = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
        for (int row = 0; row < 1; row++) {
            final RadioGroup ll = new RadioGroup(mainActivity);
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 1; i <= number; i++) {
                String optionsToadd = surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getName();
                final AppCompatRadioButton rdbtn = new AppCompatRadioButton(mainActivity);
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 5);
                rdbtn.setLayoutParams(layoutParams);
                rdbtn.setPadding(5, 0, 0, 0);
                rdbtn.setTextColor(ContextCompat.getColor(mainActivity, R.color.black));
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
                rdbtn.setTypeface(font);

                rdbtn.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                rdbtn.setText(optionsToadd);
                if (SurveyDetailsListFragment.isToDo) {
                    rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                    rdbtn.setSupportButtonTintList(getColorList());
                    rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                } else {
                    rdbtn.setBackgroundResource(R.drawable.deselector_radiobutton);
                    rdbtn.setSupportButtonTintList(getDeColorList());
                    rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.black));
                    rdbtn.setFocusable(false);
                    rdbtn.setClickable(false);
                    rdbtn.setCursorVisible(false);
                }

                if (surveyDetailsModel.getQuestionModel().getAnswerId() == surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId()) {
                    rdbtn.setChecked(true);
                    SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                }

                rdbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            int id = buttonView.getId();
                            HashMap<String, String> requestParameter = new HashMap<String, String>();
                            for (int loop = 1; loop <= surveyDetailsModel.getQuestionModel().getQuestionOptions().size(); loop++) {
                                if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionId() == id) {
                                    requestParameter.put("ResponseId", surveyDetailsModel.getPatient_Survey_ResponseID());
                                    requestParameter.put("QuestionId", surveyDetailsModel.getQuestionModel().getPatientSurveyId());
                                    requestParameter.put("OptionId", id + "");
                                    requestParameter.put("Score", surveyDetailsModel.getQuestionModel().getQuestionOptions().get(loop - 1).getOptionValue() + "");
                                }
                            }
                            SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                            SurveyDetailsItemFragment.scrollViewPager();
                            status = 1;
                            if (SurveyDetailsListFragment.isToDo) {
                                submitSelectedAnswerOfSurvey(requestParameter);
                            }
                        } else {

                        }
                    }
                });

                ((ViewGroup) getView().findViewById(R.id.radioGroup)).addView(rdbtn);
            }
        }
    }

    private void submitSelectedAnswerOfSurvey(HashMap<String, String> hashMap) {
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("ResponseId", hashMap.get("ResponseId"));
                params.put("QuestionId", hashMap.get("QuestionId"));
                params.put("OptionId", hashMap.get("OptionId"));
                params.put("Score", hashMap.get("Score"));
                params.put("Type_Of_Section", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
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
                e.printStackTrace();
            }
        } else {
            showNoNetworkMessage();
        }
    }

    public void submitMultipleSelectedAnswerOfSurvey(HashMap<String, String> hashMap, ArrayList<Integer> arrayList) {
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, Object> params = new HashMap<>();
                params.put("ResponseId", hashMap.get("ResponseId"));
                params.put("QuestionId", hashMap.get("QuestionId"));
                params.put("Score", hashMap.get("Score"));
                params.put("Type_Of_Section", surveyDetailsModel.getQuestionModel().getTypeOfAnswer() + "");
                params.put("Descriptive_Answer", "");
                params.put("OptionId", "0");
                JsonArray array = new JsonArray();

                for (int s : arrayList) {
                    array.add(s);
                }
                params.put("Multiple_Options", array);

//                tinydb.putListInt(PREF_IS_CHECKED, arrayList);

                HashMap<String, HashMap<String, Object>> hashMapRequest = new HashMap<>();
                hashMapRequest.put("SurveyDetails", params);
                JSONObject requestJson = new JSONObject(new Gson().toJson(hashMapRequest));

                Log.e("JSONObject", requestJson + "");
                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SURVEY_SUBMIT_ANSWER, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response != null) {
                            printLog("Response:survey submit" + response);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        error.getMessage();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showNoNetworkMessage();
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

    private ColorStateList getDeColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(mainActivity, R.color.black)}
        );
    }

    private void callElectronicSignature(String data, String surveyId) {
        mainActivity.changeToSurveyElectronicSubmit(data, surveyId);
        //SurveyElectronicSubmit.newInstance(data,surveyId);
    }
}








