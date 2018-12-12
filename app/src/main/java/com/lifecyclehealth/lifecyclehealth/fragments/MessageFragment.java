package com.lifecyclehealth.lifecyclehealth.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.ChatActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.ChatListAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithoutEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MessageDialogMeetAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.designate.GlobalDesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.dto.CreateMessageConversationDTO;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.GlobalCheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsWithoutEpisodeModel;
import com.lifecyclehealth.lifecyclehealth.model.MessageAcknowledgementResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageBusinessHoursResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageCreateConversationResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageEpisodeListResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageMeetModel1;
import com.lifecyclehealth.lifecyclehealth.model.MessagePatientListModel;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkAdapter;
import com.moxtra.sdk.chat.controller.ChatController;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.common.BaseRepo;
import com.moxtra.sdk.meet.model.Meet;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_NAME;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_BUSINESS_HOURS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_ACKNOWLEDGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_CREATE_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_GET_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_PATIENT_LIST;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SET_MESSAGE_ACKNOWLEDGE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.messageCount;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

public class MessageFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    //private RecyclerView recyclerView;
    private RecyclerView recyclerView;
    private static final String TAG = "DEMO_ChatList";

    private static View view;
    private static View viewGlobal;

    // private MeetRepo mMeetRepo;
    private ChatController mChatController;
    private ChatListAdapter mAdapter;
    private String selectedEpisode = "";

    @Override
    String getFragmentTag() {
        return "MessageFragment";
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        if (parent != null) {
            parent.removeAllViews();
            parent.clearDisappearingChildren();
        }
        return inflater.inflate(R.layout.fragment_message, parent, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {
        this.view = view;
       /* MainActivity.bottomBar.selectTabWithId(R.id.tab_message);
        MainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_message);*/
        Analytics.with(getContext()).screen("Conversation");
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.title_message));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        ImageView create_conversation_button = (ImageView) view.findViewById(R.id.create_conversation_button);
        create_conversation_button.setOnClickListener(createConversationLayoutListener);
        callGetList();

    }


    private View.OnClickListener createConversationLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //checkMessageAcknowledge();

            new MessageData().run();

        }
    };


    class MessageData extends Thread {
        @Override
        public void run() {
            super.run();
            checkBusinessHours();
        }
    }

    private void checkBusinessHours() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_BUSINESS_HOURS, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get message business hours:" + response);

                        if (response != null) {
                            MessageBusinessHoursResponse messageAcknowledgementResponse = new Gson().fromJson(response.toString(), MessageBusinessHoursResponse.class);
                            if (messageAcknowledgementResponse != null) {
                                if (messageAcknowledgementResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    if (messageAcknowledgementResponse.isShowPopup == true && messageAcknowledgementResponse.message.trim().length() == 0) {
                                        checkMessageAcknowledge();
                                    } else {
                                        if (messageAcknowledgementResponse.isShowPopup == true && messageAcknowledgementResponse.isShowCancelButton() == true) {
                                            showDialogWithOkCancelButton(messageAcknowledgementResponse.getMessage(), new OnOkClick() {
                                                @Override
                                                public void OnOkClicked() {
                                                    checkMessageAcknowledge();
                                                }
                                            });
                                        } else if (messageAcknowledgementResponse.isShowPopup == true && messageAcknowledgementResponse.isShowCancelButton() == false) {
                                            showDialogWithOkButton(messageAcknowledgementResponse.getMessage());
                                        } else if (messageAcknowledgementResponse.isShowPopup == false && messageAcknowledgementResponse.isShowCancelButton() == false) {
                                            showDialogWithOkButton(messageAcknowledgementResponse.getMessage());
                                        }
                                    }

                                } else {

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

    private void checkMessageAcknowledge() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_ACKNOWLEDGE, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get message acknowledge:" + response);

                        if (response != null) {
                            MessageAcknowledgementResponse messageAcknowledgementResponse = new Gson().fromJson(response.toString(), MessageAcknowledgementResponse.class);
                            if (messageAcknowledgementResponse != null) {
                                if (messageAcknowledgementResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    if (messageAcknowledgementResponse.getIsMessage_Acknowledged()) {
                                        showDialogCreateConversation(getActivity());
                                    } else {
                                        dialogMessageAcknowledge();
                                    }

                                } else {
                                    showDialogCreateConversation(getActivity());
                                }
                            } else
                                showDialogCreateConversation(getActivity());
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

    boolean checkAck;

    private void dialogMessageAcknowledge() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_message_acknowledge, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        Button buttonOk = (Button) dialogView.findViewById(R.id.btnContinue);
        Button buttonCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        AppCompatRadioButton radioButton = (AppCompatRadioButton) dialogView.findViewById(R.id.radio_message_check_acknowledge);
        checkAck = false;
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAck = true;
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAck) {
                    setMessageAcknowledge();
                } else {
                    alertDialog.dismiss();
                    showDialogCreateConversation(getActivity());
                }

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }


    private void setMessageAcknowledge() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                HashMap<String, Boolean> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("IsMessage_Acknowledged", true);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(stringStringHashMap));

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_SET_MESSAGE_ACKNOWLEDGE + "/true", null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of set message acknowledge:" + response);
                        showDialogCreateConversation(getActivity());

                     /*   if (response != null) {
                            MessageAcknowledgementResponse messageAcknowledgementResponse = new Gson().fromJson(response.toString(), MessageAcknowledgementResponse.class);
                            if (messageAcknowledgementResponse != null) {
                                if (messageAcknowledgementResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    showDialogCreateConversation(getActivity());
                                } else {
                                    //showDialogWithOkButton(messageAcknowledgementResponse.getMessage());
                                }
                            } else
                                showDialogWithOkButton(getString(R.string.error_someting_went_wrong));
                        }*/
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

    private void callGetList() {
        getDataChat();
    }


    ArrayList<MessageMeetModel1> messageMeetModel;

    private void setAdapterUpdate() {
        if (mainActivity.meetPushNotification) {
            new asyncMeetList().execute();
        } else {
            messageMeetModel = new ArrayList<>();
            if (mainActivity.meetLists != null) {
                if (mainActivity.meetLists.size() > 0) {
                    for (Meet meetListMessage : mainActivity.meetLists) {
                        // boolean status = !meetListMessage.isInProgress() && !(meetListMessage.getStartTime() > 0 && System.currentTimeMillis() < meetListMessage.getEndTime());
                        // if (status == false) {
                        MessageMeetModel1 model = new MessageMeetModel1();
                        model.setChatType(0);
                        model.setMeetList(meetListMessage);
                        messageMeetModel.add(model);
                        // }
                        //boolean status = !meetListMessage.isInProgress() && !(meetListMessage.getScheduleStartTime() > 0 && System.currentTimeMillis() < meetListMessage.getScheduleEndTime());
                        printLog("getID :" + meetListMessage.getID());
                        printLog("getEndTime " + meetListMessage.getEndTime() + "");
                        printLog("getScheduleEndTime : " + meetListMessage.getScheduleEndTime() + "");
                        //printLog("get status: " + status);

                    }
                }
            }

            if (mainActivity.chatList != null) {
                if (mainActivity.chatList.size() > 0) {
                    for (Chat chat : mainActivity.chatList) {
                        MessageMeetModel1 model = new MessageMeetModel1();

                        model.setChatType(1);
                        model.setChatList(chat);
                        messageMeetModel.add(model);
                    }
                }
            }
            if (messageMeetModel.size() > 0) {
                mAdapter.updateChats(messageMeetModel, mainActivity.chatList);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMessageCount();
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
        //MainActivity.getInstance().setViewNotificationView();

    }


    private class asyncCreateText extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected Void doInBackground(Void... unused) {

            mainActivity.chatList = mainActivity.mChatRepo.getList();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            showProgressDialog(false);
            setAdapterUpdate();
        }
    }


    private class asyncMeetList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected Void doInBackground(Void... unused) {

            if (mainActivity.meetPushNotification) {
                mainActivity.mMeetRepo.setOnChangedListener(new BaseRepo.OnRepoChangedListener<Meet>() {
                    @Override
                    public void onCreated(List<Meet> items) {
                        Log.d(TAG, "Meet: onCreated");
                        mainActivity.meetPushNotification = false;
                    }

                    @Override
                    public void onUpdated(List<Meet> items) {
                        for (Meet meet : items) {
                            if (!isEnded(meet)) {
                                if (!meet.isAccepted())
                                    mainActivity.meetLists.add(meet);
                            }
                        }
                    }

                    @Override
                    public void onDeleted(List<Meet> items) {
                        Log.d(TAG, "Meet: onDeleted");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            showProgressDialog(false);
            mainActivity.meetPushNotification = false;
            setAdapterUpdate();
        }
    }

    private static boolean isEnded(Meet meet) {
        return !meet.isInProgress() && !(meet.getScheduleStartTime() > 0 && System.currentTimeMillis() < meet.getScheduleEndTime());
    }


    private void getDataChat() {
        mAdapter = new ChatListAdapter(getContext());
        if (mainActivity.chatList != null) {
            if (mainActivity.chatList.size() > 0) {
                setAdapterUpdate();
            } else {
                new asyncCreateText().execute();
            }
        } else {
            new asyncCreateText().execute();
        }

        mainActivity.mChatRepo.setOnChangedListener(new BaseRepo.OnRepoChangedListener<Chat>() {
            @Override
            public void onCreated(List<Chat> items) {
                Log.d(TAG, "Chat: onCreated");
                mainActivity.chatList = mainActivity.mChatRepo.getList();
                setAdapterUpdate();
            }

            @Override
            public void onUpdated(List<Chat> items) {
                Log.d(TAG, "Chat: onUpdated");
                mainActivity.chatList = mainActivity.mChatRepo.getList();
                setAdapterUpdate();
            }

            @Override
            public void onDeleted(List<Chat> items) {
                Log.d(TAG, "Chat: onDeleted");
                mainActivity.chatList = mainActivity.mChatRepo.getList();
                setAdapterUpdate();
            }
        });

    }


    @Override
    public void onDetach() {
        super.onDetach();
     /*   if (mChatController != null) {
            mChatController.cleanup();
            mChatController = null;
        }*/
      /*  if (mChatRepo != null) {
            mChatRepo.cleanup();
        }*/
    }


    /*Create conversation*/

    String Message, Patient_UserID, PatientID, Title, Episode_Care_PlanID, Name, Message_Type;
    ArrayList<String> UserIDs = new ArrayList<>();
    private Button btnInvite, btnCancel;
    private EditText nameOfConversation;
    private Spinner spinner_no_episode_PatientName, spinner_episode_name, spinnerPatientName;
    private TextView txt_patient_name, txt_episode_name, txt_no_episode_patient_name;
    private AppCompatRadioButton patient_related_episode, provider_patient_with_no_episode, provider_to_provider;
    private LinearLayout linear_provider_patient_with_no_episode, linear_patient_related_episode;
    private RadioGroup radioGroup;
    private ArrayList<String> PatientNameList = new ArrayList<>();
    private ArrayList<String> PatientNameListUserID = new ArrayList<>();
    private ArrayList<String> PatientNameListId = new ArrayList<>();
    private ArrayList<String> EpisodeNameList = new ArrayList<>();
    private ArrayList<String> EpisodeStatusList = new ArrayList<>();
    private ArrayList<String> EpisodeNameListId = new ArrayList<>();
    private boolean isPatient;
    private int selectedTypePatient = 0;
    private MeetInviteParticipantsModel meetInviteParticipantsModelMeetNow;
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelMeetNowSearch;
    private MeetInviteParticipantsWithoutEpisodeModel meetInviteParticipantsWithoutEpisodeModel;
    ArrayList<MeetInviteParticipantsWithoutEpisodeModel.UserList> meetInviteParticipantsWithoutEpisodeModelSearch;

    /*private MeetInviteParticipantsWithoutEpisodeModel meetInviteParticipantsWithoutEpisodeModelGlobal;
    ArrayList<MeetInviteParticipantsWithoutEpisodeModel.UserList> meetInviteParticipantsWithoutEpisodeModelSearchGlobal;*/

    android.support.v7.app.AlertDialog alertDialogCreateConversation;

    @SuppressLint("RestrictedApi")
    public void showDialogCreateConversation(Activity activity) {
        UserIDs = new ArrayList<String>();
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.meet_dialog_create_conversation, null);
        builder.setView(dialogView);
        alertDialogCreateConversation = builder.create();

        btnInvite = (Button) dialogView.findViewById(R.id.btnInvite);
        btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        nameOfConversation = (EditText) dialogView.findViewById(R.id.nameOfConversation);
        spinner_no_episode_PatientName = (Spinner) dialogView.findViewById(R.id.spinner_no_episode_PatientName);
        spinner_episode_name = (Spinner) dialogView.findViewById(R.id.spinner_episode_name);
        spinnerPatientName = (Spinner) dialogView.findViewById(R.id.spinnerPatientName);
        txt_patient_name = (TextView) dialogView.findViewById(R.id.txt_patient_name);
        txt_episode_name = (TextView) dialogView.findViewById(R.id.txt_episode_name);
        txt_no_episode_patient_name = (TextView) dialogView.findViewById(R.id.txt_no_episode_patient_name);
        patient_related_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.patient_related_episode);
        provider_to_provider = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_to_provider);
        provider_patient_with_no_episode = (AppCompatRadioButton) dialogView.findViewById(R.id.provider_patient_with_no_episode);
        linear_provider_patient_with_no_episode = (LinearLayout) dialogView.findViewById(R.id.linear_provider_patient_with_no_episode);
        linear_patient_related_episode = (LinearLayout) dialogView.findViewById(R.id.linear_patient_related_episode);


        if (isPatient) {
            provider_to_provider.setVisibility(View.GONE);
        } else {
            provider_to_provider.setVisibility(View.VISIBLE);
        }

        Message_Type = "Patient Related Episode";
        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
        provider_to_provider.setSupportButtonTintList(deSelectColorList());
        patient_related_episode.setSupportButtonTintList(getColorList());
        patient_related_episode.setChecked(true);
        selectedTypePatient = 1;
        linear_patient_related_episode.setVisibility(View.VISIBLE);
        linear_provider_patient_with_no_episode.setVisibility(View.GONE);

        Analytics.with(getContext()).screen("MoxtraCreateConversation");
        Analytics.with(getContext()).track("Invite Participant for conversation", new Properties().putValue("category", "Mobile"));

        radioGroup = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.patient_related_episode:
                        Message_Type = "Patient Related Episode";
                        patient_related_episode.setSupportButtonTintList(getColorList());
                        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
                        provider_to_provider.setSupportButtonTintList(deSelectColorList());
                        patient_related_episode.setChecked(true);
                        selectedTypePatient = 1;
                        linear_patient_related_episode.setVisibility(View.VISIBLE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        break;

                    case R.id.provider_patient_with_no_episode:
                        Message_Type = "Provider Patient With No Episode";
                        provider_patient_with_no_episode.setChecked(true);
                        provider_patient_with_no_episode.setSupportButtonTintList(getColorList());
                        patient_related_episode.setSupportButtonTintList(deSelectColorList());
                        provider_to_provider.setSupportButtonTintList(deSelectColorList());
                        selectedTypePatient = 2;
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.VISIBLE);
                        break;

                    case R.id.provider_to_provider:
                        Message_Type = "Provider to Provider";
                        provider_to_provider.setChecked(true);
                        provider_to_provider.setSupportButtonTintList(getColorList());
                        patient_related_episode.setSupportButtonTintList(deSelectColorList());
                        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
                        selectedTypePatient = 3;
                        linear_patient_related_episode.setVisibility(View.GONE);
                        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIDs = new ArrayList<String>();
                alertDialogCreateConversation.dismiss();
            }
        });

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Analytics.with(getContext()).screen("InviteeListForConversation");
                Analytics.with(getContext()).track("OK_createConversation", new Properties().putValue("category", "Mobile"));

                switch (selectedTypePatient) {
                    case 0: {
                        break;
                    }
                    case 1: {

                        int position = spinner_episode_name.getSelectedItemPosition();
                        Name = EpisodeNameList.get(position);
                        Episode_Care_PlanID = EpisodeNameListId.get(position);
                        if (nameOfConversation.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter name of conversation");
                            return;
                        } else if (Patient_UserID.equals("-99")) {
                            showDialogWithOkButton("Please select Patient");
                            return;
                        } else if (position == 0) {
                            showDialogWithOkButton("Please select episode");
                            return;
                        } else {
                            selectedEpisode = EpisodeNameListId.get(position);
                        }
                        Title = nameOfConversation.getText().toString();
                        if (isPatient) {
                            getInviteesWithEpisodeGlobal();
                        } else {
                            getInviteesWithEpisode();
                        }

                        break;
                    }
                    case 2: {
                        selectedEpisode = "";
                        Name = "";
                        Episode_Care_PlanID = "";

                        if (nameOfConversation.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter name of conversation");
                            return;
                        } else if (Patient_UserID.equals("-99")) {
                            showDialogWithOkButton("Please select Patient");
                        }
                        Title = nameOfConversation.getText().toString();
                        if (isPatient) {
                            getInviteesWithOutEpisodeGlobal(PatientID);
                        } else {
                            getInviteesWithOutEpisode(PatientID);
                        }
                        break;
                    }

                    case 3: {
                        selectedEpisode = "";
                        Name = "";
                        Episode_Care_PlanID = "";

                        if (nameOfConversation.getText().toString().trim().equals("")) {
                            showDialogWithOkButton("Please enter name of conversation");
                            return;
                        }
                        Title = nameOfConversation.getText().toString();
                        getInviteesWithOutEpisode("");
                        break;
                    }

                }

            }
        });

        PatientNameList = new ArrayList<>();
        PatientNameListId = new ArrayList<>();
        PatientNameListUserID = new ArrayList<>();
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        if (isPatient) {
            PatientID = "";
            Patient_UserID = "";
            //PatientNameList.add(MyApplication.getInstance().getFromSharedPreference(LOGIN_NAME));
            try {
                PatientNameList.add(AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_NAME)));
                PatientNameListId.add(AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID)));
                PatientNameListUserID.add(AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID)));
            } catch (Exception e) {
                e.printStackTrace();
            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, PatientNameList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPatientName.setAdapter(dataAdapter);
            spinner_no_episode_PatientName.setAdapter(dataAdapter);
            callPatientEpisodeList("P");
        } else {
            getPatientList();

        }


        spinnerPatientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                PatientID = "";
                if (PatientNameList.size() > 0) {
                    PatientID = PatientNameListId.get(position);
                    Patient_UserID = PatientNameListUserID.get(position);
                    if (Patient_UserID.equals("-99")) {
                        EpisodeStatusList = new ArrayList<String>();
                        EpisodeNameList = new ArrayList<String>();
                        EpisodeNameListId = new ArrayList<String>();
                        EpisodeNameList.add("Select Episode");
                        EpisodeNameListId.add("-99");
                        EpisodeStatusList.add("-99");

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, EpisodeNameList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_episode_name.setAdapter(dataAdapter);
                    } else {
                        callPatientEpisodeList(PatientNameListId.get(position));
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                if (PatientNameList.size() > 2) {

                }
            }
        });


        spinner_no_episode_PatientName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PatientID = "";
                printLog("position : " + position);
                if (position > 0) {
                    PatientID = PatientNameListId.get(position);
                    Patient_UserID = PatientNameListUserID.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Window window = alertDialogCreateConversation.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialogCreateConversation.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(lp);

        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogCreateConversation.show();
    }


    private void getPatientList() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_PATIENT_LIST, new VolleyCallback() {
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
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, PatientNameList);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerPatientName.setAdapter(dataAdapter);
                                    spinner_no_episode_PatientName.setAdapter(dataAdapter);
                                    if (PatientNameList.size() > 0) {
                                        callPatientEpisodeList(PatientNameListId.get(0));
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


    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(mainActivity, R.color.colorPrimary)}
        );
    }

    private ColorStateList deSelectColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(mainActivity, R.color.black)}
        );
    }

    private void getInviteesWithEpisode() {
        showProgressDialog(true);
        String url = null;
        if (!selectedEpisode.equals("")) {
            url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_EPISODE + "/" + selectedEpisode;
        } else {
            try {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                printLog("url" + url);
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
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
                                                    networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID(), new DesignateCallBack() {
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

                                                            adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

                                                                    setAdapterWithEpisodeMeet(meetList, Type, pos);

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
                                        showDialogInviteesWithEpisode(getActivity());
                                    }
                                } else {
                                    //showDialogWithOkButton(meetInviteParticipantsModelMeetNow.getMessage());
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

    private void getInviteesWithEpisodeGlobal() {
        showProgressDialog(true);
        String url = null;
        if (!selectedEpisode.equals("")) {
            url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_EPISODE + "/" + selectedEpisode;
        } else {
            try {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isConnectedToNetwork(mainActivity)) {
            try {
                printLog("url" + url);
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
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
                                                    networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID(), new DesignateCallBack() {
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

                                                            adapterGlobal = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

                                                                    setAdapterWithEpisodeMeetGlobal(meetList, Type, pos);

                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapterGlobal);
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
                                        showDialogInviteesWithEpisodeGlobal(getActivity());
                                    }
                                } else {
                                    //showDialogWithOkButton(meetInviteParticipantsModelMeetNow.getMessage());
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


    private void getInviteesWithOutEpisode(String PatientID) {
        showProgressDialog(true);
        String url = null;
        if (PatientID == "") {
            try {
                url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + AESHelper.decrypt(seedValue, MyApplication.getInstance().getFromSharedPreference(LOGIN_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + PatientID;
        }
        if (selectedTypePatient == 3) {
            url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
        }

        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
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
                                                    networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {
                                                            for (int k = 0; k < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); k++) {
                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
                                                                }

                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
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
                                                            adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
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

                                        showDialogInviteesWithOutEpisode(getActivity());
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


    private void getInviteesWithOutEpisodeGlobal(String PatientID) {
        showProgressDialog(true);
        String url = BASE_URL + URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE + "/" + PatientID;

        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
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
                                                    networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {
                                                            for (int k = 0; k < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); k++) {
                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
                                                                }

                                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).setChecked(true);
                                                                    UserIDs.add(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(k).getUserID());
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
                                                            adapterWithoutEpisodeGlobal = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                                                                    setAdapterWithOutEpisodeGlobal(item, Type, pos);
                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapterWithoutEpisodeGlobal);
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

                                        showDialogInviteesWithOutEpisodeGlobal(getActivity());
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

    android.support.v7.app.AlertDialog alertDialogInitialMessage;

    private void initialMessageDialog(Activity activity) {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.initial_message_dialog, null);
        builder.setView(dialogView);
        alertDialogInitialMessage = builder.create();


        final EditText initialMessage = (EditText) dialogView.findViewById(R.id.initialMessage);
        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (initialMessage.getText().toString().trim().equals("")) {
                    showDialogWithOkButton("Please provide initial message");
                    return;
                }
                Analytics.with(getContext()).screen("InitialMessageForConversation");
                Analytics.with(getContext()).track("InitialMessage_btnOK", new Properties().putValue("category", "Mobile").putValue("intialMessage", initialMessage.getText().toString().trim()));


                createConversation(initialMessage.getText().toString().trim());

                alertDialogInitialMessage.dismiss();
                alertDialogInitialMessage.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogInitialMessage.dismiss();
                alertDialogInitialMessage.cancel();
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

    private void createConversation(String initialMessage) {
        Message = initialMessage;

        //alertDialogCreateConversation.dismiss();
        alertDialogInitialMessage.dismiss();
        dialog.dismiss();


        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                CreateMessageConversationDTO createMessageConversationDTO = new CreateMessageConversationDTO();
                CreateMessageConversationDTO.BinderDetails binderDetails = new CreateMessageConversationDTO().new BinderDetails();
                binderDetails.setEpisode_Care_PlanID(Episode_Care_PlanID);
                binderDetails.setName(Name);
                binderDetails.setPatient_UserID(Patient_UserID);
                binderDetails.setTitle(Title);
                binderDetails.setPatientID(PatientID);
                binderDetails.setMessage_Type(Message_Type);

                createMessageConversationDTO.setMessage(Message);
                createMessageConversationDTO.setUserIDs(UserIDs);
                createMessageConversationDTO.setBinderDetails(binderDetails);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(createMessageConversationDTO));

                mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_MESSAGE_CREATE_CONVERSATION, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        printLog("response:Create Conversation" + response);
                        MessageCreateConversationResponse messageCreateConversationResponse = new Gson().fromJson(response.toString(), MessageCreateConversationResponse.class);
                        if (messageCreateConversationResponse != null) {
                            if (messageCreateConversationResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                for (Chat chat : mainActivity.mChatRepo.getList()) {
                                    printLog("Id: " + chat.getId());
                                    if (chat.getId().equals(messageCreateConversationResponse.getBinderID())) {
                                        showProgressDialog(false);
                                        Analytics.with(getContext()).track("conversation Read", new Properties().putValue("category", "Mobile")
                                                .putValue("conversationID", chat.getId())
                                                .putValue("conversationName", chat.getTopic()));
                                        ChatActivity.showChat(getActivity(), chat);
                                    }
                                }

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


    RecyclerView recyclerViewDialog;
    MeetInviteParticipantsWithEpisodeAdapter adapter;
    MeetInviteParticipantsWithEpisodeAdapter adapterGlobal;
    MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode;
    MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisodeGlobal;
    MeetInviteParticipantsWithoutEpisodeModel.UserList userList = null;
    MeetInviteParticipantsModel.EpisodeParticipantList EpisodeList = null;

    Dialog dialog;

    /* With Episode*/
    private void showDialogInviteesWithEpisode(Context view) {

        alertDialogCreateConversation.dismiss();
        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

        final EditText search = (EditText) dialog.findViewById(R.id.search);
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
                    filterInviteesWithEpisode(searchString);
                }

            }
        });

        adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs,
                new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

                        setAdapterWithEpisodeMeet(meetList, Type, pos);

                    }
                });
        recyclerViewDialog.setAdapter(adapter);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModelMeetNow != null) {
                    MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                            setAdapterWithEpisodeMeet(item, Type, pos);

                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialog.findViewById(R.id.btn_start_message);
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.size() > 0) {
                    //submitInviteList();
                    //UserIDs = TextUtils.join(",", MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant);
                    UserIDs = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant;
                    if (UserIDs.size() > 1)
                        initialMessageDialog(getActivity());
                    else
                        Toast.makeText(getContext(), "At least one invitee is required", Toast.LENGTH_SHORT).show();
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
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    if (response.getStatus().equals("0")) {
                        // if (response.getStatus().equals("1") || response.getStatus().equals("0")) {

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
                                showDialogWithOkCancelButton("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                                //MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getUserID());
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

                        if (response.getDesignateList().size() > 0 && MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton(text);
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
                networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
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
                            showDialogWithOkCancelButton(getResources().getString(R.string.designate_available_message), new OnOkClick() {
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
            MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                    setAdapterWithEpisodeMeet(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }

    public void filterInviteesWithEpisode(String charText) {

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
            notifyDataSetChangedInviteesWithEpisode();
        else
            recyclerViewDialog.setAdapter(null);
    }

    private void notifyDataSetChangedInviteesWithEpisode() {

        MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNowSearch, getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisodeMeet(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapter);
    }


    //Global

    private void showDialogInviteesWithEpisodeGlobal(Context view) {

        alertDialogCreateConversation.dismiss();
        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

        final EditText search = (EditText) dialog.findViewById(R.id.search);
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
                    filterInviteesWithEpisode(searchString);
                }

            }
        });

        adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs,
                new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

                        setAdapterWithEpisodeMeetGlobal(meetList, Type, pos);

                    }
                });
        recyclerViewDialog.setAdapter(adapter);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModelMeetNow != null) {
                    MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                            setAdapterWithEpisodeMeetGlobal(item, Type, pos);

                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialog.findViewById(R.id.btn_start_message);
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.size() > 0) {
                    //submitInviteList();
                    //UserIDs = TextUtils.join(",", MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant);
                    UserIDs = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant;
                    if (UserIDs.size() > 1)
                        initialMessageDialog(getActivity());
                    else
                        Toast.makeText(getContext(), "At least one invitee is required", Toast.LENGTH_SHORT).show();
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

    private void setAdapterWithEpisodeMeetGlobal(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {


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
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isPatientSelected()) {
                        } else {
                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                            UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
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

        boolean contains = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(meetList.getUserID());
        if (contains) {

            if (meetList.isLoggedInUser()) {
                return;
            }
            printLog("remove");

            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    //if (response.getStatus().equals("1") || response.getStatus().equals("0")) {
                    if ((response.getStatus().equals("1") || response.getStatus().equals("0"))) {

                        MeetInviteParticipantsModel.EpisodeParticipantList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            String name = null;

                            if (response.getDesignateList() != null && response.getDesignateList().size() > 0 && MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                    }
                                }

                                if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                    String text = meetList.getFullName() + " is designate for " + name;
                                    showDialogWithOkButton(text);
                                }

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
                                setAdapterWithEpisodeMeetGlobal();
                            }
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(episodeParticipantForDesignate.getUserID())) {
                                String text = name + " is designate for " + meetList.getFullName();
                                //showDialogWithOkButton1(text);

                                final MeetInviteParticipantsModel.EpisodeParticipantList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton("Designate will also be removed", new OnOkClick() {
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

                                        }
                                        setAdapterWithEpisodeMeetGlobal();
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

                                setAdapterWithEpisodeMeetGlobal();
                            }
                        }
                    } else if (response.getDesignateList() != null && response.getDesignateList().size() > 0) {

                        if (response.getDesignateList().size() > 0 && MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton(text);
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

                            setAdapterWithEpisodeMeetGlobal();
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

                        setAdapterWithEpisodeMeetGlobal();
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
            networkAdapter.checkProviderListGlobal(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new GlobalDesignateCallBack() {
                @Override
                public void onSuccess(final GlobalCheckProviderResponse response) {
                    printLog("response:" + response);
                    if (response.getDesignateList().getOrganisation_DesignateID() != null) {
                        switch (response.getDesignateList().getOrganisation_Designate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getDesignateList().getOrganisation_Preference_Message(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            //if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getProvider_UserID() + "")) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                //meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getOrganisation_DesignateID() + "");
                                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetList.getUserID());
                                                UserIDs.add(meetList.getUserID());
                                                UserIDs.add(meetList.getUserID());
                                                EpisodeList = meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i);
                                            }

                                            if (response.getDesignateList().getOrganisation_DesignateID() != null)
                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID() + "")) {
                                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                        }
                                        if (EpisodeList != null) {
                                            setAdapterWithEpisodeMeetGlobal();
                                            setAdapterWithEpisodeGlobalIndividual(EpisodeList);
                                        } else
                                            setAdapterWithEpisodeMeetGlobal();
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
                                            for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID())) {
                                                    meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                            }
                                            setAdapterWithEpisodeMeetGlobal();
                                        } else {
                                            setAdapterWithEpisodeMeetGlobal();
                                            setAdapterWithEpisodeGlobalIndividual(meetList);
                                        }
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                setAdapterWithEpisodeMeetGlobal();
                                setAdapterWithEpisodeGlobalIndividual(meetList);

                                break;
                            }

                            default: {
                                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                        UserIDs.add(meetList.getUserID());
                                    }
                                }
                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetList.getUserID());
                                setAdapterWithEpisodeGlobalIndividual(meetList);
                                setAdapterWithEpisodeMeetGlobal();
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
                    for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                            meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                            UserIDs.add(meetList.getUserID());
                        }
                    }
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetList.getUserID());
                    setAdapterWithOutEpisodeGlobal();
                }

                @Override
                public void onError(int error) {

                }
            });

        }


    }

    private void setAdapterWithEpisodeGlobalIndividual(final MeetInviteParticipantsModel.EpisodeParticipantList meetList) {

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
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    } else if (role.contains("Patient")) {
                        if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isPatientSelected()) {
                        } else {
                            MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                            UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                        }
                    }
                }
            } else {
                if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                } else if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).isChecked()) {
                    MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
                    UserIDs.add(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID());
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

        boolean contains = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.contains(meetList.getUserID());
        /*For checked*/
        printLog("add");

        if (meetList.isDesignate_Exist()) {

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {
                    printLog("response:" + response);

                    if (response.getDesignateList().get(0).getDesignate_Preference() != null) {
                        switch (response.getDesignateList().get(0).getDesignate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID() + "")) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID() + "");
                                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                            }

                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID() + "")) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithEpisodeMeetGlobal();
                                    }
                                });

                                //setAdapterWithOutEpisode(item, Type, pos);
                                break;
                            }
                            case "designate_or_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithEpisodeMeetGlobal();
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                        /// meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                        MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                        UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                    }
                                }

                                setAdapterWithEpisodeMeetGlobal();
                                break;
                            }
                        }
                    } else {

                        for (int i = 0; i < meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(true);
                                /// meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                            }
                        }

                        setAdapterWithEpisodeMeetGlobal();
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
                    setAdapterWithEpisodeMeetGlobal();
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
            setAdapterWithEpisodeMeetGlobal();
        }
    }

    private void setAdapterWithEpisodeMeetGlobal() {
        recyclerViewDialog.setAdapter(null);
        if (meetInviteParticipantsModelMeetNow != null) {
            MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                    setAdapterWithEpisodeMeetGlobal(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }


    /*Without Episode*/
    private void showDialogInviteesWithOutEpisode(Context view) {

        // dialog = new Dialog(this.getApplicationContext());
        alertDialogCreateConversation.dismiss();
        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

        final EditText search = (EditText) dialog.findViewById(R.id.search);
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

        adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisode(item, Type, pos);
            }
        });
        recyclerViewDialog.setAdapter(adapterWithoutEpisode);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsWithoutEpisodeModel != null) {
                    MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                            setAdapterWithOutEpisode(item, Type, pos);
                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialog.findViewById(R.id.btn_start_message);
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.size() > 0) {
                    //UserIDs = TextUtils.join(",", MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout);
                    //submitInviteList();
                    UserIDs = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout;
                    initialMessageDialog(getActivity());
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
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    if (response.getStatus().equals("1") || response.getStatus().equals("0")) {

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
                                String text = name + " is designate for " + meetList.getFullName();
                                //showDialogWithOkButton1(text);

                                final MeetInviteParticipantsWithoutEpisodeModel.UserList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                                // MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(finalEpisodeParticipantForDesignate.getUserID());
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

                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
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
                                            }
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

                        if (response.getDesignateList().size() > 0 && MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {


                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton(text);
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
        } else {
            printLog("add");

            if (meetList.isDesignate_Exist()) {

                NetworkAdapter networkAdapter = new NetworkAdapter();
                networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                    @Override
                    public void onSuccess(final CheckProviderResponse response) {

                        boolean containsAlready = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getDesignate_UserID());

                        if (containsAlready) {
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                    UserIDs.add(meetList.getUserID());
                                }
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
                            showDialogWithOkCancelButton(getResources().getString(R.string.designate_available_message), new OnOkClick() {
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
            MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
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

        MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModelSearch, getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisode(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapterWithoutEpisode);
    }


    /*Without Episode Global*/
    private void showDialogInviteesWithOutEpisodeGlobal(Context view) {

        // dialog = new Dialog(this.getApplicationContext());
        alertDialogCreateConversation.dismiss();
        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

        final EditText search = (EditText) dialog.findViewById(R.id.search);
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
                    filterInviteesWithOutEpisodeGlobal(searchString);
                }

            }
        });

        adapterWithoutEpisodeGlobal = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisodeGlobal(item, Type, pos);
            }
        });
        recyclerViewDialog.setAdapter(adapterWithoutEpisodeGlobal);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsWithoutEpisodeModel != null) {
                    MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                            setAdapterWithOutEpisodeGlobal(item, Type, pos);
                        }
                    });

                    recyclerViewDialog.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_start_message = (Button) dialog.findViewById(R.id.btn_start_message);
        btn_start_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.size() > 0) {
                    //UserIDs = TextUtils.join(",", MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout);
                    //submitInviteList();
                    UserIDs = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout;
                    initialMessageDialog(getActivity());
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

    private void setAdapterWithOutEpisodeGlobal(final MeetInviteParticipantsWithoutEpisodeModel.UserList meetList, final String Type, final String pos) {


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

        boolean contains = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(meetList.getUserID());
        if (contains) {

            if (meetList.isLoggedInUser()) {
                return;
            }
            printLog("remove");

            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    //if (response.getStatus().equals("1") || response.getStatus().equals("0")) {
                    if ((response.getStatus().equals("1") || response.getStatus().equals("0"))) {

                        MeetInviteParticipantsWithoutEpisodeModel.UserList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            String name = null;

                            if (response.getDesignateList() != null && response.getDesignateList().size() > 0 && MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                    }
                                }

                                if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                    String text = meetList.getFullName() + " is designate for " + name;
                                    showDialogWithOkButton(text);
                                }

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
                                setAdapterWithOutEpisodeGlobal();
                            }
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(episodeParticipantForDesignate.getUserID())) {
                                String text = name + " is designate for " + meetList.getFullName();
                                //showDialogWithOkButton1(text);

                                final MeetInviteParticipantsWithoutEpisodeModel.UserList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton("Designate will also be removed", new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
                                                // MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(finalEpisodeParticipantForDesignate.getUserID());
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

                                        }
                                        setAdapterWithOutEpisodeGlobal();
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

                                setAdapterWithOutEpisodeGlobal();
                            }
                        }
                    } else if (response.getDesignateList() != null && response.getDesignateList().size() > 0) {

                        if (response.getDesignateList().size() > 0 && MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton(text);
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

                            setAdapterWithOutEpisodeGlobal();
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

                        setAdapterWithOutEpisodeGlobal();
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
            networkAdapter.checkProviderListGlobal(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new GlobalDesignateCallBack() {
                @Override
                public void onSuccess(final GlobalCheckProviderResponse response) {
                    printLog("response:" + response);
                    if (response.getDesignateList().getOrganisation_DesignateID() != null) {
                        switch (response.getDesignateList().getOrganisation_Designate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getDesignateList().getOrganisation_Preference_Message(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                //meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().getOrganisation_DesignateID() + "");
                                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                                                UserIDs.add(meetList.getUserID());
                                                UserIDs.add(meetList.getUserID());
                                                userList = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i);
                                            }

                                            if (response.getDesignateList().getOrganisation_DesignateID() != null)
                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID() + "")) {
                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                        }
                                        if (userList != null) {
                                            setAdapterWithOutEpisodeGlobal();
                                            setAdapterWithOutEpisodeGlobalIndividual(userList);
                                        } else
                                            setAdapterWithOutEpisodeGlobal();
                                    }
                                });

                                break;
                            }
                            case "designate_or_user": {
                                showDialogWithOkCancelButton(response.getDesignateList().getOrganisation_Preference_Message(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        if (response.getDesignateList().getOrganisation_DesignateID() != null) {
                                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                                if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().getOrganisation_DesignateID())) {
                                                    meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                    MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().getOrganisation_DesignateID());
                                                    UserIDs.add(response.getDesignateList().getOrganisation_DesignateID());
                                                }
                                            }
                                            setAdapterWithOutEpisodeGlobal();
                                        } else {
                                            userList = meetList;
                                            setAdapterWithOutEpisodeGlobal();
                                            setAdapterWithOutEpisodeGlobalIndividual(userList);
                                        }
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                /*for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                    //if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().getProvider_UserID())) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                        /// meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                        // MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().getProvider_UserID());
                                        UserIDs.add(meetList.getUserID());
                                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                                        userList = meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i);

                                    }
                                }*/

                                setAdapterWithOutEpisodeGlobal();
                                setAdapterWithOutEpisodeGlobalIndividual(meetList);

                                break;
                            }

                            default: {
                                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                        UserIDs.add(meetList.getUserID());
                                    }
                                }
                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                                setAdapterWithOutEpisodeGlobalIndividual(userList);
                                setAdapterWithOutEpisodeGlobal();
                            }
                        }
                    } else {
                        /*for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                UserIDs.add(meetList.getUserID());
                            }
                        }*/
                      /*  MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                        setAdapterWithOutEpisodeGlobal();*/
                        if (response.getDesignateList().isIs_Provider_Designate()) {
                            showDialogWithOkCancelButton(response.getDesignateList().getProvider_Preference_Message(), new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    setAdapterWithOutEpisodeGlobalIndividual(meetList);
                                }
                            });

                        } else {
                            setAdapterWithOutEpisodeGlobalIndividual(meetList);
                        }
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
                    setAdapterWithOutEpisodeGlobal();
                }

                @Override
                public void onError(int error) {

                }
            });
           /* } else {
                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                        UserIDs.add(meetList.getUserID());
                    }
                }
                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(meetList.getUserID());
                setAdapterWithOutEpisodeGlobal();
            }*/
        }


    }

    private void setAdapterWithOutEpisodeGlobalIndividual(final MeetInviteParticipantsWithoutEpisodeModel.UserList meetList) {

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

        boolean contains = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(meetList.getUserID());
        /*For checked*/
        printLog("add");

        if (meetList.isDesignate_Exist()) {

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {
                    printLog("response:" + response);

                    if (response.getDesignateList().get(0).getDesignate_Preference() != null) {
                        switch (response.getDesignateList().get(0).getDesignate_Preference()) {
                            case "designate_and_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID() + "")) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID() + "");
                                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                            }

                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID() + "")) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithOutEpisodeGlobal();
                                    }
                                });

                                //setAdapterWithOutEpisode(item, Type, pos);
                                break;
                            }
                            case "designate_or_user": {
                                showDialogWithOkCancelButton(response.getMessage(), new OnOkClick() {
                                    @Override
                                    public void OnOkClicked() {
                                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getDesignate_UserID());
                                                UserIDs.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            }
                                        }
                                        setAdapterWithOutEpisodeGlobal();
                                    }
                                });
                                break;
                            }
                            case "no_designate": {
                                for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                                    if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                        meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                        /// meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                        MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                        UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                                    }
                                }

                                setAdapterWithOutEpisodeGlobal();
                                break;
                            }
                        }
                    } else {

                        for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                            if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(meetList.getUserID())) {
                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(true);
                                /// meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setDesignate_Id(response.getDesignateList().getDesignate_UserID() + "");
                                MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.add(response.getDesignateList().get(0).getProvider_UserID());
                                UserIDs.add(response.getDesignateList().get(0).getProvider_UserID());
                            }
                        }

                        setAdapterWithOutEpisodeGlobal();
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
                    setAdapterWithOutEpisodeGlobal();
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
            setAdapterWithOutEpisodeGlobal();
        }
    }

    private void setAdapterWithOutEpisodeGlobal() {
        recyclerViewDialog.setAdapter(null);
        if (meetInviteParticipantsWithoutEpisodeModel != null) {
            MeetInviteParticipantsWithoutEpisodeAdapter adapter = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {

                    setAdapterWithOutEpisodeGlobal(item, Type, pos);
                }
            });
            recyclerViewDialog.setAdapter(adapter);
        }
    }


    /*End*/

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
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(url, new VolleyCallback() {
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

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, EpisodeNameList);
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

    public void filterInviteesWithOutEpisodeGlobal(String charText) {

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
            notifyDataSetChangedInviteesWithOutEpisodeGlobal();
        else
            recyclerViewDialog.setAdapter(null);

    }

    private void notifyDataSetChangedInviteesWithOutEpisodeGlobal() {

        MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModelSearch, getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                setAdapterWithOutEpisodeGlobal(item, Type, pos);
            }
        });

        recyclerViewDialog.setAdapter(adapterWithoutEpisode);
    }


    private void getMessageCount() {

        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_GET_CONVERSATION, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {

                        printLog("Response Of Message list:" + response);
                        if (response != null) {
                            MessageDialogResponse messageEpisodeListResponse = new Gson().fromJson(response.toString(), MessageDialogResponse.class);

                            if (messageEpisodeListResponse != null) {
                                if (messageEpisodeListResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    MyApplication.getInstance().addToSharedPreference(messageCount, messageEpisodeListResponse.getUnread_feeds());
                                    if (messageCount == null) {
                                        MyApplication.getInstance().addToSharedPreference(messageCount, "0");
                                    }
                                } else {
                                    MyApplication.getInstance().addToSharedPreference(messageCount, "0");
                                }

                            } else {
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
            }
        } else {
            //showNoNetworkMessage();
        }
    }
}

