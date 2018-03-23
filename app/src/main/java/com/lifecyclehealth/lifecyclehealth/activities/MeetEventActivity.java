package com.lifecyclehealth.lifecyclehealth.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.dto.SubmitParticipantDto;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.InviteUserMeetResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.services.BackgroundTrackingService;
import com.lifecyclehealth.lifecyclehealth.services.Time_out_services;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkAdapter;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ActionListener;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.common.BaseRepo;
import com.moxtra.sdk.common.EventListener;
import com.moxtra.sdk.common.model.User;
import com.moxtra.sdk.meet.controller.MeetSessionConfig;
import com.moxtra.sdk.meet.controller.MeetSessionController;
import com.moxtra.sdk.meet.model.MeetSession;
import com.moxtra.sdk.meet.repo.MeetRepo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import zemin.notification.NotificationBuilder;
import zemin.notification.NotificationDelegater;
import zemin.notification.NotificationLocal;
import zemin.notification.NotificationView;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.TIME_TO_WAIT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_END;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_INVITE_PARTICIPANT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_INVITE_PARTICIPANT_TO_MEET;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_START;

public class MeetEventActivity extends BaseActivity {


    private static final String TAG = "DEMO_MeetEventActivity";
    private static final String KEY_ACTION = "action";
    private static final String ACTION_JOIN = "join";
    private static final String ACTION_START = "start";
    private static final String ACTION_SHOW = "show";
    //private static final String KEY_MEET = "meet";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_USER_LIST = "userList";
    private String sessionKey;
    private ArrayList<String> UserIds = new ArrayList<>();

    private final Handler mHandler = new Handler();
    private ChatClientDelegate mChatClientDelegate;
    private MeetRepo mMeetRepo;
    private MeetSession mMeetSession;
    private MeetSessionController mMeetSessionController;
    public NetworkRequestUtil networkRequestUtil;
    MeetInviteParticipantsModel meetInviteParticipantsModel;
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelSearch;

    private NotificationDelegater mDelegater;
    private NotificationLocal mLocal;


    public static void joinMeet(Context ctx) {
        Intent intent = new Intent(ctx, MeetEventActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_JOIN);
        // intent.putExtra(KEY_MEET, meet);
        ctx.startActivity(intent);
    }

    public static void startMeet(Context ctx, String topic, ArrayList<User> userList) {
        Intent intent = new Intent(ctx, MeetEventActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_START);
        intent.putExtra(KEY_TOPIC, topic);
        intent.putParcelableArrayListExtra(KEY_USER_LIST, userList);
        ctx.startActivity(intent);
    }

    public static void showMeet(Context ctx) {
        Intent intent = new Intent(ctx, MeetEventActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_SHOW);
        ctx.startActivity(intent);

    }

    // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
        networkRequestUtil = new NetworkRequestUtil(this);
        mDelegater = NotificationDelegater.getInstance();
        mLocal = mDelegater.local();
        mLocal.setView((NotificationView) findViewById(R.id.nv));
        stopService(new Intent(this, Time_out_services.class));
        Intent intent = getIntent();
        start();
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
        mMeetRepo = mChatClientDelegate.createMeetRepo();

