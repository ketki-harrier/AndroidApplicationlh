package com.lifecyclehealth.lifecyclehealth.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.custome.Session;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.lifecyclehealth.lifecyclehealth.fragments.LogOutFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.MeetDetailsForPatientFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.MeetDetailsForProviderFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.MeetFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.MessageFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.MoreOptionsFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.NotificationFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.PatientDiaryFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SampleFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.ScheduleMeet;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsListFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyElectronicSubmit;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyReports;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveySubmittedProgressResult;
import com.lifecyclehealth.lifecyclehealth.fragments.TreatmentFragment;
import com.lifecyclehealth.lifecyclehealth.gcm.GcmRegistrationService;
import com.lifecyclehealth.lifecyclehealth.model.MeetUser;
import com.lifecyclehealth.lifecyclehealth.model.MessageDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageMeetModel;
import com.lifecyclehealth.lifecyclehealth.model.NotificationDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.model.SurveyElectronicSubmitResponse;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.services.Time_out_services;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.MultipartRequest;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.lifecyclehealth.lifecyclehealth.utils.PreferenceUtils;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.chat.repo.ChatRepo;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.common.BaseRepo;
import com.moxtra.sdk.meet.model.Meet;
import com.moxtra.sdk.meet.model.MeetDetail;
import com.moxtra.sdk.meet.repo.MeetRepo;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import zemin.notification.NotificationBuilder;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationRemote;
import zemin.notification.NotificationView;
import zemin.notification.NotificationViewCallback;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_LIST;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_GET_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_GET_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.messageCount;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.notificationCount;


public class MainActivity extends BaseActivity {

    //private List<SurveyModel> modelList;
    public NetworkRequestUtil networkRequestUtil;
    public MultipartRequest multipartRequest;
    private static final String TAG = "Moxta initilisation";
    private static MainActivity mInstance;
    final String moxtra_access_token = MyApplication.getInstance().getFromSharedPreference(AppConstants.Moxtra_Access_Token);
    //moxtra for prodution
    //String BASE_DOMAIN = "www.moxtra.com";
    //moxtra for test
    String BASE_DOMAIN = "sandbox.moxtra.com";
    boolean isPatient;
    public static ChatClientDelegate mChatClientDelegate;
    public static ChatRepo mChatRepo;
    public static MeetRepo mMeetRepo;
    public static List<Chat> chatList = new ArrayList<>();
    public static NotificationDialogResponse notificationDialogResponse = new NotificationDialogResponse();
    //public static MeetListDTO meetList;
    public static ArrayList<MessageMeetModel.MeetList> meetListMessage = new ArrayList<>();
    public static List<Meet> meetLists = new ArrayList<>();
    // public static String messageCount = "0";
    public static BottomBar bottomBar, bottomBarCaregiver;
    public static boolean meetPushNotification = false;


    String value = "0";

