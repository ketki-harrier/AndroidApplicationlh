package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.*;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomViewPager;
import com.lifecyclehealth.lifecyclehealth.adapters.SurveyPagerAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.SurveyDetailsQuestionDto;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.model.QuestionModel;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;
import com.lifecyclehealth.lifecyclehealth.model.SurveyElectronicSubmitResponse;
import com.lifecyclehealth.lifecyclehealth.model.SurveySection;
//import com.lifecyclehealth.lifecyclehealth.utils.OnSwipeTouchListener;
//import com.lifecyclehealth.lifecyclehealth.utils.OnSwipeTouchListenerPager;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.PreferenceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_QA_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_QA_PROVIDER;

/**
 * Created by satyam on 17/04/2017.
 */

public class SurveyDetailsItemFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    //private ViewPager viewPager;
    private static CustomViewPager viewPager;
    private static final String SURVEY_EXTRAS_HOLDER = "holder_extra";
    //private SurveyModel surveyModel;
    private PatientSurveyItem surveyItem;
    private boolean isPatient;
    public static ArrayList<String> arrayKey;
    public static HashMap<String, Boolean> hashmapOfKey;
    public static HashMap<String, String> hashmapOfKeyTitle;
    boolean checkflag = true;

    //scroll viewpager
    private static final float thresholdOffset = 0.1f;
    private static final int thresholdOffsetPixels = 1;
    public static boolean scrollStarted, checkDirection;
    Boolean e_submit;
    Button prev, next;
    SurveyPagerAdapter pagerAdapter;
    Handler handler = new Handler();
    Runnable Update;


    //int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    //static List<SurveySection> surveySection;
    int status = 0;


    public static SurveyDetailsItemFragment newInstance(String data) {
        SurveyDetailsItemFragment holderFragment = new SurveyDetailsItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_EXTRAS_HOLDER, data);
        holderFragment.setArguments(bundle);
        //  surveySection1 = null;
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveyDetailsItemFragment";
    }

    @Override
    public void printLog(String message) {
        super.printLog(message);
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

        View rootview = inflater.inflate(R.layout.fragment_survey_details_holder, container, false);
        prev = (Button) rootview.findViewById(R.id.prev);
    /*    prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });*/
        next = (Button) rootview.findViewById(R.id.next);
       /* next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });*/

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        surveyItem = new Gson().fromJson(getArguments().get(SURVEY_EXTRAS_HOLDER).toString(), PatientSurveyItem.class);
        arrayKey = new ArrayList<String>();
        hashmapOfKey = new HashMap<String, Boolean>();
        hashmapOfKeyTitle = new HashMap<String, String>();
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
//        getSurveyList(surveyItem.getPatientSurveyResponseId());
        //next.setBackgroundResource(R.drawable.next_light_blue);
        // prev.setBackgroundResource(R.drawable.prev_light_blue);
        getSurveyList1(surveyItem.getPatientSurveyResponseId());
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        if (resposne != null) {
            ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcodes = "";
            String hashcode = "";

            /*if (demo == null) {
                hashcode = "Green";
                Stringcode = "259b24";
                next.setBackgroundResource(R.drawable.next_green);
                prev.setBackgroundResource(R.drawable.prev_green);
            } else*/
            if (demo != null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
             /*   }
               else*/
             /*   if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    //Stringcode = "#333333";
                    Stringcode = "333333";
                    next.setBackgroundResource(R.drawable.next_blck);
                    prev.setBackgroundResource(R.drawable.prev_black);
                }*/

                if (hashcode.equals("SkyBlue")) {
                    //Stringcode = "#333333";
                    //   Stringcode = "189ad3";
                    next.setBackgroundResource(R.drawable.next_light_blue);
                    prev.setBackgroundResource(R.drawable.prev_light_blue);
                } else if (hashcode.equals("Green")) {
                    //Stringcode = "#333333";
                    //   Stringcode = "189ad3";
                    next.setBackgroundResource(R.drawable.next_green);
                    prev.setBackgroundResource(R.drawable.prev_green);
                } else if (hashcode.equals("Black")) {
                    //Stringcode = "#333333";
                    //   Stringcode = "189ad3";
                    next.setBackgroundResource(R.drawable.next_blck);
                    prev.setBackgroundResource(R.drawable.prev_black);
                } else if (hashcode.equals("Blue")) {
                    //Stringcode = "#333333";
                    //   Stringcode = "189ad3";
                    next.setBackgroundResource(R.drawable.next_blue);
                    prev.setBackgroundResource(R.drawable.prev_blue);
                }
            }
        }

        // next.setBackgroundResource(R.drawable.next_light_blue);
        // prev.setBackgroundResource(R.drawable.prev_light_blue);
    }


    /* Set view pager with values*/
    private void setViewPager(final List<SurveySection> surveySection) {
        arrayKey = new ArrayList<String>();
        hashmapOfKey = new HashMap<String, Boolean>();
        hashmapOfKeyTitle = new HashMap<String, String>();
        //checkflag = false;

        for (int i = 0; i < getListFormattedForViewCreation(surveySection).size(); i++) {
            arrayKey.add(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId());
            hashmapOfKeyTitle.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), getListFormattedForViewCreation(surveySection).get(i).getName());

            if (getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().isRequired()) {


                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), false);
            } else {
                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), true);
            }
        }


        pagerAdapter = new SurveyPagerAdapter(getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        // pagerAdapter = new SurveyPagerAdapter(this.getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  Toast.makeText(mainActivity, "position" + position, Toast.LENGTH_SHORT).show();
                Log.d("position", position + "");
                printLog("Total pages quantity" + getListFormattedForViewCreation(surveySection).get(0).getPagesQuantity() + "");
                printLog("Total arraysize" + arrayKey.size());

                //    Toast.makeText(mainActivity, "" + positionOffset, Toast.LENGTH_SHORT).show();
                //    Toast.makeText(mainActivity, "" + positionOffsetPixels, Toast.LENGTH_SHORT).show();

                e_submit = PreferenceUtils.getESignature(getContext()).equals(true);

                status = 0;
                if (checkDirection) {
                    if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                        //for completed
                        if (SurveyDetailsListFragment.isCompleted) {
                            if (arrayKey.size() == 1) {
                                if (arrayKey.size() <= (viewPager.getCurrentItem())) {
                                    showScore(getListFormattedForViewCreation(surveySection).get(0));
                                }
                            } else {
                                if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                    showScore(getListFormattedForViewCreation(surveySection).get(0));
                                }
                            }
                        } else if (SurveyDetailsListFragment.isToDo) {
                            int currentPage = viewPager.getCurrentItem();
                            printLog("Total arraysize" + arrayKey.size());
                            printLog("currentPage" + currentPage);
                            String s = arrayKey.get(currentPage);
                            boolean a = hashmapOfKey.get(s);
                            //   final Handler handler = new Handler();
                            //  Runnable mLoopingRunnable = null;

                            if (hashmapOfKey.get(s)) {


                                //handler.removeCallbacks(Update);
                                viewPager.disableScroll(false);
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                /* changes 08/11/19*/
                                //viewPager.setOffscreenPageLimit(0);
                                //  viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                status = 0;
                            } else {
                                viewPager.disableScroll(true);
                                //   viewPager.invalidate();
                                //   viewPager.setEnabled(false);
                                selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                                //      pagerAdapter.notifyDataSetChanged();
                                //viewPager.setOnTouchListener(null);
                                //viewPager.beginFakeDrag();
                                //viewPager.setPagingEnabled(false);
                                //viewPager.getCurrentItem();
                                status = 1;
                                //  viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                //viewPager.setOffscreenPageLimit(0);

                                //selectSurveyDialog(hashmapOfKeyTitle.get(s));
                            }
                            if (status == 0) {
                                SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(true))) {
                                    mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                } else if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(false))) {
                                    mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                }
                            }
                        }
                    } else {
                        //   final Handler handler = new Handler();
                        //   Runnable mLoopingRunnable = null;
                        printLog("left");
                        if (SurveyDetailsListFragment.isCompleted) {
                            if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                showScore(getListFormattedForViewCreation(surveySection).get(0));
                            }
                        } /*else if (SurveyDetailsListFragment.isSchedule) {
                            if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                showScore(getListFormattedForViewCreation(surveySection).get(0));
                            }
                        } */ else if (SurveyDetailsListFragment.isToDo) {
                            int currentPage = viewPager.getCurrentItem();
                            printLog("Total arraysize" + arrayKey.size());
                            printLog("currentPage" + currentPage);
                            String s = arrayKey.get(currentPage);
                            if (hashmapOfKey.get(s)) {

                                viewPager.disableScroll(false);
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                status = 0;
                            } else {


                                // handler.removeCallbacks(Update);
                                // handler.postDelayed(Update, 3000);
                                // viewPager.getCurrentItem();
                                // viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                //viewPager.setOmit(1);
                                //    viewPager.ffscreenPageLibeginFakeDrag();
                                viewPager.disableScroll(true);
                                //  viewPager.invalidate();
                                //   viewPager.setEnabled(false);
                           //     selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                /* new changes 20/12/19*/
                                viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                                //  pagerAdapter.notifyDataSetChanged();
                                //  viewPager.setPagingEnabled(false);
                                status = 1;

                                // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                            }

                            //String net = PreferenceUtils.getESignature(getContext());
                            if (status == 0) {
                                final SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                if ((surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1)) {
                                    if (PreferenceUtils.getESignature(getContext()).equals(true)) {
                                        mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                  /* viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
                                        @Override
                                        public void onSwipeOutAtStart() {
                                            printLog("swipe start");
                                        }

                                        @Override
                                        public void onSwipeOutAtEnd() {
                                            printLog("swipe end");
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                    });*/
                                    } else if (PreferenceUtils.getESignature(getContext()).equals(false)) {
                                        mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    }
                                }
                            }
                        }
                    }
                }
                checkDirection = false;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;


                    // handler.removeCallbacks(Update);
                } else {

                    scrollStarted = false;
                }

                if (state == ViewPager.SCROLL_STATE_IDLE) {


                }
            }
        });

    }


    /* Set view pager with values*/
    /*private void setViewPager1(final List<SurveySection> surveySection) {
        arrayKey = new ArrayList<String>();
        hashmapOfKey = new HashMap<String, Boolean>();
        hashmapOfKeyTitle = new HashMap<String, String>();

        for (int i = 0; i < getListFormattedForViewCreation(surveySection).size(); i++) {
            arrayKey.add(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId());
            hashmapOfKeyTitle.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), getListFormattedForViewCreation(surveySection).get(i).getName());

            if (getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().isRequired()) {


                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), false);
            } else {
                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), true);
            }
        }


        pagerAdapter = new SurveyPagerAdapter(getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        // pagerAdapter = new SurveyPagerAdapter(this.getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //  Toast.makeText(mainActivity, "position" + position, Toast.LENGTH_SHORT).show();
                Log.d("position", position + "");
                printLog("Total pages quantity" + getListFormattedForViewCreation(surveySection).get(0).getPagesQuantity() + "");
                printLog("Total arraysize" + arrayKey.size());

                //  Toast.makeText(mainActivity, "" + positionOffset, Toast.LENGTH_SHORT).show();
                //   Toast.makeText(mainActivity, "" + positionOffsetPixels, Toast.LENGTH_SHORT).show();

                e_submit = PreferenceUtils.getESignature(getContext()).equals(true);

                status = 0;
                if (checkflag) {
                    if (checkDirection) {
                        if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                            //for completed
                            if (SurveyDetailsListFragment.isCompleted) {
                                if (arrayKey.size() == 1) {
                                    if (arrayKey.size() <= (viewPager.getCurrentItem())) {
                                        //   viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                                    }
                                } else {
                                    //Nikhil
                                    if (arrayKey.size() < (viewPager.getCurrentItem()) +1) {
                                        //     viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                                    }
                                }
                            } else if (SurveyDetailsListFragment.isToDo) {
                                int currentPage = viewPager.getCurrentItem();
                                printLog("Total arraysize" + arrayKey.size());
                                printLog("currentPage" + currentPage);
                                String s = arrayKey.get(currentPage);
                                boolean a = hashmapOfKey.get(s);
                                //   final Handler handler = new Handler();
                                //  Runnable mLoopingRunnable = null;

                                if (hashmapOfKey.get(s)) {


                                    //handler.removeCallbacks(Update);
                                    viewPager.disableScroll(false);
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                    *//* changes 08/11/19*//*
                                    //viewPager.setOffscreenPageLimit(0);
                                    //  viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                    status = 0;
                                } else {
                                    viewPager.disableScroll(true);
                                    //   viewPager.invalidate();
                                    //   viewPager.setEnabled(false);
                                    selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                    viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                                    //      pagerAdapter.notifyDataSetChanged();
                                    //viewPager.setOnTouchListener(null);
                                    //viewPager.beginFakeDrag();
                                    //viewPager.setPagingEnabled(false);
                                    //viewPager.getCurrentItem();
                                    status = 1;
                                    //  viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                    //viewPager.setOffscreenPageLimit(0);

                                    //selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                }
                                if (status == 0) {
                                    SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                    if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(true))) {
                                        mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    } else if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(false))) {
                                        mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    }
                                }
                            }
                        } else {
                            //   final Handler handler = new Handler();
                            //   Runnable mLoopingRunnable = null;
                            printLog("left");
                            if (SurveyDetailsListFragment.isCompleted) {
                                if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                    showScore(getListFormattedForViewCreation(surveySection).get(0));
                                }
                            } *//*else if (SurveyDetailsListFragment.isSchedule) {
                                if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                    showScore(getListFormattedForViewCreation(surveySection).get(0));
                                }
                            }*//* else if (SurveyDetailsListFragment.isToDo) {
                                int currentPage = viewPager.getCurrentItem();
                                printLog("Total arraysize" + arrayKey.size());
                                printLog("currentPage" + currentPage);
                                String s = arrayKey.get(currentPage);
                                if (hashmapOfKey.get(s)) {

                                    viewPager.disableScroll(false);
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                                    status = 0;
                                } else {


                                    // handler.removeCallbacks(Update);
                                    // handler.postDelayed(Update, 3000);
                                    // viewPager.getCurrentItem();
                                    // viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                                    //viewPager.setOmit(1);
                                    //    viewPager.ffscreenPageLibeginFakeDrag();
                                    viewPager.disableScroll(true);
                                    //  viewPager.invalidate();
                                    //   viewPager.setEnabled(false);
                                    selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                    *//* new changes 20/12/19*//*
                                    viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                                    //  pagerAdapter.notifyDataSetChanged();
                                    //  viewPager.setPagingEnabled(false);
                                    status = 1;

                                    // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                }

                                //String net = PreferenceUtils.getESignature(getContext());
                                if (status == 0) {
                                    final SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                    if ((surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1)) {
                                        if (PreferenceUtils.getESignature(getContext()).equals(true)) {
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                  *//* viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
                                        @Override
                                        public void onSwipeOutAtStart() {
                                            printLog("swipe start");
                                        }

                                        @Override
                                        public void onSwipeOutAtEnd() {
                                            printLog("swipe end");
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                    });*//*
                                        } else if (PreferenceUtils.getESignature(getContext()).equals(false)) {
                                            mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    checkDirection = false;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                    // handler.removeCallbacks(Update);
                } else {

                    scrollStarted = false;
                }

                if (state == ViewPager.SCROLL_STATE_IDLE) {


                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDirection = true;
                checkflag = false;


                if (SurveyDetailsListFragment.isCompleted) {
                    if (arrayKey.size() == 1) {
                        if (arrayKey.size() <= (viewPager.getCurrentItem())) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem());
                            showScore(getListFormattedForViewCreation(surveySection).get(0));
                        }
                    } else {
                        //Nikhil
                        if (arrayKey.size() < (viewPager.getCurrentItem()) + 1) {

                            showScore(getListFormattedForViewCreation(surveySection).get(0));
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    }
                } else if (SurveyDetailsListFragment.isSchedule) {
                  *//*  if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {

                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }*//*

                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else if (SurveyDetailsListFragment.isToDo) {
                *//*    int currentPage = viewPager.getCurrentItem();
                    printLog("Total arraysize" + arrayKey.size());
                    printLog("currentPage" + currentPage);
                    String s = arrayKey.get(currentPage);
                    boolean a = hashmapOfKey.get(s);*//*
                    //   if(hashmapOfKey.get(arrayKey.get(viewPager.getCurrentItem())))
                    //      abcd = hashmapOfKey.get(viewPager.getCurrentItem()-1);
                    if (hashmapOfKey.get(arrayKey.get(viewPager.getCurrentItem()))) {


                        //handler.removeCallbacks(Update);
                        viewPager.disableScroll(false);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        // viewPager.setCurrentItem(getNextPossibleItemIndex(1), true);
                        // viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                        //  viewPager.setCurrentItem(viewPager.getCurrentItem() , true);
                        *//* changes 08/11/19*//*
                        //viewPager.setOffscreenPageLimit(0);
                        //  viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                        status = 0;
                    } else {
                        viewPager.disableScroll(true);
                        //   viewPager.invalidate();
                        //   viewPager.setEnabled(false);
                        selectSurveyDialog(hashmapOfKeyTitle.get(arrayKey.get(viewPager.getCurrentItem())));
                        viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                        status = 1;
                    }
                    if (status == 0) {
                        SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                        if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(true))) {
                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                        } else if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(false))) {
                            mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                        }
                    }
                }
            }

        });


        prev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                checkflag = false;
                if (SurveyDetailsListFragment.isCompleted) {
                    if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                } else if (SurveyDetailsListFragment.isSchedule) {
                   *//* if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {

                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }*//*
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else if (SurveyDetailsListFragment.isToDo) {
                    int currentPage = viewPager.getCurrentItem();
                    printLog("Total arraysize" + arrayKey.size());
                    printLog("currentPage" + currentPage);
                    String s = arrayKey.get(currentPage);
                    if (hashmapOfKey.get(s)) {

                        viewPager.disableScroll(false);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        status = 0;
                    } else {


                        // handler.removeCallbacks(Update);
                        // handler.postDelayed(Update, 3000);
                        // viewPager.getCurrentItem();
                        // viewPager.setOffscreenPageLimit(arrayKey.size() - 1);
                        //viewPager.setOmit(1);
                        //    viewPager.ffscreenPageLibeginFakeDrag();
                        viewPager.disableScroll(true);
                        //  viewPager.invalidate();
                        //   viewPager.setEnabled(false);
                        selectSurveyDialog(hashmapOfKeyTitle.get(s));
                        *//* new changes 20/12/19*//*
                        viewPager.setCurrentItem(viewPager.getCurrentItem(), false);
                        //  pagerAdapter.notifyDataSetChanged();
                        //  viewPager.setPagingEnabled(false);
                        status = 1;

                        // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                    }

                    //String net = PreferenceUtils.getESignature(getContext());
                    if (status == 0) {
                        final SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                        if ((surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1)) {
                            if (PreferenceUtils.getESignature(getContext()).equals(true)) {
                                mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                  *//* viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
                                        @Override
                                        public void onSwipeOutAtStart() {
                                            printLog("swipe start");
                                        }

                                        @Override
                                        public void onSwipeOutAtEnd() {
                                            printLog("swipe end");
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                    });*//*
                            } else if (PreferenceUtils.getESignature(getContext()).equals(false)) {
                                mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                            }
                        }
                    }
                }
            }
        });
        //  checkDirection = false;
    }*/


    private void setViewPager1(final List<SurveySection> surveySection) {
        arrayKey = new ArrayList<String>();
        hashmapOfKey = new HashMap<String, Boolean>();
        hashmapOfKeyTitle = new HashMap<String, String>();

        for (int i = 0; i < getListFormattedForViewCreation(surveySection).size(); i++) {
            arrayKey.add(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId());
            hashmapOfKeyTitle.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), getListFormattedForViewCreation(surveySection).get(i).getName());

            if (getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().isRequired()) {
                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), false);
            } else {
                hashmapOfKey.put(getListFormattedForViewCreation(surveySection).get(i).getQuestionModel().getPatientSurveyId(), true);
            }
        }

        final SurveyPagerAdapter pagerAdapter = new SurveyPagerAdapter(getChildFragmentManager(), getListFormattedForViewCreation(surveySection));
        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(mainActivity, "position" + position, Toast.LENGTH_SHORT).show();
                Log.d("position", position + "");
                printLog("Total pages quantity" + getListFormattedForViewCreation(surveySection).get(0).getPagesQuantity() + "");
                printLog("Total arraysize" + arrayKey.size());


                status = 0;
                if (checkflag) {

                    if (checkDirection) {
                        if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                            //for completed
                            if (SurveyDetailsListFragment.isCompleted) {
                                if (arrayKey.size() == 1) {
                                    if (arrayKey.size() <= (viewPager.getCurrentItem())) {
                                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                                    }
                                } else {
                                    if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                                    }

                                }
                            } else if (SurveyDetailsListFragment.isToDo) {
                                int currentPage = viewPager.getCurrentItem();
                                printLog("Total arraysize" + arrayKey.size());
                                printLog("currentPage" + currentPage);
                                String s = arrayKey.get(currentPage);
                                boolean a = hashmapOfKey.get(s);

                                if (hashmapOfKey.get(s)) {
                                    viewPager.disableScroll(false);
                                    status = 0;
                                } else {
                                    selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                    viewPager.disableScroll(true);
                                    // viewPager.beginFakeDrag();
                                    status = 1;
                                    //selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                }
                                if (status == 0) {
                                    SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                    if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(true))) {
                                        mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    } else if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(false))) {
                                        mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    }
                                }
                            }
                        } else {
                            printLog("left");
                            if (SurveyDetailsListFragment.isCompleted) {
                                if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                    showScore(getListFormattedForViewCreation(surveySection).get(0));
                                }
                            }  //*if (SurveyDetailsListFragment.isSchedule) {
                            if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                                //showScore(getListFormattedForViewCreation(surveySection).get(0));
                            }
                            //*else if (SurveyDetailsListFragment.isToDo) {
                            int currentPage = viewPager.getCurrentItem();
                            printLog("Total arraysize" + arrayKey.size());
                            printLog("currentPage" + currentPage);
                            String s = arrayKey.get(currentPage);
                            if (hashmapOfKey.get(s)) {
                                viewPager.disableScroll(false);
                                viewPager.setCurrentItem(viewPager.getCurrentItem());
                                status = 0;
                            } else {
                               // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                                viewPager.disableScroll(false);
                                try {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                                }
                                catch (ClassCastException e){e.printStackTrace();}


                               // viewPager.getCurrentItem();


                                //viewPager.beginFakeDrag();
                                //  viewPager.disableScroll(true);
                                status = 1;
                                // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                            }

                            //String net = PreferenceUtils.getESignature(getContext());
                            if (status == 0) {
                                final SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                                if ((surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1)) {
                                    if (PreferenceUtils.getESignature(getContext()).equals(true)) {
                                        mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                  /* viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
                                        @Override
                                        public void onSwipeOutAtStart() {
                                            printLog("swipe start");
                                        }

                                        @Override
                                        public void onSwipeOutAtEnd() {
                                            printLog("swipe end");
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                     });*/
                                    } else if (PreferenceUtils.getESignature(getContext()).equals(false)) {
                                        mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                    }
                                }
                            }
                        }
                    }
                }
                checkDirection = false;

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDirection = true;
                checkflag = false;
                if (SurveyDetailsListFragment.isCompleted) {
                    if (arrayKey.size() == 1) {
                        if (arrayKey.size() <= (viewPager.getCurrentItem())) {
                            showScore(getListFormattedForViewCreation(surveySection).get(0));
                        }
                    } else {
                        if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                            showScore(getListFormattedForViewCreation(surveySection).get(0));
                        } else {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    }
                } else if (SurveyDetailsListFragment.isSchedule) {

                    viewPager.disableScroll(false);
                    //Nik
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

                } else if (SurveyDetailsListFragment.isToDo) {
                    int currentPage = viewPager.getCurrentItem();
                    printLog("Total arraysize" + arrayKey.size());
                    printLog("currentPage" + currentPage);
                    String s = arrayKey.get(currentPage);
                    boolean a = hashmapOfKey.get(s);

                    if (hashmapOfKey.get(s)) {
                        viewPager.disableScroll(false);
                        //Nik
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                        checkflag = true;
                        status = 0;
                    } else {
                        selectSurveyDialog(hashmapOfKeyTitle.get(s));
                        viewPager.disableScroll(true);
                        checkflag = true;
                        // viewPager.beginFakeDrag();
                        status = 1;
                        //selectSurveyDialog(hashmapOfKeyTitle.get(s));
                    }
                    if (status == 0) {
                        SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                        if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(true))) {
                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                        } else if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1 && (PreferenceUtils.getESignature(getContext()).equals(false))) {
                            mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                        }
                    }
                }
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkflag = false;
                if (SurveyDetailsListFragment.isCompleted) {
                    if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {
                        showScore(getListFormattedForViewCreation(surveySection).get(0));
                    }
                    else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    }
                }
                if (SurveyDetailsListFragment.isSchedule) {


                    viewPager.disableScroll(false);
                    //Nik
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);


                }
                if (arrayKey.size() <= (viewPager.getCurrentItem()) + 1) {

                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    //showScore(getListFormattedForViewCreation(surveySection).get(0));
                } else if (SurveyDetailsListFragment.isToDo) {
                    int currentPage = viewPager.getCurrentItem();
                    printLog("Total arraysize" + arrayKey.size());
                    printLog("currentPage" + currentPage);
                    String s = arrayKey.get(currentPage);
                    if (hashmapOfKey.get(s)) {
                        viewPager.disableScroll(false);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                        checkflag = true;
                        status = 0;
                    } else {
                       // selectSurveyDialog(hashmapOfKeyTitle.get(s));

                        viewPager.disableScroll(false);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);


                        viewPager.getCurrentItem();
                        checkflag = true;
                        //viewPager.beginFakeDrag();
                        //  viewPager.disableScroll(true);
                        status = 1;
                        // selectSurveyDialog(hashmapOfKeyTitle.get(s));
                    }

                    //String net = PreferenceUtils.getESignature(getContext());
                    if (status == 0) {
                        final SurveyDetailsModel surveyDetailsModel = getListFormattedForViewCreation(surveySection).get(0);
                        if ((surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem()) + 1)) {
                            if (PreferenceUtils.getESignature(getContext()).equals(true)) {
                                mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                  /* viewPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
                                        @Override
                                        public void onSwipeOutAtStart() {
                                            printLog("swipe start");
                                        }

                                        @Override
                                        public void onSwipeOutAtEnd() {
                                            printLog("swipe end");
                                            mainActivity.changeToSurveyElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                                        }
                                    });*/
                            } else if (PreferenceUtils.getESignature(getContext()).equals(false)) {
                                mainActivity.changeToSurveyNonElectronicSubmit(surveyDetailsModel.getPatient_Survey_ResponseID(), surveyDetailsModel.getSurveyID());
                            }
                        }
                    }
                }
            }
        });
    }


    private int getNextPossibleItemIndex(int change) {

        int currentIndex = viewPager.getCurrentItem();
        int total = viewPager.getAdapter().getCount();

        if (currentIndex + change < 0) {
            return 0;
        }

        return Math.abs((currentIndex + change) % total);
    }

    public static void scrollViewPager1() {
        checkDirection = false;
    }

    public static void scrollViewPager() {
        viewPager.disableScroll(false);
    }

    public static void disableScrollViewPager() {
        viewPager.disableScroll(true);
    }

    private void showScore(SurveyDetailsModel surveyDetailsModel1) {
        viewPager.disableScroll(false);
        int currentItem = viewPager.getCurrentItem();

        printLog("current page" + currentItem);
        SurveyDetailsModel surveyDetailsModel = surveyDetailsModel1;

        if (surveyDetailsModel.getPagesQuantity() <= (viewPager.getCurrentItem() + 1)) {
            SurveyElectronicSubmitResponse surveyElectronicSubmitResponse = new SurveyElectronicSubmitResponse();
            SurveyElectronicSubmitResponse.SubmittedScoreData submitResponse = surveyElectronicSubmitResponse.new SubmittedScoreData();
            submitResponse.setMaximum_total_survey_score(surveyDetailsModel.getPatient_Total_Survey_Score());
            submitResponse.setPatient_total_survey_score(surveyDetailsModel.getPatient_Total_Survey_Score());
            submitResponse.setSubmission_datetime(surveyDetailsModel.getSubmission_DateTime());
            submitResponse.setUser(surveyDetailsModel.getSubmitting_User_FirstName() + " " + surveyDetailsModel.getSubmitting_User_LastName());
            mainActivity.SurveySubmittedProgressResult(submitResponse);
        }
    }

    /* reconstruct List for view creation*/
   // private List<SurveyDetailsModel> getListFormattedForViewCreation(List<SurveySection> surveySection) {
    private List<SurveyDetailsModel> getListFormattedForViewCreation(List<SurveySection> surveySection) {
        List<SurveyDetailsModel> detailsModels = new ArrayList<>();
        for (SurveySection section : surveySection) {
            for (QuestionModel questionModel : section.getQuestionModels()) {
               // SurveyDetailsModel detailsModel = new SurveyDetailsModel();
                SurveyDetailsModel detailsModel = new SurveyDetailsModel();
                detailsModel.setDescription(section.getDescription());
                detailsModel.setName(section.getName());
                detailsModel.setPatientSurveySectionId(section.getPatientSurveySectionId());
                detailsModel.setTypeOfSection(section.getTypeOfSection());
                detailsModel.setQuestionModel(questionModel);
                detailsModel.setPatient_Survey_ResponseID(section.getPatient_Survey_ResponseID());
                detailsModel.setPagesQuantity(surveySection.get(0).getSizeOfSurvey());
                detailsModel.setSurveyID(surveySection.get(0).getSurveyID());
                detailsModel.setPatient_Total_Survey_Score(surveySection.get(0).getPatient_Total_Survey_Score());
                detailsModel.setSubmission_DateTime(surveySection.get(0).getSubmission_DateTime());
                detailsModel.setSubmitting_User_FirstName(surveySection.get(0).getSubmitting_User_FirstName());
                detailsModel.setFirstName(surveySection.get(0).getFirstName());
                detailsModel.setLastName(surveySection.get(0).getLastName());
                detailsModel.setSubmitting_User_LastName(surveySection.get(0).getSubmitting_User_LastName());

                detailsModels.add(detailsModel);
            }
        }
        return detailsModels;
    }

   /* private void getSurveyList(int id) {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PATIENT + id;
        } else {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PROVIDER + id;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response QA:" + response);
                        if (response != null) {
                            SurveyDetailsQuestionDto questionDto = new Gson().fromJson(response.toString(), SurveyDetailsQuestionDto.class);
                            if (questionDto != null) {
                                // printLog("QuestionDto" + questionDto);
                                if (questionDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    int id = questionDto.getSurveyQuestion().getSurveyDetails().getSurveyId();
                                    final List<SurveySection> surveySection = questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection();
                                    printLog("Type: " + questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseStatus());

                                    int questionSize = 0;
                                    for (int i = 0; i < questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().size(); i++) {
                                        questionSize = questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().get(i).getQuestionModels().size() + questionSize;
                                    }

                                    for (int i = 0; i < questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().size(); i++) {
                                        surveySection.get(i).setPatient_Survey_ResponseID(questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseId());
                                        //surveySection.get(i).setSizeOfSurvey(surveySection.get(i).getQuestionModels().size());
                                        surveySection.get(i).setSizeOfSurvey(questionSize);
                                        surveySection.get(i).setSurveyID(id + "");
                                        surveySection.get(i).setPatient_Total_Survey_Score(questionDto.getSurveyQuestion().getSurveyDetails().getTotalSurveyScore());
                                        surveySection.get(i).setSubmission_DateTime(questionDto.getSurveyQuestion().getSurveyDetails().getSubmissionDateAndTime());
                                        surveySection.get(i).setSubmitting_User_FirstName(questionDto.getSurveyQuestion().getSurveyDetails().getSubmittingUserFirstName());
                                        surveySection.get(i).setSubmitting_User_LastName(questionDto.getSurveyQuestion().getSurveyDetails().getSubmittingUserLastName());
                                        surveySection.get(i).setFirstName(questionDto.getSurveyQuestion().getSurveyDetails().getFirstName());
                                        surveySection.get(i).setLastName(questionDto.getSurveyQuestion().getSurveyDetails().getLastName());
                                    }

                                    setViewPager(surveySection);
                                } else showDialogWithOkButton(questionDto.getMessage());

                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }*/


    private void getSurveyList1(int id) {
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PATIENT + id;
        } else {
            url = BASE_URL + URL_SURVEY_PLAN_QA_PROVIDER + id;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response QA:" + response);
                        if (response != null) {
                            SurveyDetailsQuestionDto questionDto = new Gson().fromJson(response.toString(), SurveyDetailsQuestionDto.class);
                            if (questionDto != null) {
                                // printLog("QuestionDto" + questionDto);
                                if (questionDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    int id = questionDto.getSurveyQuestion().getSurveyDetails().getSurveyId();
                                    final List<SurveySection> surveySection = questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection();
                                    printLog("Type: " + questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseStatus());

                                    /*if (questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseStatus().equals("Completed")) {
                                        SurveyDetailsListFragment.isToDo = false;
                                        SurveyDetailsListFragment.isCompleted = true;
                                    } else if (questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseStatus().equals("To Do")) {
                                        SurveyDetailsListFragment.isToDo = true;
                                        SurveyDetailsListFragment.isCompleted = false;
                                    } else {
                                        SurveyDetailsListFragment.isToDo = false;
                                        SurveyDetailsListFragment.isCompleted = false;
                                        SurveyDetailsListFragment.isSchedule = true;
                                    }*/

                                    int questionSize = 0;
                                    for (int i = 0; i < questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().size(); i++) {
                                        questionSize = questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().get(i).getQuestionModels().size() + questionSize;
                                    }

                                    for (int i = 0; i < questionDto.getSurveyQuestion().getSurveyDetails().getSurveySection().size(); i++) {
                                        surveySection.get(i).setPatient_Survey_ResponseID(questionDto.getSurveyQuestion().getSurveyDetails().getPatientSurveyResponseId());
                                        //surveySection.get(i).setSizeOfSurvey(surveySection.get(i).getQuestionModels().size());
                                        surveySection.get(i).setSizeOfSurvey(questionSize);
                                        surveySection.get(i).setSurveyID(id + "");
                                        surveySection.get(i).setPatient_Total_Survey_Score(questionDto.getSurveyQuestion().getSurveyDetails().getTotalSurveyScore());
                                        surveySection.get(i).setSubmission_DateTime(questionDto.getSurveyQuestion().getSurveyDetails().getSubmissionDateAndTime());
                                        surveySection.get(i).setSubmitting_User_FirstName(questionDto.getSurveyQuestion().getSurveyDetails().getSubmittingUserFirstName());
                                        surveySection.get(i).setSubmitting_User_LastName(questionDto.getSurveyQuestion().getSurveyDetails().getSubmittingUserLastName());
                                        surveySection.get(i).setFirstName(questionDto.getSurveyQuestion().getSurveyDetails().getFirstName());
                                        surveySection.get(i).setLastName(questionDto.getSurveyQuestion().getSurveyDetails().getLastName());
                                    }

                                    setViewPager1(surveySection);
                                } else showDialogWithOkButton(questionDto.getMessage());

                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }


    private void selectSurveyDialog(String title) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mainActivity);
        } else {
            builder = new AlertDialog.Builder(mainActivity);
        }
        builder.setMessage("Please answer all mandatory question marked with asterisk(*)")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setTitle(title);
        final AlertDialog dialog = builder.create();
        dialog.show();

        viewPager.setOffscreenPageLimit(0);
        viewPager.disableScroll(true);
        /*handler.removeCallbacks(Update);
        handler.post(Update);*/


        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
        positiveButton.setLayoutParams(positiveButtonLL);

    }


}
