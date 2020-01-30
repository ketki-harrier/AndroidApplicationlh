/*
package com.lifecyclehealth.lifecyclehealth.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsItemFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsListFragment;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalMediaController;
import com.lifecyclehealth.lifecyclehealth.universal_video.UniversalVideoView;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_SUBMIT_ANSWER;

public class DemoSurveyZero extends AppCompatActivity {
    private SurveyDetailsModel surveyDetailsModel;
    private static final String SURVEY_EXTRAS_ZERO_TYPE = "type_zero_survey_extras_data";
    HashMap<String, String> requestParameter = new HashMap<String, String>();
    final ArrayList<Integer> arrayList = new ArrayList<>();
    private static int pagePosition;
    //    TinyDB tinydb;
    private ColorCode colorCode;
    String Stringcode;
    View mVideoLayout;
    UniversalVideoView mVideoView;
    //  VideoView video;
    UniversalMediaController mMediaController;
    private int cachedHeight;
    String new_video;
    String uriPath;
    MediaController mediaControls;
    ImageView imageView;
    MainActivity mainActivity;
    static int status = 0;
    int id;
    RadioGroup rg;
  //  private Object RadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_survey);
        pagePosition = position;
        Bundle bundle = new Bundle();
       // bundle.putString(SURVEY_EXTRAS_ZERO_TYPE, data);
        surveyDetailsModel = new Gson().fromJson(bundle.getString(SURVEY_EXTRAS_ZERO_TYPE,data), SurveyDetailsModel.class);
        rg = (RadioGroup)findViewById(R.id.radioGroup);

    }

    private void setupView(View view) {
        //  try {
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcoden = "";
        String hashcode = "";

        if (demo == null) {
            hashcode = "Green";
            Stringcode = "259b24";
        } else if (demo != null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();
          */
/*  }
            else*//*

            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
        //     }catch (Exception e){e.printStackTrace();}
        TextView TextViewName = (TextView) view.findViewById(R.id.surveyName);
        TextView TextViewQuestion = (TextView) view.findViewById(R.id.questionTv);
        TextViewName.setTextColor(Color.parseColor("#" + Stringcode));

        imageView = (ImageView) view.findViewById(R.id.imageView);
        String text;
        if (surveyDetailsModel.getQuestionModel().isRequired()) {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "" + " </font>" + " <font color=#ffcc00>*</font>";
        } else {
            text = "<font color=#000000>" + surveyDetailsModel.getPagePosition() + ". " + surveyDetailsModel.getQuestionModel().getDescription() + "";
        }

       */
/* tinydb = new TinyDB(getActivity().getApplicationContext());

        if(tinydb.getListInt(PREF_IS_CHECKED) != null && tinydb.getListInt(PREF_IS_CHECKED).size() > 0){
            for (int i = 0; i < tinydb.getListInt(PREF_IS_CHECKED).size(); i++) {
                arrayList.add(tinydb.getListInt(PREF_IS_CHECKED).get(i));
            }
        }*//*


        new_video = String.valueOf(surveyDetailsModel.getQuestionModel().getVideo_Url());


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

        //   showVideo();
        new_video = surveyDetailsModel.getQuestionModel().getVideo_Url();
        //      try {

        if(new_video == null){
            if (surveyDetailsModel.getQuestionModel().getTypeOfAnswer() == 5) {
                addCheckBoxes();
            } else {
                addRadioButtons();
            }
        }
        else if (new_video.equalsIgnoreCase("") || new_video.equalsIgnoreCase("null") ||  new_video == "null" || new_video.equalsIgnoreCase(null)) {
      */