        showProgressDialog(true);
        String action = intent.getStringExtra(KEY_ACTION);
        if (ACTION_JOIN.equals(action)) {
            joinMeet(intent);
        } else if (ACTION_START.equals(action)) {
            startMeet(intent);
        } else if (ACTION_SHOW.equals(action)) {
            mMeetSessionController = mChatClientDelegate.createMeetSessionController(null);
            mMeetSession = mMeetSessionController.getMeetSession();
            showMeetFragment();
        } else {
            Log.e(TAG, "unsupported action: " + action);
            showProgressDialog(false);
            finishInMainThread();
        }
    }

    private void finishInMainThread() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void joinMeet(Intent intent) {

        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        printLog("sessionKey" + sessionKey);


        mMeetRepo.joinMeet(sessionKey, new ApiCallback<MeetSession>() {
            @Override
            public void onCompleted(MeetSession meetSession) {
                Log.i(TAG, "Join meet successfully.");
                mMeetSession = meetSession;
                mMeetSessionController = mChatClientDelegate.createMeetSessionController(mMeetSession);
                //MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().hideMeetID = false;
                MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().meetLinkEnabled = false;
                showMeetFragment();

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "Failed to join meet, errorCode=" + errorCode + ", errorMsg=" + errorMsg);

                showDialogWithOkButton("Join Meet failed. Please check Meet ID and try again.", new OnOkClick() {
                    @Override
                    public void OnOkClicked() {
                        showProgressDialog(false);
                        finishInMainThread();
                    }
                });
            }
        });

    }

    private void startMeet(Intent intent) {
        String topic = intent.getStringExtra(KEY_TOPIC);
        final List<User> userList = intent.getParcelableArrayListExtra(KEY_USER_LIST);
        // mMeetRepo.startMeetWithTopic(topic, new ApiCallback<MeetSession>() {
        sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        printLog("sessionKey" + sessionKey);
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().autoJoinVOIP = true;

        mMeetRepo.startMeetWithMeetID(sessionKey, new ApiCallback<MeetSession>() {
            @Override
            public void onCompleted(MeetSession meetSession) {
                Log.i(TAG, "Start meet successfully.");
                mMeetSession = meetSession;

                MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().autoJoinVOIP = true;
                MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().hideMeetID = false;
                MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().meetLinkEnabled = false;
                mMeetSession.inviteParticipants(userList, new ApiCallback<Void>() {
                    @Override
                    public void onCompleted(Void result) {
                        Log.i(TAG, "Invite participants successfully.");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.i(TAG, "Failed to invite participants, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                    }
                });
                mMeetSessionController = mChatClientDelegate.createMeetSessionController(mMeetSession);
                showMeetFragment();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "Failed to start meet, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                //finishInMainThread();

                showDialogWithOkButton("Join Meet failed. Please check Meet ID and try again.", new OnOkClick() {
                    @Override
                    public void OnOkClicked() {
                        showProgressDialog(false);
                        finishInMainThread();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
    }

    Dialog dialog;

    @Override
    String getTag() {
        return null;
    }

    private void showMeetFragment() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, true);
                if (myHandler != null) {
                    stop();
                    myHandler.removeCallbacks(null);
                    myHandler = null;

                    printLog("Stop timeout session");
                }
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.meet_frame);
                if (fragment == null) {
                    fragment = mMeetSessionController.createMeetFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.meet_frame, fragment).commit();
                }
                mMeetSessionController.setSwitchToNormalViewActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        try {
                            MeetEventActivity.showMeet(view.getContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                mMeetSessionController.setInviteMemberActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        printLog("invite member listener");
                        //getInviteParticipant(view.getContext());
                        getInviteParticipant(MeetEventActivity.this);
                    }
                });
                mMeetSessionController.setSwitchToFloatingViewActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        try {
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mMeetSession.setOnMeetEndedEventListener(new EventListener<Void>() {
                    @Override
                    public void onEvent(Void aVoid) {
                        printLog("end event occur");
                        // finish();
                        callEndMeet(sessionKey);
                    }
                });


                MeetSessionConfig meetSessionConfig = new MeetSessionConfig();
                meetSessionConfig.setVoIPEnabled(true);
                meetSessionConfig.setAutoJoinVoIPEnabled(true);
                meetSessionConfig.setMeetIDEnabled(false);
                mMeetSessionController.setMeetSessionConfig(meetSessionConfig);

                showProgressDialog(false);
            }
        });
    }

    RecyclerView recyclerView;
    MeetInviteParticipantsAdapter adapter;


    private void showDialog1(Context view) {
        UserIds = new ArrayList<String>();
        // dialog = new Dialog(this.getApplicationContext());
        dialog = new Dialog(view);
        context = dialog.getContext();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_meet_invite_participants);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                if (meetInviteParticipantsModel != null) {
                    String searchString = search.getText().toString();
                    filter(searchString);
                }

            }
        });

        adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisode(item, Type, pos);

            }
        });
        recyclerView.setAdapter(adapter);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModel != null) {
                    MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                            setAdapterWithEpisode(item, Type, pos);
                        }
                    });

                    recyclerView.setAdapter(adapter);
                }
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIds = new ArrayList<String>();
                dialog.dismiss();
                dialog.cancel();
            }
        });

        Button btn_invite = (Button) dialog.findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MeetInviteParticipantsAdapter.selectedParticipant.size() > 0) {
                    submitInviteList();
                }

            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

    }


    private void submitInviteList() {
        showProgressDialog(true);
        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        if (isConnectedToNetwork(this)) {
            try {

                String join = TextUtils.join(",", MeetInviteParticipantsAdapter.selectedParticipant);
                SubmitParticipantDto submitParticipantDto = new SubmitParticipantDto();
                submitParticipantDto.setSession_key(sessionKey);
                submitParticipantDto.setUserIDs(MeetInviteParticipantsAdapter.selectedParticipant);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(submitParticipantDto));

                this.networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_INVITE_PARTICIPANT_TO_MEET, requestJson, new VolleyCallback() {
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


    private void setAdapterWithEpisode(final MeetInviteParticipantsModel.EpisodeParticipantList meetList, String Type, String pos) {

        MeetInviteParticipantsAdapter.selectedParticipant = new ArrayList();

        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
            String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getRoleName());
            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
            } else if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isChecked()) {
                MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
            } else if (role.contains("Patient")) {
                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isPatientSelected()) {
                } else {
                    MeetInviteParticipantsAdapter.selectedParticipant.add(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID());
                }
            }


        }

        boolean contains = MeetInviteParticipantsAdapter.selectedParticipant.contains(meetList.getUserID());
        if (contains) {
            printLog("remove");
            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                String role = TextUtils.join(",", meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getRoleName());
                if (role.contains("Patient")) {
                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setPatientSelected(true);
                }
            }

            NetworkAdapter networkAdapter = new NetworkAdapter();
            networkAdapter.removeProviderList(getApplicationContext(), networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                @Override
                public void onSuccess(final CheckProviderResponse response) {

                    if (response.getStatus().equals("1")) {

                        MeetInviteParticipantsModel.EpisodeParticipantList episodeParticipantForDesignate = null;
                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getDesignate_Id() != null) {
                                if (meetList.getUserID().equals(meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID())) {
                                    episodeParticipantForDesignate = meetInviteParticipantsModel.getEpisodeParticipantList().get(i);
                                }
                            }
                        }

                        if (episodeParticipantForDesignate == null) {
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                }
                            }

                            int i = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                            if (i >= 0)
                                MeetInviteParticipantsAdapter.selectedParticipant.remove(i);
                            setAdapterWithEpisode();
                        } else {
                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(episodeParticipantForDesignate.getDesignate_Id())) {
                                    name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsAdapter.selectedParticipant.contains(episodeParticipantForDesignate.getUserID())) {
                                String text = name + " is designate for " + meetList.getFullName();
                                // showDialogWithOkButton1(text);


                                final MeetInviteParticipantsModel.EpisodeParticipantList finalEpisodeParticipantForDesignate = episodeParticipantForDesignate;
                                showDialogWithOkCancelButton1("Designate will also be removed", new OnOkClick() {
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
                                                String designate_id = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getDesignate_Id();

                                                r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(designate_id);
                                                if (r >= 0)
                                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(r);

                                            }

                                           /* if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getDesignate_Id())) {
                                                meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                                //MeetInviteParticipantsAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                printLog("checked designate");
                                                int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                if (r >= 0)
                                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                                            }*/
                                        }
                                        setAdapterWithEpisode();
                                    }
                                });

                            } else {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(i);

                            }
                        }
                        setAdapterWithEpisode();

                    } else {

                        if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {

                            String name = null;
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    name = meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getFullName();
                                }
                            }

                            if (MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getProvider_UserID())) {
                                String text = meetList.getFullName() + " is designate for " + name;
                                showDialogWithOkButton1(text);
                            } else {
                                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                    }
                                }

                                int i = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                                if (i >= 0)
                                    MeetInviteParticipantsAdapter.selectedParticipant.remove(i);
                                setAdapterWithEpisode();
                            }



                           /* showDialogWithOkCancelButton1("Designate will also be removed", new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                            MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                            printLog("checked provider");
                                            int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(response.getDesignateList().get(0).getDesignate_UserID());
                                            MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                                        }

                                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                            MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            printLog("checked designate");
                                            int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(response.getDesignateList().get(0).getDesignate_UserID());
                                            MeetInviteParticipantsAdapter.selectedParticipant.remove(r);
                                        }
                                    }
                                    setAdapterWithEpisode();
                                }
                            });*/
                        } else {

                            int r = MeetInviteParticipantsAdapter.selectedParticipant.indexOf(meetList.getUserID());
                            if (r >= 0)
                                MeetInviteParticipantsAdapter.selectedParticipant.remove(r);

                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(false);
                                }
                            }
                            setAdapterWithEpisode();
                        }
                    }
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

                        boolean containsAlready = MeetInviteParticipantsAdapter.selectedParticipant.contains(response.getDesignateList().get(0).getDesignate_UserID());

                        if (containsAlready) {
                            for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                              /*  if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                    MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                }*/
                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                    MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                }

                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                    MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                }
                            }
                            setAdapterWithEpisode();
                        } else {
                            showDialogWithOkCancelButton1(getResources().getString(R.string.designate_available_message), new OnOkClick() {
                                @Override
                                public void OnOkClicked() {
                                    for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                            MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getProvider_UserID());
                                            printLog("checked provider");
                                        }

                                        if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                            meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                                            MeetInviteParticipantsAdapter.selectedParticipant.add(response.getDesignateList().get(0).getDesignate_UserID());
                                            printLog("checked designate");
                                        }
                                    }
                                    setAdapterWithEpisode();
                                }
                            });

                        }

                    }

                    @Override
                    public void onError(int error) {

                    }
                });
            } else {
                for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                    if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID().equals(meetList.getUserID())) {
                        meetInviteParticipantsModel.getEpisodeParticipantList().get(i).setChecked(true);
                    }
                }
                MeetInviteParticipantsAdapter.selectedParticipant.add(meetList.getUserID());
                setAdapterWithEpisode();
            }
        }


    }

    private void setAdapterWithEpisode() {
        recyclerView.setAdapter(null);
        if (meetInviteParticipantsModel != null) {
            MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

                    setAdapterWithEpisode(item, Type, pos);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }


    public void filter(String charText) {

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
            notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {

        MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModelSearch, getApplicationContext(), UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                setAdapterWithEpisode(item, Type, pos);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void hideDialog() {
        if (dialog != null) {
            dialog.hide();
            dialog.cancel();
        }
    }

    Context context;

    private void getInviteParticipant(final Context view) {
        showProgressDialog(true);
        context = view;
        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        if (isConnectedToNetwork(this)) {
            try {
                this.networkRequestUtil.getDataSecure(BASE_URL + URL_MEET_INVITE_PARTICIPANT + sessionKey, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Meet Participant:" + response);
                        if (response != null) {
                            meetInviteParticipantsModel = new Gson().fromJson(response.toString(), MeetInviteParticipantsModel.class);
                            if (meetInviteParticipantsModel != null) {
                                if (meetInviteParticipantsModel.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    int designateFlag = 0;
                                    if (meetInviteParticipantsModel.getEpisodeParticipantList().size() > 0) {
                                        for (int i = 0; i < meetInviteParticipantsModel.getEpisodeParticipantList().size(); i++) {
                                            if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isLoggedInUser()) {
                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(i).isDesignate_Exist()) {
                                                    NetworkAdapter networkAdapter = new NetworkAdapter();
                                                    final int finalI = i;
                                                    networkAdapter.checkProviderList(getApplicationContext(), networkRequestUtil, meetInviteParticipantsModel.getEpisodeParticipantList().get(i).getUserID(), new DesignateCallBack() {
                                                        @Override
                                                        public void onSuccess(final CheckProviderResponse response) {
                                                            for (int j = 0; j < meetInviteParticipantsModel.getEpisodeParticipantList().size(); j++) {
                                                                if (meetInviteParticipantsModel.getEpisodeParticipantList().get(j).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(j).setChecked(true);
                                                                    meetInviteParticipantsModel.getEpisodeParticipantList().get(j).setDesignate_Id(response.getDesignateList().get(0).getDesignate_UserID());
                                                                }
                                                            }
                                                            adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), getApplicationContext(), UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                                                                    setAdapterWithEpisode(item, Type, pos);

                                                                }
                                                            });
                                                            recyclerView.setAdapter(adapter);
                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    showDialog1(context);
                                                    break;
                                                }
                                            }
                                            if ((i + 1) == meetInviteParticipantsModel.getEpisodeParticipantList().size()) {
                                                showDialog1(context);
                                            }
                                        }
                                    } else {
                                        showDialogWithOkButton1("No Participant Remain");
                                    }

                                } else {
                                    hideDialog();
                                    showDialogWithOkButton(meetInviteParticipantsModel.getMessage());
                                }
                            } else {
                                hideDialog();
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


    private void callEndMeet(String sessionKey) {
        try {
            //showProgressDialog(true);
            if (isConnectedToNetwork(this)) {
                try {
                    this.networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_END + sessionKey, null, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            // showProgressDialog(false);
                            printLog("Response Of meet end:" + response);
                            finish();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showDialogWithOkCancelButton1(String message, final OnOkClick onOkClick) {

        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_dialog_ok_cancel);

        TextView TextViewMessage = (TextView) dialog1.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button yes_btn = (Button) dialog1.findViewById(R.id.yes_btn);
        Button cancel_btn = (Button) dialog1.findViewById(R.id.cancel_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkClick.OnOkClicked();
                dialog1.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog1.getWindow().setAttributes(lp);
        dialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog1.show();
    }


    public void showDialogWithOkButton1(String message) {

        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.layout_dialog_ok);

        TextView TextViewMessage = (TextView) dialog1.findViewById(R.id.messageTv);
        TextViewMessage.setText(message);
        Button buttonOk = (Button) dialog1.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog1.getWindow().setAttributes(lp);
        dialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog1.show();


    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        printLog("touch onUserInteraction");
        if (myHandler != null) {
            stop();
            restart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MEETEVENTACTIVITY_ALIVE, false);
        if (myHandler != null) {
            stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //BaseActivity.active=false;
        startService(new Intent(this, BackgroundTrackingService.class));
        if (myHandler != null) {
            stop();
        }
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // your code here
            Intent i = new Intent(MeetEventActivity.this, LoginActivity.class);
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
        //MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_MAINACTIVITY_ALIVE, false);
        myHandler.removeCallbacks(myRunnable);
    }

    private void restart() {
        myHandler.removeCallbacks(myRunnable);
        myHandler.postDelayed(myRunnable, TIME_TO_WAIT);
    }


}