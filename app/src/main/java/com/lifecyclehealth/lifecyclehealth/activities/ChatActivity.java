package com.lifecyclehealth.lifecyclehealth.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithoutEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.designate.GlobalDesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.dto.CreateMessageConversationDTO;
import com.lifecyclehealth.lifecyclehealth.dto.MeetNowDTO;
import com.lifecyclehealth.lifecyclehealth.dto.SubmitChatParticipantDto;
import com.lifecyclehealth.lifecyclehealth.dto.SubmitParticipantDto;
import com.lifecyclehealth.lifecyclehealth.fragments.ChatBinderDialog;
import com.lifecyclehealth.lifecyclehealth.model.ChatBinderDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.GlobalCheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.InviteUserMeetResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsWithoutEpisodeModel;
import com.lifecyclehealth.lifecyclehealth.model.MeetNowResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageCreateConversationResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageEpisodeListResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessagePatientListModel;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.services.Time_out_services;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkAdapter;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.moxtra.binder.ui.search.global.GlobalSearchFragment;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.chat.controller.ChatController;
import com.moxtra.sdk.chat.impl.ChatDetailImpl;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.chat.model.ChatDetail;
import com.moxtra.sdk.chat.repo.ChatRepo;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.common.model.User;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TOUCH_TIME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHAT_GET_BINDER_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHAT_GET_BINDER_PATIENT_INVITEES;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_CHAT_SET_BINDER_PATIENT_INVITEES;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_CREATE_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_WITH_MULTIPLE_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_PATIENT_LIST;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_STARTMEET;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class ChatActivity extends BaseActivity implements View.OnClickListener {


    private static final String KEY_CHAT = "chat";
    private static final String KEY_ACTION = "action";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_UNIQUE_ID_LIST = "uniqueIdList";

    private static final String ACTION_SHOW = "show";
    private static final String ACTION_START = "start";
    private boolean isPatient;
    private static final String TAG = "DEMO_ChatActivity";

    private final Handler mHandler = new Handler();
    private ChatClientDelegate mChatClientDelegate;
    private ChatController mChatController;
    private ChatRepo mChatRepo;
    public NetworkRequestUtil networkRequestUtil;
    private String patientUserId, EpisodeCarePlanID;
    MeetInviteParticipantsModel.EpisodeParticipantList EpisodeList = null;

    private Chat mChat;
    TextView TextViewTitle;

    public static void showChat(Context ctx, Chat chat) {
        Intent intent = new Intent(ctx, ChatActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_SHOW);
        intent.putExtra(KEY_CHAT, chat);
        ctx.startActivity(intent);
    }

    public static void startGroupChat(Context ctx, String topic, ArrayList<String> uniqueIdList) {
        Intent intent = new Intent(ctx, ChatActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_START);
        intent.putExtra(KEY_TOPIC, topic);
        intent.putStringArrayListExtra(KEY_UNIQUE_ID_LIST, uniqueIdList);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_chat);
        networkRequestUtil = new NetworkRequestUtil(this);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        start();

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "no intent received");
            finishInMainThread();
            return;
        }
        mChatClientDelegate = ChatClient.getClientDelegate();
        if (mChatClientDelegate == null) {
            Log.e(TAG, "ChatClient is null");
            finishInMainThread();
            return;
        }
        String action = intent.getStringExtra(KEY_ACTION);
        showProgressDialog(true);
        if (ACTION_SHOW.equals(action)) {
            showChat(intent);
        } else if (ACTION_START.equals(action)) {
            startChat(intent);
        } else {
            finishInMainThread();
        }


        TextViewTitle = (TextView) findViewById(R.id.toolbar_title);
        ImageView imageView = (ImageView) findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(this);
        TextViewTitle.setOnClickListener(this);

        TextViewTitle.setText(mChat.getTopic());

        ImageView overFlowIcon = (ImageView) findViewById(R.id.overFlowIcon);
        ImageView search = (ImageView) findViewById(R.id.search);
        overFlowIcon.setOnClickListener(this);
        search.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search: {
                showSearchFragment();
                break;
            }
            case R.id.overFlowIcon: {
                isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
                if (isPatient) {
                    getBinder(ChatActivity.this);
                } else {
                    binderForCaregiver(ChatActivity.this);
                }
                break;
            }
            case R.id.backArrowBtn: {
                finishInMainThread();
                break;
            }
            case R.id.toolbar_title: {
                finishInMainThread();
                break;
            }
        }
    }

    @Override
    String getTag() {
        return "ChatActivity";
    }

    private void startChat(Intent intent) {
        String topic = intent.getStringExtra(KEY_TOPIC);

        final List<String> uniqueIdList = intent.getStringArrayListExtra(KEY_UNIQUE_ID_LIST);
        final String orgId = null;
        mChatRepo = mChatClientDelegate.createChatRepo();


        mChatRepo.createGroupChat(topic, new ApiCallback<Chat>() {
            @Override
            public void onCompleted(Chat chat) {
                Log.i(TAG, "Create group chat successfully.");
                mChat = chat;


               /* mChat.inviteMembers(orgId, uniqueIdList, new ApiCallback<Void>() {
                    @Override
                    public void onCompleted(Void result) {
                        Log.i(TAG, "Invite members successfully.");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.e(TAG, "Failed to invite members, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                    }
                });*/

                ChatDetail chatDetail;
                chatDetail = new ChatDetailImpl(chat);

                chatDetail.inviteMembers(orgId, uniqueIdList, new ApiCallback<Void>() {
                    @Override
                    public void onCompleted(Void result) {
                        Log.i(TAG, "Invite members successfully.");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.e(TAG, "Failed to invite members, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                    }
                });

                showChatFragment();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "Failed to create group chat, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                finishInMainThread();
            }
        });
    }

    private void showChat(Intent intent) {
        mChat = intent.getParcelableExtra(KEY_CHAT);
        if (mChat == null) {
            Log.e(TAG, "No chat found");
            finishInMainThread();
            return;
        }
        showChatFragment();
    }

    private void finishInMainThread() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void showChatFragment() {
        mChatController = mChatClientDelegate.createChatController(mChat);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.chat_frame);
                //Fragment fragment1= new GlobalSearchFragment();
                if (fragment == null) {
                    fragment = mChatController.createChatFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.chat_frame, fragment).commit();
                }
                showProgressDialog(false);
            }
        });
    }

    private void showSearchFragment() {

       /* Fragment fragment= new GlobalSearchFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.chat_frame, fragment).commit();*/
        Intent intent = new Intent(ChatActivity.this, MessageSearchActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BaseActivity.active=true;
        if (mChatController != null) {
            mChatController.cleanup();
        }
        stop();
        Log.d(TAG, "onCreate");
    }


    private void binderForCaregiver(final Activity activity) {
        context = activity.getApplicationContext();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.chat_binder_dialog, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialogInitialMessage = builder.create();

        Button binder_setting = (Button) dialogView.findViewById(R.id.binder_setting);
        final Button meet_now = (Button) dialogView.findViewById(R.id.meet_now);

        binder_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogInitialMessage.dismiss();
                getBinder(ChatActivity.this);
            }
        });

        meet_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogInitialMessage.dismiss();
                getPatientEpisodeName(ChatActivity.this);

            }
        });

        Window window = alertDialogInitialMessage.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogInitialMessage.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(lp);

        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogInitialMessage.show();

    }


    private void getPatientEpisodeName(final Activity activity) {
        context = activity.getApplicationContext();
        showProgressDialog(true);
        String url;

        url = BASE_URL + URL_CHAT_GET_BINDER_PATIENT + "/" + mChat.getId();

        if (isConnectedToNetwork(activity)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Binder:" + response);
                        if (response != null) {
                            ChatBinderDialogResponse chatBinderDialogResponse = new Gson().fromJson(response.toString(), ChatBinderDialogResponse.class);
                            if (chatBinderDialogResponse != null) {
                                if (chatBinderDialogResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (chatBinderDialogResponse.getBinderDetails() != null) {
                                        patientUserId = chatBinderDialogResponse.getBinderDetails().getPatient_UserID();
                                        EpisodeCarePlanID = chatBinderDialogResponse.getBinderDetails().getEpisode_Care_PlanID();
                                        meetNow(activity);
                                    }
                                } else {
                                    meetNow(activity);
                                    //showDialogWithOkButton(chatBinderDialogResponse.getMessage());
                                }
                            } else {
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                                meetNow(activity);
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                        meetNow(activity);
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
                meetNow(activity);
            }
        } else {
            showProgressDialog(false);
            meetNow(activity);
            showNoNetworkMessage();
        }

    }

    private void getBinder(final Activity activity) {
        context = activity.getApplicationContext();
        showProgressDialog(true);
        String url;

        url = BASE_URL + URL_CHAT_GET_BINDER_PATIENT + "/" + mChat.getId();

        if (isConnectedToNetwork(activity)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Binder:" + response);
                        if (response != null) {
                            ChatBinderDialogResponse chatBinderDialogResponse = new Gson().fromJson(response.toString(), ChatBinderDialogResponse.class);
                            if (chatBinderDialogResponse != null) {
                                if (chatBinderDialogResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (chatBinderDialogResponse.getBinderDetails() != null) {
                                        binderDialog(activity, chatBinderDialogResponse);
                                    }
                                } else {
                                    //showDialogWithOkButton(chatBinderDialogResponse.getMessage());
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

    android.support.v7.app.AlertDialog alertDialogShowData;

    private void binderDialog(final Activity activity, ChatBinderDialogResponse chatBinderDialogResponse) {
        context = activity.getApplicationContext();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.chat_binder_invitees_dialog, null);

        builder.setView(dialogView);
        alertDialogShowData = builder.create();

        Button Invite = dialogView.findViewById(R.id.Invite);
        Button cancel = dialogView.findViewById(R.id.cancel);
        TextView topic = dialogView.findViewById(R.id.topic);
        TextView title = dialogView.findViewById(R.id.title);
        final ImageView imageView2 = dialogView.findViewById(R.id.imageView2);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        title.setText(chatBinderDialogResponse.getBinderDetails().getName());
        topic.setText(chatBinderDialogResponse.getBinderDetails().getName());
        com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

        imageLoader.get(chatBinderDialogResponse.getBinderDetails().getThumbnail_uri(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Log", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    imageView2.setImageBitmap(response.getBitmap());
                }
            }
        });

        ChatBinderDialog chatBinderDialog = new ChatBinderDialog(chatBinderDialogResponse.getBinderDetails(), getApplicationContext(), new ChatBinderDialog.OnItemClickListener() {
            @Override
            public void onItemClick(ChatBinderDialogResponse.BinderDetails item, String Type, String pos) {

            }
        });
        recyclerView.setAdapter(chatBinderDialog);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                alertDialogShowData.dismiss();
            }
        });

        Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analytics.with(getApplicationContext()).track("Invite participant for eVisit from running meet", new Properties().putValue("category", "Mobile"));
                getInviteesForPatient(activity);
            }
        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(alertDialogShowData.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        int dialogWindowHeight = (int) (displayHeight * 0.7f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        alertDialogShowData.getWindow().setAttributes(layoutParams);
        alertDialogShowData.getWindow().setAttributes(layoutParams);
        alertDialogShowData.show();

    }

    private MeetInviteParticipantsModel meetInviteParticipantsModel;
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelSearch;


    private void getInviteesForPatient(final Activity activity) {
        context = activity.getApplicationContext();
        showProgressDialog(true);
        String url;

        url = BASE_URL + URL_CHAT_GET_BINDER_PATIENT_INVITEES + "/" + mChat.getId();

        if (isConnectedToNetwork(activity)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Invitees:" + response);
                        if (response != null) {
                            meetInviteParticipantsModel = new Gson().fromJson(response.toString(), MeetInviteParticipantsModel.class);
                            if (meetInviteParticipantsModel != null) {
                                if (meetInviteParticipantsModel.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().size() > 0) {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isDesignate_Exist()) {
                                                    NetworkAdapter networkAdapter = new NetworkAdapter();
                                                    final int finalI = i;
                                                    networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {

                                                            for (int k = 0; k < meetInviteParticipantsModel.getEpisodeParticipantList().size(); k++) {
                                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(k).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(k).setChecked(true);
                                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(k).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                    //UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(k).getUserID());
                                                                }

                                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(k).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(k).setChecked(true);
                                                                    //UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(k).getUserID());
                                                                }
                                                            }
                                                            UserIDs = new ArrayList<String>();
                                                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant = new ArrayList<String>();

                                                            for (int h = 0; h < meetInviteParticipantsModel.getEpisodeParticipantList().size(); h++) {
                                                                String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getRoleName());
                                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(h).isLoggedInUser()) {
                                                                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(h).isChecked()) {
                                                                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                } else if (role.contains("Patient")) {
                                                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(h).isPatientSelected()) {
                                                                    } else {
                                                                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(h).getUserID());
                                                                    }
                                                                }
                                                            }

                                                            adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                                                                    setAdapterWithEpisode(item, Type, pos);
                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapter);
                                                        }

                                                        @Override
                                                        public void onFailure() {

                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }
                                        showDialogInviteesForPatient(activity);
                                    } else {
                                        showDialogWithOkButton("No invitees");
                                    }
                                } else {
                                    showDialogWithOkButton1(meetInviteParticipantsModel.getMessage());
                                }
                            }

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


    RecyclerView recyclerViewDialog;
    MeetInviteParticipantsAdapter adapter;
    android.support.v7.app.AlertDialog dialog;

    private void showDialogInviteesForPatient(final Activity activity) {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.invite_participants_for_chat_patient, null);
        builder.setView(dialogView);
        dialog = builder.create();

        context = activity.getApplicationContext();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.invite_participants_for_chat_patient);

        recyclerViewDialog = (RecyclerView) dialogView.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(activity));

        final EditText search = (EditText) dialogView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                printLog("tonTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {

                printLog("afterTextChanged");
                if (meetInviteParticipantsModel != null) {
                    String searchString = search.getText().toString();
                    filterInviteesWithEpisode(searchString);
                }

            }
        });

        adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisode(item, Type, pos);
            }
        });
        recyclerViewDialog.setAdapter(adapter);


        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModel != null) {
                    MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                            setAdapterWithEpisode(item, Type, pos);
                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button done = (Button) dialogView.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogShowData.dismiss();

                if (MeetInviteParticipantsAdapter.selectedParticipant.size() > 0) {
                    UserIDs = MeetInviteParticipantsAdapter.selectedParticipant;
                    submitInviteList(activity);

                }

            }
        });

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.copyFrom(dialog.getWindow().getAttributes());
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        dialog.show();

    }



    /*Global Start*/

    private void setAdapterWithEpisode(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {


        MeetInviteParticipantsAdapter.selectedParticipant = new ArrayList();

        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
            String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getRoleName());
            if (UserIDs != null) {
                if (UserIDs.size() > 0) {
                    if (UserIDs.contains(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID())) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    }
                } else {
                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isChecked()) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isPatientSelected()) {
                        } else {
                            MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                            UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isChecked()) {
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                } else if (role.contains("Patient")) {
                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isPatientSelected()) {
                    } else {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    }
                }
            }

        }

        HashSet hs = new HashSet();
        hs.addAll(UserIDs);
        UserIDs.clear();
        UserIDs.addAll(hs);

        boolean contains = MeetInviteParticipantsAdapter.selectedParticipant.contains(meetList.getUserID());
        if (contains) {

            if (meetList.isLoggedInUser()) {
                return;
            }
            printLog("remove");

            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(context, this.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    //if (response.getStatus().equals("1") || response.getStatus().equals("0")) {
                    if ((response.getStatus().equals("1") || response.getStatus().equals("0"))) {

                        MeetInviteParticipantsModel.EpisodeParticipantList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsModel.getEpisodeParticipantList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            String name = null;

                            if (response.getDesignateList() != null && response.getDesignateList().size() > 0 && MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                    }
                                }

                                if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                    String text = meetList.getFullName() + " is designate for " + name;
                                    showDialogWithOkButton(text);
                                }

                            } else {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(i);
                                int user = UserIDs.indexOf(meetList.getUserID());
                                if (user >= 0) {
                                    UserIDs.remove(user);
                                }
                                setAdapterWithEpisode();
                            }
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsAdapter.selectedParticipant.contains(episodeParticipantForDesignate.getUserID())) {
                                String text = name + " is designate for " + meetList.getFullName();
                                //showDialogWithOkButton1(text);

                                final MeetInviteParticipantsModel.EpisodeParticipantList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                                // MeetInviteParticipantsAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getUserID());
                                                printLog("checked provider");
                                                int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                                                int user = UserIDs.indexOf(meetList.getUserID());
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                                String designate_id = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getDesignate_Id();

                                                r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(designate_id);
                                                if (r >= 0)
                                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                                                user = UserIDs.indexOf(designate_id);
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                            }

                                        }
                                        setAdapterWithEpisode();
                                    }
                                });


                            } else if (response.getDesignateList() != null && response.getDesignateList().size() > 0 && MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                    }
                                }

                                if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                    String text = meetList.getFullName() + " is designate for " + name;
                                    showDialogWithOkButton(text);
                                }

                            } else {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(i);
                                int user = UserIDs.indexOf(meetList.getUserID());
                                if (user >= 0) {
                                    UserIDs.remove(user);
                                }

                                setAdapterWithEpisode();
                            }
                        }
                    } else if (response.getDesignateList() != null && response.getDesignateList().size() > 0) {

                        if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton(text);
                            }

                        } else {
                            int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                            if (r >= 0)
                                MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                            int user = UserIDs.indexOf(meetList.getUserID());
                            if (user >= 0) {
                                UserIDs.remove(user);
                            }

                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                }
                            }

                            setAdapterWithEpisode();
                        }
                    } else {
                        int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                        if (r >= 0)
                            MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                        int user = UserIDs.indexOf(meetList.getUserID());
                        if (user >= 0) {
                            UserIDs.remove(user);
                        }

                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                            }
                        }

                        setAdapterWithEpisode();
                    }
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onError(int error) {

                }
            });
        }


        /*For checked*/
        else {
            printLog("add");

            // if (meetList.isDesignate_Exist()) {

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.checkProviderListGlobal(context, this.networkRequestUtil, meetList.getUserID(), new GlobalDesignateCallBack() {
                @Override
                public void onSuccess(final GlobalCheckProviderResponse response) {
                    printLog("response:" + response);
                    if (response.getDesignateList().getOrganisation_DesignateID() != null) {
                        switch (response.getDesignateList().getOrganisation_Designate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getDesignateList().getOrganisation_Preference_Message(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            //if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getProvider_UserID() + "")) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                //meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getOrganisation_DesignateID() + "");
                                                MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
                                                UserIDs.add(meetList.getUserID());
                                                EpisodeList = meetInviteParticipantsModel.getEpisodeParticipantList().get(i);
                                            }

                                            if (response.getDesignateList().getOrganisation_DesignateID() != null)
                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID() + "")) {
                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                    MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                        }
                                        if (EpisodeList != null) {
                                            setAdapterWithEpisode();
                                            setAdapterWithEpisodeGlobalIndividual(EpisodeList);
                                        } else
                                            setAdapterWithEpisode();
                                    }
                                });

                                //setAdapterWithOutEpisode(item, Type, pos);
                                break;
                            }
                            case "designate_or_user": {
                                showDialogWithOkCancelButton(response.getDesignateList().getOrganisation_Preference_Message(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        if (response.getDesignateList().getOrganisation_DesignateID() != null) {
                                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID())) {
                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                    MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                            }
                                            setAdapterWithEpisode();
                                        } else {
                                            setAdapterWithEpisode();
                                            setAdapterWithEpisodeGlobalIndividual(meetList);
                                        }
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                setAdapterWithEpisode();
                                setAdapterWithEpisodeGlobalIndividual(meetList);

                                break;
                            }

                            default: {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                        UserIDs.add(meetList.getUserID());
                                    }
                                }
                                MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
                                setAdapterWithEpisodeGlobalIndividual(meetList);
                                setAdapterWithEpisode();
                            }
                        }
                    } else {

                        if (response.getDesignateList().isIs_Provider_Designate()) {
                            showDialogWithOkCancelButton(response.getDesignateList().getProvider_Preference_Message(), new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    setAdapterWithEpisodeGlobalIndividual(meetList);
                                }
                            });

                        } else {
                            setAdapterWithEpisodeGlobalIndividual(meetList);
                        }
                    }
                }

                @Override
                public void onFailure() {
                    for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                            UserIDs.add(meetList.getUserID());
                        }
                    }
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
                    setAdapterWithEpisodeGlobalIndividual(meetList);
                    setAdapterWithEpisode();
                }

                @Override
                public void onError(int error) {

                }
            });

        }


    }

    private void setAdapterWithEpisodeGlobalIndividual(final MeetInviteParticipantsModel.EpisodeParticipantList meetList) {

        MeetInviteParticipantsAdapter.selectedParticipant = new ArrayList();

        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
            String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getRoleName());
            if (UserIDs != null) {
                if (UserIDs.size() > 0) {
                    if (UserIDs.contains(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID())) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    }
                } else {
                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isChecked()) {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isPatientSelected()) {
                        } else {
                            MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                            UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isChecked()) {
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                } else if (role.contains("Patient")) {
                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isPatientSelected()) {
                    } else {
                        MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                    }
                }
            }

        }

        HashSet hs = new HashSet();
        hs.addAll(UserIDs);
        UserIDs.clear();
        UserIDs.addAll(hs);

        boolean contains = MeetInviteParticipantsAdapter.selectedParticipant.contains(meetList.getUserID());
        /*For checked*/
        printLog("add");

        if (meetList.isDesignate_Exist()) {

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.checkProviderList(context, this.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {
                    printLog("response:" + response);

                    if (response.getDesignateList().get(0).getDesignate_Preference() != null) {
                        switch (response.getDesignateList().get(0).getDesignate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID() + "")) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID() + "");
                                                MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                            }

                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID() + "")) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithEpisode();
                                    }
                                });

                                //setAdapterWithOutEpisode(item, Type, pos);
                                break;
                            }
                            case "designate_or_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                                MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithEpisode();
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                        /// meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                        MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                        UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                    }
                                }

                                setAdapterWithEpisode();
                                break;
                            }
                        }
                    } else {

                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                /// meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                            }
                        }

                        setAdapterWithEpisode();
                    }
                }

                @Override
                public void onFailure() {
                    for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                            UserIDs.add(meetList.getUserID());
                        }
                    }
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
                    setAdapterWithEpisode();
                }

                @Override
                public void onError(int error) {

                }
            });
        } else {
            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                    UserIDs.add(meetList.getUserID());
                }
            }
            MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
            setAdapterWithEpisode();
        }
    }

    private void setAdapterWithEpisode() {
        recyclerViewDialog.setAdapter(null);
        if (meetInviteParticipantsModel != null) {
            MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), context, UserIDs, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                    setAdapterWithEpisode(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }

    /*Global End*/




    private void submitInviteList(Activity activity) {
        showProgressDialog(true);
        String sessionKey = mChat.getId();
        if (isConnectedToNetwork(activity)) {
            try {
                String join = TextUtils.join(",", MeetInviteParticipantsAdapter.selectedParticipant);
                SubmitChatParticipantDto submitParticipantDto = new SubmitChatParticipantDto();
                submitParticipantDto.setBinder_id(sessionKey);
                submitParticipantDto.setUserIDs(MeetInviteParticipantsAdapter.selectedParticipant);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(submitParticipantDto));

                this.networkRequestUtil.putDataSecure(BASE_URL + URL_CHAT_SET_BINDER_PATIENT_INVITEES, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Invite Meet Participant:" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    dialog.dismiss();
                                    dialog.cancel();

                                } else {
                                    showDialogWithOkButton(inviteUserMeetResponse.getMessage());
                                }
                            } else {
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                            }
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


    public void filterInviteesWithEpisode(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        meetInviteParticipantsModelSearch = new ArrayList<>();
        if (charText.equals("")) {
            meetInviteParticipantsModelSearch = meetInviteParticipantsModel.getEpisodeParticipantList();
        } else {
            meetInviteParticipantsModelSearch = new ArrayList<>();
            for (MeetInviteParticipantsModel.EpisodeParticipantList cs : meetInviteParticipantsModel.getEpisodeParticipantList()) {
                if (cs.getFullName().toLowerCase().contains(charText)) {
                    meetInviteParticipantsModelSearch.add(cs);
                }
            }

        }
        if (meetInviteParticipantsModelSearch.size() > 0)
            notifyDataSetChangedInviteesWithEpisode();
        else
            recyclerViewDialog.setAdapter(null);
    }

    private void notifyDataSetChangedInviteesWithEpisode() {

        MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModelSearch, getApplicationContext(), UserIDs, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisode(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapter);
    }


























    /*Meet Now*/

    String Name, Patient_UserID, PatientID, Title, Episode_Care_PlanID, Episode_Care_Plan_Name;
    ArrayList<String> UserIDs = new ArrayList<>();
    private Button btnInvitees, btnCancel, startMeet;
    private EditText meetingTitle;
    private Spinner spinner_no_episode_PatientName, spinner_episode_name, spinnerPatientName;
    private AppCompatRadioButton provider_to_provider, patient_related_episode, provider_patient_with_no_episode, provider_with_multiple_patient;
    private LinearLayout linear_provider_patient_with_no_episode, linear_patient_related_episode;
    private RadioGroup radioGroup;
    private ArrayList<String> PatientNameList = new ArrayList<>();
    private ArrayList<String> PatientNameListUserID = new ArrayList<>();
    private ArrayList<String> PatientNameListId = new ArrayList<>();
    private ArrayList<String> EpisodeNameList = new ArrayList<>();
    private ArrayList<String> EpisodeStatusList = new ArrayList<>();
    private ArrayList<String> EpisodeNameListId = new ArrayList<>();

    private int selectedTypePatient = 0;
    Context context;
    private String selectedEpisode = "";
    MeetInviteParticipantsWithEpisodeAdapter adapterWithEpisode;
    MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode;


    private MeetInviteParticipantsModel meetInviteParticipantsModelMeetNow;
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelMeetNowSearch;
    private MeetInviteParticipantsWithoutEpisodeModel meetInviteParticipantsWithoutEpisodeModel;
    ArrayList<MeetInviteParticipantsWithoutEpisodeModel.UserList> meetInviteParticipantsWithoutEpisodeModelSearch;

    android.support.v7.app.AlertDialog alertDialogMeetNow;

    private void meetNow(final Activity activity) {
        UserIDs = new ArrayList<String>();
        Analytics.with(getApplicationContext()).screen("MeetNow");
        context = activity.getApplicationContext();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.chat_meet_now_dialog, null);
        builder.setView(dialogView);
        alertDialogMeetNow = builder.create();
        context = alertDialogMeetNow.getContext();

        btnInvitees = (Button) dialogView.findViewById(R.id.btnInvitees);
        startMeet = (Button) dialogView.findViewById(R.id.startMeet);
        btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        meetingTitle = (EditText) dialogView.findViewById(R.id.meetingTitle);
        spinner_no_episode_PatientName = (Spinner) dialogView.findViewById(R.id.spinner_no_episode_PatientName);
        spinner_episode_name = (Spinner) dialogView.findViewById(R.id.spinner_episode_name);
        spinnerPatientName = (Spinner) dialogView.findViewById(R.id.spinnerPatientName);

        patient_related_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.patient_related_episode);
        provider_to_provider = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_to_provider);
        provider_with_multiple_patient = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_with_multiple_patient);
        provider_patient_with_no_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_patient_with_no_episode);
        linear_provider_patient_with_no_episode = (LinearLayout) dialogView.findViewById(R.id.linear_provider_patient_with_no_episode);
        linear_patient_related_episode = (LinearLayout) dialogView.findViewById(R.id.linear_patient_related_episode);

        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
        linear_patient_related_episode.setVisibility(View.GONE);

        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.patient_related_episode:
                        selectedTypePatient = 2;
                        patient_related_episode.setSupportButtonTintList(getColorList());
                        patient_related_episode.setChecked(true);
                        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
                        provider_to_provider.setSupportButtonTintList(deSelectColorList());
                        provider_with_multiple_patient.setSupportButtonTintList(deSelectColorList());
                        linear_patient_related_episode.setVisibility(View.VISIBLE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        setSpinnerAdapter();
                        getPatientList();

                        break;

                    case R.id.provider_patient_with_no_episode:
                        selectedTypePatient = 3;
                        provider_patient_with_no_episode.setSupportButtonTintList(getColorList());
                        provider_patient_with_no_episode.setChecked(true);
                        patient_related_episode.setSupportButtonTintList(deSelectColorList());
                        provider_to_provider.setSupportButtonTintList(deSelectColorList());
                        provider_with_multiple_patient.setSupportButtonTintList(deSelectColorList());
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.VISIBLE);
                        getPatientList();
                        break;

                    case R.id.provider_to_provider:
                        selectedTypePatient = 1;
                        provider_to_provider.setSupportButtonTintList(getColorList());
                        provider_to_provider.setChecked(true);
                        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
                        provider_with_multiple_patient.setSupportButtonTintList(deSelectColorList());
                        patient_related_episode.setSupportButtonTintList(deSelectColorList());
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        break;

                    case R.id.provider_with_multiple_patient:
                        selectedTypePatient = 4;
                        provider_with_multiple_patient.setSupportButtonTintList(getColorList());
                        provider_with_multiple_patient.setChecked(true);
                        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
                        provider_to_provider.setSupportButtonTintList(deSelectColorList());
                        patient_related_episode.setSupportButtonTintList(deSelectColorList());
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        break;

                }
            }
        });

        setSpinnerAdapter();

        spinnerPatientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                PatientID = "";
                printLog("position : " + position);
                if (position > 0) {
                    PatientID = PatientNameListId.get(position);
                    Patient_UserID = PatientNameListUserID.get(position);

                    if (PatientID.equals("-99")) {
                        EpisodeStatusList = new ArrayList<String>();
                        EpisodeNameList = new ArrayList<String>();
                        EpisodeNameListId = new ArrayList<String>();
                        EpisodeNameList.add("Select Episode");
                        EpisodeNameListId.add("-99");
                        EpisodeStatusList.add("-99");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, EpisodeNameList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_episode_name.setAdapter(dataAdapter);
                    } else {
                        callPatientEpisodeList(PatientNameListId.get(position));
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinner_no_episode_PatientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                printLog("position : " + position);
                PatientID = "";
                if (position > 0) {
                    PatientID = PatientNameListId.get(position);
                    Patient_UserID = PatientNameListUserID.get(position);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserIDs = new ArrayList<String>();
                alertDialogMeetNow.dismiss();
            }
        });

        btnInvitees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (selectedTypePatient) {
                    case 1: {
                        if (meetingTitle.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter meeting title");
                            return;
                        }
                        Name = meetingTitle.getText().toString();

                        getInviteesWithOutEpisode("");
                        break;
                    }
                    case 4: {
                        if (meetingTitle.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter meeting title");
                            return;
                        }
                        Name = meetingTitle.getText().toString();

                        getInviteesWithOutEpisode("");
                        break;
                    }
                    case 2: {

                        int position = spinner_episode_name.getSelectedItemPosition();
                        int positionPatient = spinnerPatientName.getSelectedItemPosition();
                        Episode_Care_Plan_Name = EpisodeNameList.get(position);
                        Episode_Care_PlanID = EpisodeNameListId.get(position);
                        if (meetingTitle.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter meeting title");
                            return;
                        } else if (positionPatient == 0) {
                            showDialogWithOkButton("Please select Patient");
                            return;
                        } else if (position == 0) {
                            showDialogWithOkButton("Please select episode");
                            return;
                        } else {
                            selectedEpisode = EpisodeNameListId.get(position);
                        }
                        Name = meetingTitle.getText().toString();

                        getInviteesWithEpisode();
                        break;
                    }
                    case 3: {
                        selectedEpisode = "";
                        Episode_Care_Plan_Name = "";
                        Episode_Care_PlanID = "";
                        int positionPatient = spinner_no_episode_PatientName.getSelectedItemPosition();
                        if (meetingTitle.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter meeting title");
                            return;
                        } else if (positionPatient == 0) {
                            showDialogWithOkButton("Please select Patient");
                            return;
                        }
                        Name = meetingTitle.getText().toString();

                        getInviteesWithOutEpisode(PatientID);
                        break;
                    }
                }

            }
        });

        startMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMeet(activity);
            }
        });

        Window window = alertDialogMeetNow.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogMeetNow.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(lp);

        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogMeetNow.show();

    }


    @SuppressLint("RestrictedApi")
    private void setSpinnerAdapter() {

        EpisodeNameList = new ArrayList<>();
        EpisodeNameListId = new ArrayList<>();
        selectedTypePatient = 2;
        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
        provider_to_provider.setSupportButtonTintList(deSelectColorList());
        provider_with_multiple_patient.setSupportButtonTintList(deSelectColorList());
        linear_patient_related_episode.setVisibility(View.VISIBLE);
        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
        patient_related_episode.setSupportButtonTintList(getColorList());
        patient_related_episode.setChecked(true);

        EpisodeNameList.add("Select Episode");
        EpisodeNameListId.add("-99");


        ArrayAdapter<String> dataAdapterEpisode = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, EpisodeNameList);
        dataAdapterEpisode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_episode_name.setAdapter(dataAdapterEpisode);

    }

    private void getPatientList() {
        showProgressDialog(true);
        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_PATIENT_LIST, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Patient list:" + response);
                        if (response != null) {
                            MessagePatientListModel messageEpisodeListResponse = new Gson().fromJson(response.toString(), MessagePatientListModel.class);
                            PatientNameList = new ArrayList<String>();
                            PatientNameListId = new ArrayList<String>();
                            PatientNameListUserID = new ArrayList<String>();

                            PatientNameList.add("Select Patient");
                            PatientNameListId.add("-99");
                            PatientNameListUserID.add("-99");

                            if (messageEpisodeListResponse != null) {
                                if (messageEpisodeListResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    for (int i = 0; i < messageEpisodeListResponse.getPatientList().size(); i++) {
                                        PatientNameList.add(messageEpisodeListResponse.getPatientList().get(i).getPatient_get_patient_list().getFullName());
                                        PatientNameListId.add(messageEpisodeListResponse.getPatientList().get(i).getPatient_get_patient_list().getPatientID());
                                        PatientNameListUserID.add(messageEpisodeListResponse.getPatientList().get(i).getPatient_get_patient_list().getPatient_UserID());
                                    }
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, PatientNameList);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerPatientName.setAdapter(dataAdapter);
                                    spinner_no_episode_PatientName.setAdapter(dataAdapter);
                                    printLog("selectedTypePatient: " + selectedTypePatient);
                                    if (selectedTypePatient == 2) {
                                        if (PatientNameList.size() > 0) {
                                            printLog("patientUserId :" + patientUserId);
                                            if (patientUserId != null) {
                                                int index = PatientNameListUserID.indexOf(patientUserId);
                                                printLog("index :" + index);
                                                if (index > 0) {
                                                    spinnerPatientName.setSelection(index, true);
                                                }
                                            }
                                        }
                                    } else if (selectedTypePatient == 3) {
                                        if (PatientNameList.size() > 0) {
                                            if (patientUserId != null) {
                                                int index = PatientNameListUserID.indexOf(patientUserId);
                                                printLog("index :" + index);
                                                if (index > 0) {
                                                    spinner_no_episode_PatientName.setSelection(index, true);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // showDialogWithOkButton(messageEpisodeListResponse.getMessage());
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


    private void callPatientEpisodeList(String isUser) {
        EpisodeNameList = new ArrayList<>();
        EpisodeNameListId = new ArrayList<>();
        showProgressDialog(true);
        String url;
        if (isUser.equals("P")) {
            url = BASE_URL + URL_MESSAGE_EPISODE_LIST_FOR_PATIENT;
        } else {
            // int selectedItemPosition = spinnerPatientName.getSelectedItemPosition();
            url = BASE_URL + URL_MESSAGE_EPISODE_LIST_FOR_PROVIDER + "/" + isUser;
        }
        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Episode list:" + response);
                        if (response != null) {
                            EpisodeStatusList = new ArrayList<String>();
                            EpisodeNameList = new ArrayList<String>();
                            EpisodeNameListId = new ArrayList<String>();
                            MessageEpisodeListResponse messageEpisodeListResponse = new Gson().fromJson(response.toString(), MessageEpisodeListResponse.class);
                            EpisodeNameList.add("Select Episode");
                            EpisodeNameListId.add("-99");
                            EpisodeStatusList.add("-99");
                            if (messageEpisodeListResponse != null) {
                                if (messageEpisodeListResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    for (int i = 0; i < messageEpisodeListResponse.getEpisodePlanList().size(); i++) {
                                        EpisodeNameList.add(messageEpisodeListResponse.getEpisodePlanList().get(i).getName());
                                        EpisodeNameListId.add(messageEpisodeListResponse.getEpisodePlanList().get(i).getEpisode_Care_PlanID());
                                        EpisodeStatusList.add(messageEpisodeListResponse.getEpisodePlanList().get(i).getStatus());
                                    }

                                } else {
                                    //showDialogWithOkButton(messageEpisodeListResponse.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, EpisodeNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_episode_name.setAdapter(dataAdapter);
                            if (EpisodeNameList != null) {
                                if (EpisodeNameList.size() == 2) {
                                    spinner_episode_name.setSelection(1, true);
                                } else {
                                    int index = EpisodeStatusList.indexOf("In Process");
                                    printLog("index : " + index);
                                    if (index > 0) {
                                        spinner_episode_name.setSelection(index, true);
                                    } else if (EpisodeNameList.size() > 2) {
                                        spinner_episode_name.setSelection(1, true);
                                    }
                                }
                            }

                            if (EpisodeCarePlanID != null) {
                                int index = EpisodeNameListId.indexOf(EpisodeCarePlanID);
                                if (index > 0) {
                                    spinner_episode_name.setSelection(index, true);
                                }
                            }
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


    private void getInviteesWithEpisode() {
        showProgressDialog(true);
        String url = null;
        switch (selectedTypePatient) {
            case 1: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
                break;
            }
            case 2: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_EPISODE + "/" + selectedEpisode;
                break;
            }
            case 3: {
                try {
                    url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_WITH_MULTIPLE_PATIENT;
                break;
            }
        }

        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Invitees:" + response);
                        if (response != null) {
                            meetInviteParticipantsModelMeetNow = new Gson().fromJson(response.toString(), MeetInviteParticipantsModel.class);
                            if (meetInviteParticipantsModelMeetNow != null) {
                                if (meetInviteParticipantsModelMeetNow.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size() > 0) {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isDesignate_Exist()) {
                                                    NetworkAdapter networkAdapter = new NetworkAdapter();
                                                    final int finalI = i;
                                                    networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {
                                                            for (int k = 0; k < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); k++) {
                                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).setChecked(true);
                                                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                    //UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).getUserID());
                                                                }

                                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).setChecked(true);
                                                                    //UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(k).getUserID());
                                                                }
                                                            }
                                                            UserIDs = new ArrayList<String>();
                                                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant = new ArrayList<String>();

                                                            for (int h = 0; h < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); h++) {
                                                                String role = TextUtils.join(",", meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getRoleName());
                                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).isLoggedInUser()) {
                                                                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).isChecked()) {
                                                                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                } else if (role.contains("Patient")) {
                                                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).isPatientSelected()) {
                                                                    } else {
                                                                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(h).getUserID());
                                                                    }
                                                                }
                                                            }
                                                            MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                                                                    setAdapterWithEpisodeMeet(item, Type, pos);

                                                                }
                                                            });

                                                            recyclerViewDialog.setAdapter(adapter);
                                                        }

                                                        @Override
                                                        public void onFailure() {

                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }

                                        showDialogInviteesWithEpisode(getApplicationContext());
                                    }
                                } else {
                                    //showDialogWithOkButton(meetInviteParticipantsModel.getMessage());
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

    /*    With Episode*/
    private void showDialogInviteesWithEpisode(Context view) {
        //dialog = new Dialog(view);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.fragment_message_invite_participants, null);
        builder.setView(dialogView);
        dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialogView.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final EditText search = (EditText) dialogView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                printLog("tonTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {

                printLog("afterTextChanged");
                if (meetInviteParticipantsModelMeetNow != null) {
                    String searchString = search.getText().toString();
                    filterInviteesWithEpisode1(searchString);
                }

            }
        });

        adapterWithEpisode = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisodeMeet(item, Type, pos);
            }
        });
        recyclerViewDialog.setAdapter(adapterWithEpisode);


        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModelMeetNow != null) {
                    MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                            setAdapterWithEpisodeMeet(item, Type, pos);

                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialogView.findViewById(R.id.btn_start_message);
        btn_start_message.setText("DONE");
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.size() > 0) {
                    //submitInviteList();
                    // UserIDs = TextUtils.join(",", MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant);
                    UserIDs = new ArrayList<String>();
                    UserIDs.addAll(MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant);
                    dialog.dismiss();
                    dialog.cancel();
                    Analytics.with(getApplicationContext()).track("inviteParticipantForMeetNow", new Properties().putValue("category", "Mobile")
                            .putValue("invitees", UserIDs));
                }

            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

    }

    public void filterInviteesWithEpisode1(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        meetInviteParticipantsModelMeetNowSearch = new ArrayList<>();
        if (charText.equals("")) {
            meetInviteParticipantsModelMeetNowSearch = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList();
        } else {
            meetInviteParticipantsModelMeetNowSearch = new ArrayList<>();
            for (MeetInviteParticipantsModel.EpisodeParticipantList cs : meetInviteParticipantsModelMeetNow.getEpisodeParticipantList()) {
                if (cs.getFullName().toLowerCase().contains(charText)) {
                    meetInviteParticipantsModelMeetNowSearch.add(cs);
                }
            }

        }
        if (meetInviteParticipantsModelMeetNowSearch.size() > 0)
            notifyDataSetChangedInviteesWithEpisode1();
        else
            recyclerViewDialog.setAdapter(null);
    }


    private void setAdapterWithEpisodeMeet(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant = new ArrayList();

        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
            String role = TextUtils.join(",", meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getRoleName());
            if (UserIDs != null) {
                if (UserIDs.size() > 0) {
                    if (UserIDs.contains(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID())) {
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    }
                } else {
                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isPatientSelected()) {
                        } else {
                            UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                } else if (role.contains("Patient")) {
                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isPatientSelected()) {
                    } else {
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    }
                }
            }
        }

        HashSet hs = new HashSet();
        hs.addAll(UserIDs);
        UserIDs.clear();
        UserIDs.addAll(hs);
      /*  if (UserIDs != null) {
            if (UserIDs.size() > 0) {
                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant = new ArrayList();
                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.addAll(UserIDs);
            }
        }*/

        boolean contains = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(meetList.getUserID());
        if (contains) {
            printLog("remove");
            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(getApplicationContext(), networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    if (response.getStatus().equals("1")) {

                        MeetInviteParticipantsModel.EpisodeParticipantList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                }
                            }

                            int i = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(meetList.getUserID());
                            if (i >= 0)
                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(i);
                            int user = UserIDs.indexOf(meetList.getUserID());
                            if (user >= 0) {
                                UserIDs.remove(user);
                            }
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(episodeParticipantForDesignate.getUserID())) {
                                //showDialogWithOkButton1(text);


                                final MeetInviteParticipantsModel.EpisodeParticipantList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton1("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                                // MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getUserID());
                                                printLog("checked provider");
                                                int r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(r);
                                                int user = UserIDs.indexOf(meetList.getUserID());
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                                String designate_id = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getDesignate_Id();

                                                r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(designate_id);
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(r);
                                                user = UserIDs.indexOf(designate_id);
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                            }

                                           /* if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getDesignate_Id())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                                //MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                printLog("checked designate");
                                                int r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(r);
                                                int user = UserIDs.indexOf(meetList.getUserID());
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                            }*/
                                        }
                                        setAdapterWithEpisodeMeet();
                                    }
                                });


                            } else {
                                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(i);
                                int user = UserIDs.indexOf(meetList.getUserID());
                                if (user >= 0) {
                                    UserIDs.remove(user);
                                }

                            }
                        }
                        setAdapterWithEpisodeMeet();

                    } else {

                        if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton1(text);
                            } else {
                                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }
                                int i = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(i);
                                int user = UserIDs.indexOf(meetList.getUserID());
                                if (user >= 0) {
                                    UserIDs.remove(user);
                                }
                                setAdapterWithEpisodeMeet();
                            }


                        } else {
                            int r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(meetList.getUserID());
                            if (r >= 0)
                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.remove(r);
                            int user = UserIDs.indexOf(meetList.getUserID());
                            if (user >= 0) {
                                UserIDs.remove(user);
                            }
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                }
                            }
                            setAdapterWithEpisodeMeet();
                        }
                    }
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onError(int error) {

                }
            });


        }


        /*  For checked*/
        else {
            if (meetList.isDesignate_Exist()) {

                NetworkAdapter networkAdapter = new NetworkAdapter();
                networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                    @Override
                    public void onSuccess(final CheckProviderResponse response) {

                        boolean containsAlready = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getDesignate_UserID());

                        if (containsAlready) {
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                               /* if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }*/
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }

                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }
                            }
                            setAdapterWithEpisodeMeet();
                        } else {
                            showDialogWithOkCancelButton1(getResources().getString(R.string.designate_available_message), new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                            meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                            meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                            printLog("checked provider");
                                        }

                                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                            meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            printLog("checked designate");
                                        }
                                    }
                                    UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                    UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                    setAdapterWithEpisodeMeet();
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure() {
                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                UserIDs.add(meetList.getUserID());
                            }
                        }
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetList.getUserID());
                    }

                    @Override
                    public void onError(int error) {

                    }
                });
            } else {
                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                        meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                        UserIDs.add(meetList.getUserID());
                    }
                }
                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetList.getUserID());
            }
        }
        setAdapterWithEpisodeMeet();


    }


    private void setAdapterWithEpisodeMeet() {
        recyclerViewDialog.setAdapter(null);
        if (meetInviteParticipantsModelMeetNow != null) {
            MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                    setAdapterWithEpisodeMeet(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }

    private void notifyDataSetChangedInviteesWithEpisode1() {

        MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNowSearch, getApplicationContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisodeMeet(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapter);
    }



    /*Without Episode*/

    private void getInviteesWithOutEpisode(String PatientID) {
        showProgressDialog(true);
        String url = null;


        switch (selectedTypePatient) {
            case 1: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
                break;
            }
            case 2: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_EPISODE + "/" + selectedEpisode;
                break;
            }

            case 3: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + PatientID;
                break;
            }
            case 4: {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_WITH_MULTIPLE_PATIENT;
                break;
            }
        }

        printLog(url);

        if (isConnectedToNetwork(this)) {
            try {
                networkRequestUtil.getDataSecure(url, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of Invitees:" + response);
                        if (response != null) {
                            meetInviteParticipantsWithoutEpisodeModel = new Gson().fromJson(response.toString(), MeetInviteParticipantsWithoutEpisodeModel.class);
                            if (meetInviteParticipantsWithoutEpisodeModel != null) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().size() > 0) {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isLoggedInUser()) {
                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isDesignate_Exist()) {
                                                    NetworkAdapter networkAdapter = new NetworkAdapter();
                                                    final int finalI = i;
                                                    networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {
                                                            for (int k = 0; k < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); k++) {
                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                    //UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
                                                                }

                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    // UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
                                                                }
                                                            }
                                                            UserIDs = new ArrayList<String>();
                                                            MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout = new ArrayList<String>();
                                                            for (int h = 0; h < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); h++) {
                                                                String role = TextUtils.join(",", meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getRoleName());

                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).isLoggedInUser()) {
                                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                } else if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).isChecked()) {
                                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                } else if (role.contains("Patient")) {
                                                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).isPatientSelected()) {
                                                                    } else {
                                                                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                        UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(h).getUserID());
                                                                    }
                                                                }
                                                            }


                                                            adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                                                                    setAdapterWithOutEpisode(item, Type, pos);
                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapterWithoutEpisode);
                                                        }

                                                        @Override
                                                        public void onFailure() {

                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }
                                        showDialogInviteesWithOutEpisode(getApplicationContext());
                                    }
                                } else {
                                    showDialogWithOkButton(meetInviteParticipantsWithoutEpisodeModel.getMessage());
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

    private void showDialogInviteesWithOutEpisode(Context view) {
        //dialog = new Dialog(view);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.fragment_message_invite_participants, null);
        builder.setView(dialogView);
        dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialogView.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final EditText search = (EditText) dialogView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                printLog("tonTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {

                printLog("afterTextChanged");
                if (meetInviteParticipantsWithoutEpisodeModel != null) {
                    String searchString = search.getText().toString();
                    filterInviteesWithOutEpisode(searchString);
                }

            }
        });

        adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisode(item, Type, pos);
            }
        });
        recyclerViewDialog.setAdapter(adapterWithoutEpisode);


        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                search.setText("");
                if (meetInviteParticipantsWithoutEpisodeModel != null) {
                    MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                            setAdapterWithOutEpisode(item, Type, pos);
                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialogView.findViewById(R.id.btn_start_message);
        btn_start_message.setText("DONE");
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.size() > 0) {
                    //UserIDs = TextUtils.join(",", MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipant);
                    UserIDs = new ArrayList<String>();
                    UserIDs.addAll(MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout);
                    dialog.dismiss();
                    dialog.cancel();

                    Analytics.with(getApplicationContext()).track("inviteParticipantForMeetNow", new Properties().putValue("category", "Mobile")
                            .putValue("invitees", UserIDs));
                }

            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

    }


    private void setAdapterWithOutEpisode(final MeetInviteParticipantsWithoutEpisodeModel.UserList meetList, String Type, String pos) {

        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout = new ArrayList();

        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
            String role = TextUtils.join(",", meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getRoleName());
            if (UserIDs != null) {
                if (UserIDs.size() > 0) {
                    if (UserIDs.contains(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID())) {
                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    }
                } else {
                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isLoggedInUser()) {
                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    } else if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isChecked()) {
                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isPatientSelected()) {
                        } else {
                            MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                            UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isLoggedInUser()) {
                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                } else if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isChecked()) {
                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                } else if (role.contains("Patient")) {
                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).isPatientSelected()) {
                    } else {
                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID());
                    }
                }
            }

        }

        HashSet hs = new HashSet();
        hs.addAll(UserIDs);
        UserIDs.clear();
        UserIDs.addAll(hs);
      /*  if (UserIDs!=null) {
            if (UserIDs.size()>0) {
                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout = new ArrayList();
                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.addAll(UserIDs);
            }
        }*/

        boolean contains = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(meetList.getUserID());
        if (contains) {
            printLog("remove");

            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(getApplicationContext(), networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    if (response.getStatus().equals("1")) {

                        MeetInviteParticipantsWithoutEpisodeModel.UserList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                }
                            }

                            int i = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(meetList.getUserID());
                            if (i >= 0)
                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(i);
                            int user = UserIDs.indexOf(meetList.getUserID());
                            if (user >= 0) {
                                UserIDs.remove(user);
                            }
                            setAdapterWithOutEpisode();
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(episodeParticipantForDesignate.getUserID())) {
                                //showDialogWithOkButton1(text);

                                final MeetInviteParticipantsWithoutEpisodeModel.UserList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton1("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                                //MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(finalEpisodeParticipantForDesignate.getUserID());
                                                printLog("checked provider");
                                                int r = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(r);
                                                int user = UserIDs.indexOf(meetList.getUserID());
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                                String designate_id = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getDesignate_Id();

                                                r = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(designate_id);
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(r);
                                                user = UserIDs.indexOf(designate_id);
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                            }

                                        /*    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                                //MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                printLog("checked designate");
                                                int r = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(r);
                                                int user = UserIDs.indexOf(meetList.getUserID());
                                                if (user >= 0) {
                                                    UserIDs.remove(user);
                                                }
                                            }*/
                                        }
                                        setAdapterWithOutEpisode();
                                    }
                                });


                            } else {
                                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(i);
                                int user = UserIDs.indexOf(meetList.getUserID());
                                if (user >= 0) {
                                    UserIDs.remove(user);
                                }

                                setAdapterWithOutEpisode();
                            }
                        }
                    } else {

                        if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {


                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton1(text);
                            }

                        } else {
                            int r = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.indexOf(meetList.getUserID());
                            if (r >= 0)
                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.remove(r);
                            int user = UserIDs.indexOf(meetList.getUserID());
                            if (user >= 0) {
                                UserIDs.remove(user);
                            }

                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                }
                            }

                            setAdapterWithOutEpisode();
                        }
                    }
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onError(int error) {

                }
            });
        }


        /*For checked*/
        else {
            printLog("add");

            if (meetList.isDesignate_Exist()) {

                NetworkAdapter networkAdapter = new NetworkAdapter();
                networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                    @Override
                    public void onSuccess(final CheckProviderResponse response) {

                        boolean containsAlready = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getDesignate_UserID());

                        if (containsAlready) {
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                /*if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }*/
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }

                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getDesignate_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }
                            }
                            setAdapterWithOutEpisode();
                        } else {
                            showDialogWithOkCancelButton1(getResources().getString(R.string.designate_available_message), new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                        if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                            meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                            meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                            MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                            printLog("checked provider");
                                        }

                                        if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                            meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                            MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            printLog("checked designate");
                                        }
                                    }
                                    UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                    UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                    setAdapterWithOutEpisode();
                                }
                            });

                        }

                    }

                    @Override
                    public void onFailure() {
                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                UserIDs.add(meetList.getUserID());
                            }
                        }
                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                        setAdapterWithOutEpisode();
                    }

                    @Override
                    public void onError(int error) {

                    }
                });
            } else {
                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                        UserIDs.add(meetList.getUserID());
                    }
                }
                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                setAdapterWithOutEpisode();
            }
        }


    }

    private void setAdapterWithOutEpisode() {
        recyclerViewDialog.setAdapter(null);
        if (meetInviteParticipantsWithoutEpisodeModel != null) {
            MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getApplicationContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {

                    setAdapterWithOutEpisode(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }


    public void filterInviteesWithOutEpisode(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        meetInviteParticipantsWithoutEpisodeModelSearch = new ArrayList<>();
        if (charText.equals("")) {
            meetInviteParticipantsWithoutEpisodeModelSearch = meetInviteParticipantsWithoutEpisodeModel.getUserList();
        } else {
            meetInviteParticipantsWithoutEpisodeModelSearch = new ArrayList<>();
            for (MeetInviteParticipantsWithoutEpisodeModel.UserList cs : meetInviteParticipantsWithoutEpisodeModel.getUserList()) {
                if (cs.getFullName().toLowerCase().contains(charText)) {
                    meetInviteParticipantsWithoutEpisodeModelSearch.add(cs);
                }
            }

        }
        if (meetInviteParticipantsWithoutEpisodeModelSearch.size() > 0)
            notifyDataSetChangedInviteesWithOutEpisode();
        else
            recyclerViewDialog.setAdapter(null);
    }

    private void notifyDataSetChangedInviteesWithOutEpisode() {

        MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModelSearch, getApplicationContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisode(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapterWithoutEpisode);
    }


    private void createMeet(final Activity activity) {
        context = activity.getApplicationContext();
        Analytics.with(getApplicationContext()).track("Start eVisit", new Properties().putValue("category", "Mobile"));

        if (isConnectedToNetwork(this)) {
            try {
                String type = null;

                if (meetingTitle.getText().toString().trim().equals("")) {
                    showDialogWithOkButton("Please enter meeting title");
                    return;
                }
                switch (selectedTypePatient) {
                    case 1:
                        Episode_Care_Plan_Name = "";
                        Episode_Care_PlanID = "";
                        PatientID = "";
                        Patient_UserID = "";
                        type = "Provider to Provider";
                        break;
                    case 2: {
                        int position = spinner_episode_name.getSelectedItemPosition();
                        int positionPatient = spinnerPatientName.getSelectedItemPosition();
                        if (positionPatient == 0) {
                            showDialogWithOkButton("Please select Patient");
                            return;
                        } else if (position == 0) {
                            showDialogWithOkButton("Please select episode");
                            return;
                        }
                        type = "Patient Related Episode";
                        break;
                    }
                    case 3: {
                        int positionPatient = spinner_no_episode_PatientName.getSelectedItemPosition();
                        if (positionPatient == 0) {
                            showDialogWithOkButton("Please select Patient");
                            return;
                        }
                        Episode_Care_Plan_Name = "";
                        Episode_Care_PlanID = "";
                        type = "Provider Patient With No Episode";
                        break;
                    }
                    case 4:
                        Episode_Care_Plan_Name = "";
                        Episode_Care_PlanID = "";
                        PatientID = "";
                        Patient_UserID = "";
                        type = "Provider With Multiple Patient";
                        break;
                }

                if (UserIDs.size() <= 0) {
                    showDialogWithOkButton("Please select Invitees");
                    return;
                }

                alertDialogMeetNow.dismiss();
                showProgressDialog(true);

                TimeZone tz = TimeZone.getDefault();
                Date now = new Date();
                int offsetFromUtc = (tz.getOffset(now.getTime()) / 1000) * (-1);

                MeetNowDTO.MeetDetails meetDetails = new MeetNowDTO().new MeetDetails();

                meetDetails.setBinder_id(mChat.getId());
                meetDetails.setPatient_UserID(Patient_UserID);
                meetDetails.setEpisode_Care_PlanID(Episode_Care_PlanID);
                meetDetails.setName(Name);
                meetDetails.setUserIDs(UserIDs);
                meetDetails.setEpisode_Care_Plan_Name(Episode_Care_Plan_Name);
                meetDetails.setMeeting_Type(type);
                meetDetails.setPatientID(PatientID);
                meetDetails.setAgenda("");
                meetDetails.setOffSet(offsetFromUtc + "");

                MeetNowDTO meetNowDTO = new MeetNowDTO();
                meetNowDTO.setMeetDetails(meetDetails);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(meetNowDTO));
                printLog("getAgenda:" + meetDetails.getAgenda());
                printLog("getBinder_id:" + meetDetails.getBinder_id());
                printLog("getEpisode_Care_Plan_Name:" + meetDetails.getEpisode_Care_Plan_Name());
                printLog("getEpisode_Care_PlanID:" + meetDetails.getEpisode_Care_PlanID());
                printLog("getMeeting_Type:" + meetDetails.getMeeting_Type());
                printLog("getName:" + meetDetails.getName());
                printLog("getPatient_UserID:" + meetDetails.getPatient_UserID());
                printLog("getPatientID:" + meetDetails.getPatientID());
                printLog("getUserIDs:" + meetDetails.getUserIDs());

                //printLog("SendingValue:" + requestJson.toString());
                networkRequestUtil.postDataSecure(BASE_URL + URL_MESSAGE_STARTMEET, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("response:Create Meet" + response);
                        MeetNowResponse messageCreateConversationResponse = new Gson().fromJson(response.toString(), MeetNowResponse.class);
                        if (messageCreateConversationResponse != null) {
                            if (messageCreateConversationResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                MyApplication.getInstance().addToSharedPreference(SESSION_KEY, messageCreateConversationResponse.getSession_key());
                                ArrayList<User> userList = new ArrayList<>();
                                //userList.addAll(chat.getMembers());
                                String topic = messageCreateConversationResponse.getMessage();
                                MeetEventActivity.startMeet(activity, topic, userList);

                            } else
                                showDialogWithOkButton(messageCreateConversationResponse.getMessage());
                        } else {
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

    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(this, R.color.colorPrimary)}
        );
    }

    private ColorStateList deSelectColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(this, R.color.black)}
        );
    }


    public void showDialogWithOkCancelButton1(String message, final OnOkClick onOkClick) {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok_cancel, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button yes_btn = (Button) dialogView.findViewById(R.id.yes_btn);
        Button cancel_btn = (Button) dialogView.findViewById(R.id.cancel_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClick.OnOkClicked();
                alertDialog.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        alertDialog.show();


    }


    public void showDialogWithOkButton1(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView TextViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Window window = alertDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmlp = window.getAttributes();
            wmlp.windowAnimations = R.style.dialog_animation;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(wmlp);
        }
        alertDialog.show();
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
        startService(new Intent(this, BackgroundTrackingService.class));
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        startService(new Intent(this, Time_out_services.class));
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(ChatActivity.this, LoginActivity.class);
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


}