/*  if (surveyDetailsModel.getQuestionModel().getVideo_Url() != null) {
            showVideo();
        } else if(surveyDetailsModel.getQuestionModel().getVideo_Url() == null){*//*

            if (surveyDetailsModel.getQuestionModel().getTypeOfAnswer() == 5) {
                addCheckBoxes();
            } else {
                addRadioButtons();
            }
        }
        else if(new_video.equalsIgnoreCase(null)){
            if (surveyDetailsModel.getQuestionModel().getTypeOfAnswer() == 5) {
                addCheckBoxes();
            } else {
                addRadioButtons();
            }
        }
        else {

            if (surveyDetailsModel.getQuestionModel().getTypeOfAnswer() == 5) {
                addCheckBoxes();
                showVideo();
            } else {
                addRadioButtons();
                showVideo();
            }
        }
        //    }
        //    catch (NullPointerException e){e.printStackTrace();}
        // }


    }

    private void showVideo() {
        Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.survey_three_show);

        //  video = (VideoView) dialog.findViewById(R.id.surfaceview);

        mVideoView = (UniversalVideoView) dialog.findViewById(R.id.videoView);

        mMediaController = (UniversalMediaController) dialog.findViewById(R.id.media_controller);
        mVideoLayout = dialog.findViewById(R.id.video_layout);
        mVideoView.setMediaController(mMediaController);
        //     mVideoView.start();
        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {

            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {

            }
        });
        //   video.setMediaController(mediaControls);
        setVideoAreaSize();
        //  video.setVideoURI(Uri.parse(surveyDetailsModel.getQuestionModel().getVideo_Url()));
        // start a video
        //  video.start();

        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(getApplicationContext());
            mediaControls.setAnchorView(mVideoView);
        }

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //printLog("onCompletion");
            }
        });
        dialog.show();
    }

    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                mVideoView.setVideoPath(surveyDetailsModel.getQuestionModel().getVideo_Url(), getApplicationContext());
                mVideoView.requestFocus();
                mVideoView.start();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void addCheckBoxes() {
        final int number = surveyDetailsModel.getQuestionModel().getQuestionOptions().size();
        for (int row = 0; row < 1; row++) {
           // final RadioGroup ll = new RadioGroup();
            rg.setOrientation(LinearLayout.VERTICAL);

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
                Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
                rdbtn.setTypeface(font);

                rdbtn.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                rdbtn.setText(optionsToadd);
                if (SurveyDetailsListFragment.isToDo) {
                    rdbtn.setButtonDrawable(R.drawable.selector_radiobutton1);
                    rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                    rdbtn.setSupportButtonTintList(getColorList());
                    //rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                    rdbtn.setHighlightColor(Color.parseColor("#" + Stringcode));
                } else {
                    rdbtn.setButtonDrawable(R.drawable.deselector_radiobutton1);
                    rdbtn.setBackgroundResource(R.drawable.deselector_radiobutton);
                    rdbtn.setSupportButtonTintList(getDeColorList());
                    rdbtn.setHighlightColor(ContextCompat.getColor(DemoSurveyZero.this, R.color.black));
                    rdbtn.setFocusable(false);
                    rdbtn.setClickable(false);
                    rdbtn.setCursorVisible(false);
                }

                try {
                    if (surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).isSelected()) {
                        rdbtn.setChecked(true);
                        SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                        arrayList.add(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                    } else {
                        rdbtn.setChecked(false);
                        arrayList.remove(arrayList.indexOf(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId()));
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

                            if (arrayList.size() > 0) {
                                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), true);
                                SurveyDetailsItemFragment.scrollViewPager();
                            } else {
                                SurveyDetailsItemFragment.hashmapOfKey.put(surveyDetailsModel.getQuestionModel().getPatientSurveyId(), false);
                                SurveyDetailsItemFragment.scrollViewPager();
                            }
                            status = 1;
                            if (SurveyDetailsListFragment.isToDo) {
                                submitMultipleSelectedAnswerOfSurvey(requestParameter, arrayList);
                            }
                        }
                    }
                });

                rg.addView(rdbtn);

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
        //    String bool =  surveyDetailsModel.getQuestionModel().isRequired();
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
                Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
                rdbtn.setTypeface(font);

                rdbtn.setId(surveyDetailsModel.getQuestionModel().getQuestionOptions().get(i - 1).getOptionId());
                rdbtn.setText(optionsToadd);
                if (SurveyDetailsListFragment.isToDo) {
                    rdbtn.setBackgroundResource(R.drawable.selector_radiobutton);
                    rdbtn.setSupportButtonTintList(getColorList());
                    //rdbtn.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                    rdbtn.setHighlightColor(Color.parseColor("#" + Stringcode));
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

                rg.addView(rdbtn);
            }
        }
    }

    private void submitSelectedAnswerOfSurvey(HashMap<String, String> hashMap) {
        if (mainActivity.isConnectedToNetwork(mainActivity)) {
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
                            //printLog("Response:survey submit" + response);
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
            mainActivity.showNoNetworkMessage();
        }
    }

    public void submitMultipleSelectedAnswerOfSurvey(HashMap<String, String> hashMap, ArrayList<Integer> arrayList) {
        if (mainActivity.isConnectedToNetwork(mainActivity)) {
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
                           // printLog("Response:survey submit" + response);
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
            mainActivity.showNoNetworkMessage();
        }
    }

    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                //new int[]{ContextCompat.getColor(mainActivity, R.color.colorPrimary)
                new int[]{Color.parseColor("#" + Stringcode)}
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
}
*/