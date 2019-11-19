package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomExpandableListAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.SurveyPlanDto;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_COMPLETED;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_SCHEDULE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_TODO;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_PLAN_Provider;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.notificationCount;

public class SurveyFragment extends BaseFragmentWithOptions {
    private static MainActivity mainActivity;
    boolean isPatient;
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;
    private LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail;
    List<String> titleList;
    static ImageView imageViewMessage, imageViewNotification;
    static TextView notificationCountTextViewMessage, countTextViewMessage;
    public static int expandedGroup = 0;
    String messageCount, notificationCount;
    public ColorCode colorCode;
    String Stringcode = "";
    // ProgressDialog pDialog;

    @Override
    String getFragmentTag() {
        return "ToolTipRelativeLayout";
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file_show for the fragment
        return inflater.inflate(R.layout.fragment_survey, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        initializeView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
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
                    //Toast.makeText(getContext(),"back_arrow key pressed",Toast.LENGTH_SHORT).show();
                    //mainActivity.finish();
                    return true;

                }

                return false;
            }
        });
    }

    private void initializeView(View view) {
        //try {
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcode = "";
        String hashcode = "";
        // pDialog = new ProgressDialog(getActivity());

        if (demo == null) {
            hashcode = "Green";
            Stringcode = "259b24";
        } else if (demo != null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();

            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
        /*} catch (Exception e) {
            e.printStackTrace();
        }*/
        messageCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.messageCount);
        notificationCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);

        Analytics.with(view.getContext()).screen("Survey");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        //else {
        newsetupToolbarTitle(toolbar, getString(R.string.title_survey), colorCode.getVisualBrandingPreferences().getColorPreference());
        //}