    @Override
    String getTag() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        printLog("onCreate");
        initialiseData();
    }

    private void initialiseData() {
        mInstance = this;

        networkRequestUtil = new NetworkRequestUtil(this);
        multipartRequest = new MultipartRequest(this);
        start();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_LOGINACTIVITY_ALIVE, false);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_IN_MEET, false);
       /* initialiseMoxtra();
        getMessageCount();*/
        //setupBottomBar();
        Bundle extras = getIntent().getExtras();
        value = extras.getString("from_notification");
        if (value.trim().equals("1")) {
            setupBottomBar();
        } else {
            new InitThread().run();
        }
    }

    class InitThread extends Thread{
        @Override
        public void run() {
            super.run();
            initialiseMoxtra();
            getMessageCount();
        }
    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    public void callMessageCountService() {
        getMessageCountForMessage();
    }


    private void getMessageCountForMessage() {

        MyApplication.getInstance().addToSharedPreference(messageCount, "0");

        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_GET_CONVERSATION, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        printLog("Response Of Message list:" + response);
                        if (response != null) {
                            MessageDialogResponse messageEpisodeListResponse = new Gson().fromJson(response.toString(), MessageDialogResponse.class);

                            if (messageEpisodeListResponse != null) {
                                if (messageEpisodeListResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    //messageCount = messageEpisodeListResponse.getUnread_feeds();
                                    MyApplication.getInstance().addToSharedPreference(messageCount, messageEpisodeListResponse.getUnread_feeds());

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    if (fragmentManager != null && fragmentManager.getFragments() != null) {
                                        for (Fragment fragment : fragmentManager.getFragments()) {
                                            if (fragment != null && fragment.isVisible() && fragment instanceof SurveyFragment) {
                                                ((SurveyFragment) fragment).changeMessageIcon();
                                            }
                                            if (fragment != null && fragment.isVisible() && fragment instanceof MeetFragment) {
                                                ((MeetFragment) fragment).changeMessageIcon();
                                            }
                                        }
                                    }

                                }

                            } else {
                                //messageCount = "0";
                                MyApplication.getInstance().addToSharedPreference(messageCount, "0");

                                //showDialogWithOkButton(messageEpisodeListResponse.getMessage());
                            }
                        }
                        //showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (Exception e) {
                setupBottomBar();
            }
        } else {

            //showNoNetworkMessage();
        }
        getNotificationCount();

    }

    @Override
    protected void onStart() {
        super.onStart();
        printLog("onStart");
    }

    private void initialiseMoxtra() {
        try {
            showProgressDialog(true);
            mChatClientDelegate = null;
            mChatRepo = null;
            mMeetRepo = null;
            ChatClient.linkWithAccessToken(moxtra_access_token, BASE_DOMAIN,
                    new ApiCallback<ChatClientDelegate>() {
                        @Override
                        public void onCompleted(ChatClientDelegate ccd) {
                            Log.i(TAG, "Linked to Moxtra account successfully.");
                            //MyApplication.getInstance().addToSharedPreference(GCM_token, "");
                            Intent intent = new Intent(getApplicationContext(), GcmRegistrationService.class);
                            startService(intent);

                            mChatClientDelegate = ChatClient.getClientDelegate();
                            if (mChatClientDelegate == null) {
                                Log.e(TAG, "Unlinked, ChatClient is null.");
                                return;
                            }
                            //showProgressDialog(false);
                            new asyncCreateText().execute();

                            PreferenceUtils.saveUser(getApplicationContext(), moxtra_access_token);
                            // startChatListActivity();
                        }

                        @Override
                        public void onError(int errorCode, String errorMsg) {
                            showProgressDialog(false);
                            //Toast.makeText(getApplicationContext(), "Failed to link to Moxtra account.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Failed to link to Moxtra account, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                            // showProgress(false);
                        }
                    });
        } catch (Exception e) {
            e.getMessage();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private class asyncCreateText extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog(true);
        }

        @Override
        protected Void doInBackground(Void... unused) {

            try {
                meetLists = new ArrayList<>();
                mChatRepo = mChatClientDelegate.createChatRepo();
                mMeetRepo = mChatClientDelegate.createMeetRepo();
                chatList = mChatRepo.getList();
                sortData();


                ApiCallback<List<Meet>> mMeetListApiCallback = new ApiCallback<List<Meet>>() {
                    @Override
                    public void onCompleted(List<Meet> meets) {
                        Log.d(TAG, "FetchMeets: onCompleted");
                        //meetList = meets;
                        for (Meet meet : meets) {
                            if (!isEnded(meet)) {
                                /*if (!meet.isAccepted())
                                    meetLists.add(meet);*/
                            }
                        }

                        printLog("meet list: " + meetLists.size());
                        for (Meet meet : meetLists) {
                          /*  printLog("topic" + meet.getTopic());
                            printLog("isInProgress" + meet.isInProgress());
                            printLog("getScheduleStartTime" + meet.getScheduleStartTime());
                            printLog("meetId" + meet.getID());
                            printLog("isAccepted" + meet.isAccepted());*/
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.d(TAG, "FetchMeets: onError");
                    }
                };

                mMeetRepo.fetchMeets(mMeetListApiCallback);
                mMeetRepo.setOnChangedListener(new BaseRepo.OnRepoChangedListener<Meet>() {
                    @Override
                    public void onCreated(List<Meet> items) {
                        // Log.d(TAG, "Meet: onCreated");
                        for (Meet meet : items) {
                            if (!isEnded(meet)) {
                                if (!meet.isAccepted())
                                    meetLists.add(meet);
                            }
                        }
                    }

                    @Override
                    public void onUpdated(List<Meet> items) {
                        /*for (Meet meet : items) {
                            if (!isEnded(meet)) {
                                if (!meet.isAccepted())
                                    meetLists.add(meet);
                            }
                        }*/
                    }

                    @Override
                    public void onDeleted(List<Meet> items) {
                        Log.d(TAG, "Meet: onDeleted");
                        for (Meet meet : items) {
                            if (!isEnded(meet)) {
                               /* if (!meet.isAccepted())
                                    meetLists.add(meet);*/
                            }
                        }
                    }
                });
            } catch (Exception e) {
                printLog("error " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //printLog("complete data");
            showProgressDialog(false);
            SurveyFragment surveyFragment = new SurveyFragment();
            surveyFragment.changeMessageIcon();
        }
    }


    private static boolean isEnded(Meet meet) {
        boolean meetStatus;
        meetStatus = !meet.isInProgress() && !(meet.getScheduleStartTime() > 0 && System.currentTimeMillis() < meet.getScheduleEndTime());
        return meetStatus;
    }


    private void sortData() {

        Collections.sort(chatList, new Comparator<Chat>() {

            @Override
            public int compare(Chat arg0, Chat arg1) {
                SimpleDateFormat format = new SimpleDateFormat("KK:mm aa yyyy-MM-dd");

                int compareResult = 0;
                try {
                    String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg0.getLastFeedTimeStamp());
                    String s1 = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg1.getLastFeedTimeStamp());
                    Date arg0Date = format.parse(s);
                    Date arg1Date = format.parse(s1);
                    compareResult = arg1Date.compareTo(arg0Date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return compareResult;
            }
        });
    }


    private void getAllMeetData() {
        // showProgressDialog(true);

        if (isConnectedToNetwork(this)) {
            try {

                TimeZone tz = TimeZone.getDefault();
                Date now = new Date();
                int offsetFromUtc = (tz.getOffset(now.getTime()) / 1000) * (-1);

                MeetUser meetUser = new MeetUser();
                meetUser.setDays("1");
                meetUser.setOffSet(offsetFromUtc + "");
                meetUser.setStart(new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH).format(new Date()));

                final JSONObject requestJson = new JSONObject(new Gson().toJson(meetUser));

                networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_LIST, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        //showProgressDialog(false);
                        printLog("Response Of get all Meet:" + response);

                        if (response != null) {
                            MeetListDTO meetListDTO = new Gson().fromJson(response.toString(), MeetListDTO.class);


                            if (meetListDTO != null) {
                                if (meetListDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                             /*     meetList = new MeetListDTO();
                                    meetList = meetListDTO;
                                    meetListMessage = new ArrayList<MessageMeetModel.MeetList>();
                                    for (int i = (meetListDTO.getMeetList().size() - 1); i >= 0; i--) {
                                        if (meetListDTO.getMeetList().get(i).getStatus().contains("SESSION_STARTED")) {
                                            printLog("add" + meetList.getMeetList().get(i).getStatus() + "  " + i);
                                            MessageMeetModel.MeetList meet = new MessageMeetModel().new MeetList();
                                            meet.setTopic(meetList.getMeetList().get(i).getTopic());
                                            meet.setStatus(meetList.getMeetList().get(i).getStatus());
                                            meet.setStarts(meetList.getMeetList().get(i).getStarts());
                                            meet.setSession_key(meetList.getMeetList().get(i).getSession_key());
                                            meetListMessage.add(meet);

                                        } else {
                                            printLog("remove" + meetList.getMeetList().get(i).getStatus() + "  " + i);
                                            meetList.getMeetList().remove(i);
                                        }
                                    }
*/
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                    }
                });
            } catch (Exception e) {
            }
        }


    }


    /* Set up bottom Navigation Bar*/
    private void setupBottomBar() {
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBarCaregiver = (BottomBar) findViewById(R.id.bottomBar_Caregiver);

        if (isPatient) {
            bottomBar.setVisibility(View.VISIBLE);
            bottomBarCaregiver.setVisibility(View.GONE);
        } else {
            bottomBar.setVisibility(View.GONE);
            bottomBarCaregiver.setVisibility(View.VISIBLE);
        }
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (value.trim().equals("1")) {
                    openFragment(R.id.tab_message);
                } else {
                    openFragment(tabId);
                }
            }
        });
        bottomBarCaregiver.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (value.trim().equals("1")) {
                    value = "0";
                    openFragment(R.id.tab_message);
                    bottomBar.selectTabWithId(R.id.tab_message);
                    bottomBarCaregiver.selectTabWithId(R.id.tab_message);
                } else {
                    openFragment(tabId);
                }
            }
        });


        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                printLog("Tab Id:" + tabId);
                openFragment(tabId);
            }
        });

        bottomBarCaregiver.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                printLog("Tab Id:" + tabId);
                openFragment(tabId);
            }
        });

    }



   /*// Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    private void openFragment(int tabId) {
        Fragment fragment = null;

        switch (tabId) {
            case R.id.tab_survey:
                fragment = new SurveyFragment();
                break;
            /*case R.id.tab_patientDiary:
                fragment = new PatientDiaryFragment();
                break;*/
            case R.id.tab_message:
                fragment = new MessageFragment();
                break;
            case R.id.tab_meet:
                fragment = new MeetFragment();
                break;
            case R.id.tab_more:
                fragment = new MoreOptionsFragment();
                break;
         /*   case R.id.tab_episode_list:
                fragment = new TreatmentFragment();
                break;*/
        }
        if (fragment != null)

            replaceFragment(fragment);

    }


    /*get message count*/

    private void getMessageCount() {

        MyApplication.getInstance().addToSharedPreference(messageCount, "0");

        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_GET_CONVERSATION, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        printLog("Response Of Message list:" + response);
                        if (response != null) {
                            MessageDialogResponse messageEpisodeListResponse = new Gson().fromJson(response.toString(), MessageDialogResponse.class);

                            if (messageEpisodeListResponse != null) {
                                if (messageEpisodeListResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    //messageCount = messageEpisodeListResponse.getUnread_feeds();
                                    MyApplication.getInstance().addToSharedPreference(messageCount, messageEpisodeListResponse.getUnread_feeds());
                                    setupBottomBar();
                                } else {
                                    //messageCount = "0";
                                    MyApplication.getInstance().addToSharedPreference(messageCount, "0");
                                    setupBottomBar();
                                }

                            } else {
                                //messageCount = "0";
                                MyApplication.getInstance().addToSharedPreference(messageCount, "0");
                                setupBottomBar();
                                //showDialogWithOkButton(messageEpisodeListResponse.getMessage());
                            }
                        } else {
                            setupBottomBar();
                        }
                        //showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                    }

                    @Override
                    public void onError(VolleyError error) {
                        setupBottomBar();
                    }
                });
            } catch (Exception e) {
                setupBottomBar();
            }
        } else {
            setupBottomBar();
            //showNoNetworkMessage();
        }
        getNotificationCount();
    }

    private void getNotificationCount() {
        MyApplication.getInstance().addToSharedPreference(notificationCount, "0");
        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_GET_NOTIFICATION, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        printLog("Response Of Notification list:" + response);
                        if (response != null) {
                            notificationDialogResponse = new Gson().fromJson(response.toString(), NotificationDialogResponse.class);

                            if (notificationDialogResponse != null) {
                                if (notificationDialogResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    //messageCount = messageEpisodeListResponse.getUnread_feeds();
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, notificationDialogResponse.getAlertList().get(0).getNewAlertCount());
                                } else {
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, "0");
                                }
                                SurveyFragment surveyFragment = new SurveyFragment();
                                surveyFragment.changeMessageIcon();

                            } else {
                                SurveyFragment surveyFragment = new SurveyFragment();
                                surveyFragment.changeMessageIcon();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (Exception e) {
            }
        } else {
            //showNoNetworkMessage();
        }
    }


    /* gives survey details view is showing or not*/
    private boolean isDetailsViewVisible() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("com.lifecyclehealth.lifecyclehealth.fragments.SurveyDetailsListFragment");
        return (fragment != null && fragment.isVisible());
    }

    /* for changing fragments on click of bottom bar*/
    protected void replaceFragment(Fragment fragment) {
        MeetFragment.selectedDate = null;
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
            // fm.popBackStackImmediate();
        }
        String fragmentTag = fragment.getClass().getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    protected void replaceFragmentNotificationMessage(Fragment fragment) {
        MeetFragment.selectedDate = null;
        String fragmentTag = fragment.getClass().getName();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    /* Change fragment for survey Details view*/
    public void changeToSurveyDetailsView(String data, int position, String title) {
        Fragment fragment = SurveyDetailsListFragment.newInstance(data, title, position);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("SurveyDetailsListFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void changeToMeetDetailsProvider(String data) {
        Fragment fragment = MeetDetailsForProviderFragment.newInstance(data);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("MeetDetailsForProviderFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void changeToMeetDetailsPatient(String data) {
        Fragment fragment = MeetDetailsForPatientFragment.newInstance(data);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("MeetDetailsForPatientFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void changeToSurveyElectronicSubmit(String data, String surveyId) {
        Fragment fragment = SurveyElectronicSubmit.newInstance(data, surveyId);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("SurveyElectronicSubmit")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void SurveySubmittedProgressResult(SurveyElectronicSubmitResponse.SubmittedScoreData submittedScoreData) {
        Fragment fragment = SurveySubmittedProgressResult.newInstance(submittedScoreData);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("SurveySubmittedProgressResult")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /* Back from Survey toolbar button*/
    public void backFromSurveyDetailsView() {
        Fragment fragment = new SurveyFragment();
        String fragmentTag = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.left_to_right_anim, R.anim.right_to_left_animation)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("com.lifecyclehealth.lifecyclehealth.fragments.SurveyFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    /* Change fragment for coming soon view*/
    public void changeToComingSoon(String heading) {
        Fragment fragment = SampleFragment.newInstance(heading);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("SampleFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    public void changeToScheduleMeet() {
        Fragment fragment = ScheduleMeet.newInstance();
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("ScheduleMeet")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    public void moreLogout() {
        Fragment fragment = LogOutFragment.newInstance();
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("LogOutFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    /* Change fragment for survey report view*/
    public void changeToSurveyReport(String data) {
        Fragment fragment = SurveyReports.newInstance(data);
        String fragmentTag = fragment.getClass().getName();
        printLog("tag" + fragmentTag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack("SurveyReports")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }


    /******************************HELPER Method Creation*********************************/
    public List<PatientSurveyItem> getListConverted(String data) {

        TypeToken<List<PatientSurveyItem>> token = new TypeToken<List<PatientSurveyItem>>() {
        };
        return new Gson().fromJson(data, token.getType());
    }

    private boolean isOdd(int val) {
        return (val & 0x01) != 0;
    }

    /**********************************************DUMMY DATA CREATION ENDS****************************************************/

    @Override
    protected void onResume() {
        super.onResume();
        printLog("Main activity onResume");
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        startService(new Intent(this, Time_out_services.class));
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        printLog("mainactivity onNewIntent");
        setIntent(intent);
        try {
            Bundle extras = getIntent().getExtras();
            value = extras.getString("from_notification");
            if (value.trim().equals("1")) {
                MainActivity.bottomBar.selectTabWithId(R.id.tab_message);
                MainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_message);
                setupBottomBar();
            } else if (value.trim().equals("2")) {
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                initialiseData();
                //setupBottomBar();
            }
        } catch (Exception e) {
            printLog("intent error of notification" + e.getMessage());
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        printLog("touch onUserInteraction");
        TOUCH_TIME = System.currentTimeMillis();
        stop();
        restart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, false);
        startService(new Intent(this, BackgroundTrackingService.class));
        stop();
    }

    @Override
    protected void onDestroy() {
        //BaseActivity.active=false;
        super.onDestroy();
        stop();
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    };

    private Handler myHandler;

    private void start() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
        stopService(new Intent(this, BackgroundTrackingService.class));
    }

    private void stop() {
        myHandler.removeCallbacks(myRunnable);
    }

    private void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }


    /*Check for fragment alive or not*/
    public void checkAliveFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null && fragmentManager.getFragments() != null) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                if (fragment != null && fragment.isVisible() && fragment instanceof MeetFragment) {
                    ((MeetFragment) fragment).getRefreshMeetFromNotification();
                }
            }
        }
    }

    /* *//*Local Notification start*//*
    public void showLocalNotification(String text) {
        NotificationBuilder.V1 builder = NotificationBuilder.local()
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .setLayoutId(getNextLayout())
                .setIconDrawable(getResources().getDrawable(R.drawable.message_notification))
                .setTitle(text)
                .setDelay(15);

        mDelegater.send(builder.getNotification());
    }

    private int mLayoutIdx;
    // if layout is set to 0, the default will be used.
    private static final int[] mLayoutSet = new int[]{
            zemin.notification.R.layout.notification_simple_2,
    };

    private int getNextLayout() {
        if (mLayoutIdx == mLayoutSet.length) {
            mLayoutIdx = 0;
        }
        return mLayoutSet[mLayoutIdx++];
    }

    *//*Local Notification end*/


}
