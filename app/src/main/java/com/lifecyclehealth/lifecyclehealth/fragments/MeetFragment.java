package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.lifecyclehealth.lifecyclehealth.model.MeetUser;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.PreferenceUtils;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.common.model.User;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_LIST;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_START;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.notificationCount;


public class MeetFragment extends BaseFragmentWithOptions implements View.OnClickListener {
    private MainActivity mainActivity;
    private HorizontalCalendar horizontalCalendar;
    private RecyclerView recyclerView;
    private TextView dateDisplayTextView, emptyViewTv;
    public static String selectedDate = null;
    Button btnScheduleMeet;
    String messageCount, notificationCount;
    private ImageView imageViewMessage,imageViewNotification;
    TextView countTextViewMessage, notificationCountTextViewMessage;
    private boolean isPatient;
    static View view1;


    @Override
    String getFragmentTag() {
        return "MeetFragment";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatClient.initialize(mainActivity.getApplication());
    }


    public static MeetFragment newInstance() {
        MeetFragment meetFragment = new MeetFragment();
        return meetFragment;
    }

    public MeetFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_meet, parent, false);

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);

    }


    private void initializeView(View view) {
        try{
            MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_IN_MEET_FRAGMENT, true);
            messageCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.messageCount);
            notificationCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);
            Analytics.with(getContext()).screen("Calendar Meet");

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            setupToolbarTitle(toolbar, getString(R.string.title_meet));

            isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
            RelativeLayout notificationHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.notificationHolder);
            RelativeLayout messageHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.messageHolder);
            countTextViewMessage = (TextView) view.findViewById(R.id.countTextViewMessage);
            notificationCountTextViewMessage = (TextView) view.findViewById(R.id.countTextViewNotificatione);
            imageViewMessage = (ImageView) view.findViewById(R.id.imageViewMessage);
            imageViewNotification = (ImageView) view.findViewById(R.id.imageViewNotification);
            //get Notification count

            new MeetThread().run();

            notificationHolderLayout.setOnClickListener(this);
            messageHolderLayout.setOnClickListener(this);

            dateDisplayTextView = (TextView) view.findViewById(R.id.selectedDateTv);
            emptyViewTv = (TextView) view.findViewById(R.id.emptyViewTv);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
            recyclerView.hasFixedSize();
            btnScheduleMeet = (Button) view.findViewById(R.id.btnScheduleMeet);
            btnScheduleMeet.setOnClickListener(this);
            if (MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT)) {
                btnScheduleMeet.setVisibility(View.GONE);
            } else {
                btnScheduleMeet.setVisibility(View.VISIBLE);
            }
            view1 = view;
            setupCalendar(view);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    class MeetThread extends Thread{
        @Override
        public void run() {
            super.run();
            getNotificationCount(notificationCountTextViewMessage, mainActivity);
            changeMessageIcon();
        }
    }

    public void changeMessageIcon(){
        if (!messageCount.equals("0")) {
            countTextViewMessage.setText(messageCount);
        } else {
            countTextViewMessage.setVisibility(View.GONE);
        }
        if (!notificationCount.equals("0")) {
            notificationCountTextViewMessage.setText(notificationCount);
        } else {
            notificationCountTextViewMessage.setVisibility(View.GONE);
        }

        if (mainActivity.chatList.size() > 0) {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else if (mainActivity.meetListMessage.size() > 0) {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
        }
        if (!notificationCount.equals("0")) {
            imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
        }
    }

    /*Refresh meet*/
    public void getRefreshMeetFromNotification() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String todayDate = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH).format(new Date());
                if (todayDate.equals(selectedDate)) {
                    getAllMeetData(selectedDate);
                }
            }
        });

    }

    Handler mHandler;

    void workerThread() {
        // And this is how you call it from the worker thread:
        Message message = mHandler.obtainMessage(0);
        message.sendToTarget();
    }


    private void getAllMeetData(String selectedDate) {
        showProgressDialog(true);

        if (isConnectedToNetwork(mainActivity)) {
            try {

                TimeZone tz = TimeZone.getDefault();
                Date now = new Date();
                int offsetFromUtc = (tz.getOffset(now.getTime()) / 1000) * (-1);

                MeetUser meetUser = new MeetUser();
                meetUser.setDays("1");
                meetUser.setOffSet(offsetFromUtc + "");
                meetUser.setStart(selectedDate);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(meetUser));

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_LIST, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get all Meet:" + response);

                        if (response != null) {
                            recyclerView.setAdapter(null);
                            MeetListDTO meetListDTO = new Gson().fromJson(response.toString(), MeetListDTO.class);
                            if (meetListDTO != null) {
                                if (meetListDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    showEmptyView(false);
                                    recyclerView.setAdapter(null);
                                    setRecyclerView(meetListDTO);//setupList(getHashMapFromService(surveyPlanDto));
                                } else {
                                    showEmptyView(true);
                                    resetRecycleView();
                                    //showDialogWithOkButton(meetListDTO.getMessage());
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

    /* for clearing recycleview*/
    private void resetRecycleView() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
    }

    /* for aplying adapter list*/
    private void setRecyclerView(MeetListDTO meetListDTO) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        MeetAdapter meetAdapter = new MeetAdapter(meetListDTO.getMeetList(), mainActivity, new MeetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetListDTO.MeetList meetList, String Type, String pos) {

                switch (Type) {
                    case "M": {
                        callStartMeet(SESSION_KEY);
                        if ((meetList.getStatus().equals("SESSION_STARTED") && meetList.isSelf_hosted() == true) || meetList.isSelf_hosted() == false) {
                            MyApplication.getInstance().addToSharedPreference(SESSION_KEY, meetList.getSession_key());
                            Analytics.with(getContext()).track("Join eVisit", new Properties().putValue("category", "Mobile")
                                    .putValue("meetId", meetList.getSession_key()));

                            MeetEventActivity.joinMeet(mainActivity);

                        } else if (meetList.getStatus().equals("SESSION_SCHEDULED") && meetList.isSelf_hosted() == true) {
                            MyApplication.getInstance().addToSharedPreference(SESSION_KEY, meetList.getSession_key());
                            ArrayList<User> userList = new ArrayList<>();
                            //userList.addAll(chat.getMembers());
                            Analytics.with(getContext()).track("Start eVisit", new Properties().putValue("category", "Mobile")
                                    .putValue("meetId", meetList.getSession_key()));

                            String topic = meetList.getTopic();
                            MeetEventActivity.startMeet(mainActivity, topic, userList);
                        }
                        break;
                    }

                    case "D": {
                        String data = new Gson().toJson(meetList);
                        Analytics.with(getContext()).track("meetDetails", new Properties().putValue("category", "Mobile")
                                .putValue("meetID", meetList.getSession_key())
                                .putValue("meetDetailsDict", data));
                        if (isPatient) {
                            mainActivity.changeToMeetDetailsPatient(data);
                        } else {
                            mainActivity.changeToMeetDetailsProvider(data);
                        }
                        break;
                    }
                }
            }
        });

        recyclerView.setAdapter(meetAdapter);

    }

    private void callStartMeet(String sessionKey) {
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_START + sessionKey, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get all Meet:" + response);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                    }
                });
            } catch (Exception e) {

            }
        } else {
            showProgressDialog(false);
            showNoNetworkMessage();
        }
    }

    /* For showing empty view when there is no data*/
    private void showEmptyView(boolean flag) {
        if (flag) emptyViewTv.setVisibility(View.VISIBLE);
        else emptyViewTv.setVisibility(View.GONE);
    }

    private void setUpCurrentDate(Date date) {
        dateDisplayTextView.setText(new SimpleDateFormat(" ", Locale.ENGLISH).format(date.getTime()));
    }

    private void setupCalendar(View view) {
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            if (selectedDate != null) {
                Date d = dateFormat.parse(selectedDate);
                currentCalendar.setTime(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        printLog("Current Month" + currentMonth);
        setUpCurrentDate(currentCalendar.getTime());

        /** current Month*/
        Calendar startDate = Calendar.getInstance();
        try {
            if (selectedDate != null) {
                Date d = dateFormat.parse(selectedDate);
                currentCalendar.setTime(d);
            } else {
                selectedDate = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH).format(new Date());
                startDate.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startDate.set(currentCalendar.DAY_OF_MONTH, -30);

        //printLog("Start" + startDate.get(Calendar.MONTH) + startDate.get(Calendar.DAY_OF_MONTH));
        /**
         * end after 1 month from now
         */
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate.getTime());
        endDate.add(currentCalendar.DATE, 60);

        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setupToolbarTitle(toolbar, getString(R.string.title_message));
        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(14f, 24f, 14f)
                .showDayName(true)
                .showMonthName(false)
                .textColor(Color.LTGRAY, Color.BLACK)
                .selectedCalendarDate(currentCalendar)
                .selectorColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                printLog("dateFormat" + DateFormat.getDateInstance().format(date));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setUpCurrentDate(calendar.getTime());
                selectedDate = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH).format(horizontalCalendar.getSelectedDate());
                getAllMeetData(new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH).format(horizontalCalendar.getSelectedDate()));
            }

        });


        getAllMeetData(selectedDate);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedDate != null) {
            recyclerView.setAdapter(null);
            getAllMeetData(selectedDate);
        }
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_IN_MEET_FRAGMENT, true);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    //mainActivity.finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_IN_MEET_FRAGMENT, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScheduleMeet: {
                mainActivity.changeToScheduleMeet();
                break;
            }
            case R.id.notificationHolder:
                getDialogSetup(mainActivity, LEFT, notificationCountTextViewMessage);

                break;
            case R.id.messageHolder:
                getDialogSetup(mainActivity, RIGHT, countTextViewMessage);
                break;
        }
    }
}