//        mToolTipFrameLayout = (ToolTipRelativeLayout) view.findViewById(R.id.toolTipFrameLayout);

        RelativeLayout notificationHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.notificationHolder);
        RelativeLayout messageHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.messageHolder);
        countTextViewMessage = (TextView) view.findViewById(R.id.countTextViewMessage);
        notificationCountTextViewMessage = (TextView) view.findViewById(R.id.countTextViewNotificatione);
        imageViewMessage = (ImageView) view.findViewById(R.id.imageViewMessage);

        imageViewNotification = (ImageView) view.findViewById(R.id.imageViewNotification);

        if (!messageCount.equals("0")) {
            countTextViewMessage.setText(messageCount);
            //imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
        } else {
            countTextViewMessage.setVisibility(View.GONE);
        }


        if (mainActivity.chatList.size() > 0) {
            //imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
        } else if (mainActivity.meetListMessage.size() > 0) {
            //imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
        } else {
            imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
            //imageViewMessage.setColorFilter(mainActivity.getResources().getColor(R.color.uvv_gray));

        }
        if (!notificationCount.equals("0")) {
            //imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            imageViewNotification.setColorFilter(Color.parseColor("#" + Stringcode));
            notificationCountTextViewMessage.setText(notificationCount);
        } else {
            notificationCountTextViewMessage.setVisibility(View.GONE);
            //imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
            imageViewNotification.setColorFilter(Color.parseColor("#" + Stringcode));
        }

        //get Notification count
        getNotificationCount(notificationCountTextViewMessage, mainActivity);

        notificationHolderLayout.setOnClickListener(onClickListener);
        messageHolderLayout.setOnClickListener(onClickListener);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        // for right indicator work
        DisplayMetrics metrics = new DisplayMetrics();
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }

        titleList = getSurveyTitleList();
        expandableListDetail = new LinkedHashMap<String, List<PatientSurveyItem>>();
        expandableListDetail = getExpandableListDetail(expandableListDetail, titleList);
        setupList(expandableListDetail);
        getSurveyList();
    }

    public void changeMessageIcon() {
        try {

            //try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcodes = "";
            String hashcode = "";

            if (demo == null) {
                hashcode = "Green";
                Stringcode = "259b24";
            } else if (demo != null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
              /*  }

                else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
          /*  } catch (Exception e) {
                e.printStackTrace();
            }*/

            if (mainActivity.chatList.size() > 0) {
                //imageViewMessage.setColorFilter(mainActivity.getResources().getColor(R.color.colorPrimary));
                imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
            } else if (mainActivity.meetListMessage.size() > 0) {
                //imageViewMessage.setColorFilter(mainActivity.getResources().getColor(R.color.colorPrimary));
                imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
            } else {
                //imageViewMessage.setColorFilter(mainActivity.getResources().getColor(R.color.uvv_gray));
                imageViewMessage.setColorFilter(Color.parseColor("#" + Stringcode));
            }

            notificationCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);
            if (!notificationCount.equals("0")) {
                //imageViewNotification.setColorFilter(mainActivity.getResources().getColor(R.color.colorPrimary));
                imageViewNotification.setColorFilter(Color.parseColor("#" + Stringcode));
                notificationCountTextViewMessage.setVisibility(View.VISIBLE);
                notificationCountTextViewMessage.setText(notificationCount);
            } else {
                notificationCountTextViewMessage.setVisibility(View.GONE);
                //imageViewNotification.setColorFilter(mainActivity.getResources().getColor(R.color.uvv_gray));
                imageViewNotification.setColorFilter(Color.parseColor("#" + Stringcode));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /* for setting view before service call*/
    private LinkedHashMap<String, List<PatientSurveyItem>> getExpandableListDetail(LinkedHashMap<String, List<PatientSurveyItem>> listLinkedHashMap, List<String> titleList) {
        for (String s : titleList) {
            listLinkedHashMap.put(s, new ArrayList<PatientSurveyItem>());
        }
        return listLinkedHashMap;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.notificationHolder:
                    //Analytics.with(getContext()).track("Read notification from NotificaitonPopUp", new Properties().putValue("category", "Mobile"));
                    getDialogSetup(mainActivity, LEFT, notificationCountTextViewMessage);
                    break;
                case R.id.messageHolder:
                    //Analytics.with(getContext()).track("Read message from MessageNotificaitonPopUp", new Properties().putValue("category", "Mobile"));
                    getDialogSetup(mainActivity, RIGHT, countTextViewMessage);
                    break;
            }
        }
    };

    /* For adding title in list before server call*/
    private List<String> getSurveyTitleList() {
        List<String> strings = new ArrayList<>();
        strings.add(CONST_SURV_LIST_TODO);
        strings.add(CONST_SURV_LIST_SCHEDULE);
        strings.add(CONST_SURV_LIST_COMPLETED);
        return strings;
    }

    String data = null;
    String title = null;

    /* Setting List*/
    private void setupList(final LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail) {
        data = null;
        expandableListAdapter = new CustomExpandableListAdapter(mainActivity, titleList, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Analytics.with(getContext()).track("Explore Survey", new Properties().putValue("category", "Mobile"));
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        expandableListView.expandGroup(expandedGroup);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                List<PatientSurveyItem> listConverted;
                printLog("ExpandableList:" + expandableListDetail.get(CONST_SURV_LIST_TODO));

                switch (groupPosition) {
                    case 0:
                        title = CONST_SURV_LIST_TODO;
                        expandedGroup = 0;
                        listConverted = expandableListDetail.get(CONST_SURV_LIST_TODO);
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_TODO));
                        break;
                    case 1:
                        title = CONST_SURV_LIST_SCHEDULE;
                        expandedGroup = 1;
                        listConverted = expandableListDetail.get(CONST_SURV_LIST_SCHEDULE);
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_SCHEDULE));
                        break;
                    case 2:
                        title = CONST_SURV_LIST_COMPLETED;
                        expandedGroup = 2;
                        listConverted = expandableListDetail.get(CONST_SURV_LIST_COMPLETED);
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_COMPLETED));
                        break;
                    default:
                        title = CONST_SURV_LIST_TODO;
                        expandedGroup = 0;
                        listConverted = expandableListDetail.get(CONST_SURV_LIST_TODO);
                        data = new Gson().toJson(expandableListDetail.get(CONST_SURV_LIST_TODO));

                }
                printLog("DataSending In fragment:" + data);
                if (listConverted.size() == 1) {

                    if (listConverted.get(0).getPatientSurveyResponseId() != -99) {
                        mainActivity.changeToSurveyDetailsView(data, childPosition, title);

                    }

                } else {
                    Analytics.with(getContext()).track("Survey Selected", new Properties().putValue("category", "Mobile"));
                    mainActivity.changeToSurveyDetailsView(data, childPosition, title);
                }
                return false;
            }
        });
    }

    private void getSurveyList() {
        //pDialog.show();
        showProgressDialog(true);
        String url;
        if (isPatient) {
            url = BASE_URL + URL_SURVEY_PLAN_PATIENT;
        } else {
            url = BASE_URL + URL_SURVEY_PLAN_Provider;
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        // pDialog.dismiss();
                        showProgressDialog(false);
                        printLog("Response Of Send code:" + response);

                        if (response != null) {
                            SurveyPlanDto surveyPlanDto = new Gson().fromJson(response.toString(), SurveyPlanDto.class);
                            if (surveyPlanDto != null) {
                                if (surveyPlanDto.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (surveyPlanDto.getSurveyPlan().getSurveyPlanCompleted() == null) {
                                        ArrayList<PatientSurveyItem> surveyItems = new ArrayList<PatientSurveyItem>();
                                        PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setName("No item to display");
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setPatientSurveyResponseId(-99);
                                        surveyItems.add(patientSurveyItem);
                                        surveyPlanDto.getSurveyPlan().setSurveyPlanCompleted(surveyItems);
                                    }
                                    if (surveyPlanDto.getSurveyPlan().getSurveyPlanSchedule() == null) {
                                        ArrayList<PatientSurveyItem> surveyItems = new ArrayList<PatientSurveyItem>();
                                        PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setName("No item to display");
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setPatientSurveyResponseId(-99);
                                        surveyItems.add(patientSurveyItem);
                                        surveyPlanDto.getSurveyPlan().setSurveyPlanSchedule(surveyItems);
                                    }
                                    if (surveyPlanDto.getSurveyPlan().getSurveyPlanTodo() == null) {
                                        ArrayList<PatientSurveyItem> surveyItems = new ArrayList<PatientSurveyItem>();

                                        PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setName("No item to display");
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItem.setPatientSurveyResponseId(-99);
                                        surveyItems.add(patientSurveyItem);

                                        surveyPlanDto.getSurveyPlan().setSurveyPlanTodo(surveyItems);
                                    }
                                    setupList(getHashMapFromService(surveyPlanDto));
                                } else {
                                    showDialogWithOkButton(surveyPlanDto.getMessage());
                                }
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

    private LinkedHashMap<String, List<PatientSurveyItem>> getHashMapFromService(SurveyPlanDto surveyPlanDto) {
        LinkedHashMap<String, List<PatientSurveyItem>> listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put(titleList.get(0), surveyPlanDto.getSurveyPlan().getSurveyPlanTodo());
        listLinkedHashMap.put(titleList.get(1), surveyPlanDto.getSurveyPlan().getSurveyPlanSchedule());
        listLinkedHashMap.put(titleList.get(2), surveyPlanDto.getSurveyPlan().getSurveyPlanCompleted());
        return listLinkedHashMap;
    }


}