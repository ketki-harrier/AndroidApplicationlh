package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.SubmitParticipantDto;
import com.lifecyclehealth.lifecyclehealth.model.InviteUserMeetResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.MXChatCustomizer;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ActionListener;
import com.moxtra.sdk.common.ApiCallback;
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

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_END;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_INVITE_PARTICIPANT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_INVITE_PARTICIPANT_TO_MEET;


public class MeetEvent extends BaseFragmentWithOptions {

    MainActivity mainActivity;
    private static final String TAG = "DEMO_MeetEventActivity";
    private static final String KEY_ACTION = "action";
    private static final String ACTION_JOIN = "join";
    private static final String ACTION_START = "start";
    private static final String ACTION_SHOW = "show";
    //private static final String KEY_MEET = "meet";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_USER_LIST = "userList";
    private String sessionKey;

    private final Handler mHandler = new Handler();

    private ChatClientDelegate mChatClientDelegate;
    private MeetRepo mMeetRepo;
    private MeetSession mMeetSession;
    private MeetSessionController mMeetSessionController;
    public NetworkRequestUtil networkRequestUtil;
    MeetInviteParticipantsModel meetInviteParticipantsModel;
    ArrayList<String> UserIds=new ArrayList<>();
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelSearch;

    public MeetEvent() {
        // Required empty public constructor
    }


    public static MeetEvent newInstanceJoinMeet() {
        MeetEvent fragment = new MeetEvent();
        Bundle args = new Bundle();
        args.putString(KEY_ACTION, ACTION_JOIN);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static MeetEvent newInstanceStartMeet(String topic, ArrayList<User> userList) {
        MeetEvent fragment = new MeetEvent();
        Bundle args = new Bundle();
        args.putString(KEY_ACTION, ACTION_START);
        args.putString(KEY_TOPIC, topic);
        args.putParcelableArrayList(KEY_USER_LIST, userList);
        fragment.setArguments(args);
        return fragment;
    }


    public static MeetEvent showMeet() {
        MeetEvent fragment = new MeetEvent();
        Bundle args = new Bundle();
        args.putString(KEY_ACTION, ACTION_SHOW);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    String getFragmentTag() {
        return "MeetEvent";
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {

        Bundle intent = getArguments();
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
        String action = getArguments().getString(KEY_ACTION);
        if (ACTION_JOIN.equals(action)) {
            joinMeet();
        } else if (ACTION_START.equals(action)) {
            startMeet();
        } else if (ACTION_SHOW.equals(action)) {
            mMeetSessionController = mChatClientDelegate.createMeetSessionController(null);
            mMeetSession = mMeetSessionController.getMeetSession();
            showMeetFragment();
        } else {
            Log.e(TAG, "unsupported action: " + action);
            finishInMainThread();
        }
    }

    private void finishInMainThread() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //finish();
                backPage();
            }
        });
    }

    private void joinMeet() {

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
                finishInMainThread();
            }
        });

    }

    private void startMeet() {
        String topic = getArguments().getString(KEY_TOPIC);
        final List<User> userList = getArguments().getParcelableArrayList(KEY_USER_LIST);
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
                finishInMainThread();
            }
        });
    }


    Dialog dialog;


    private void showMeetFragment() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.meet_frame);
                if (fragment == null) {
                    fragment = mMeetSessionController.createMeetFragment();
                    getFragmentManager().beginTransaction().add(R.id.meet_frame, fragment).commit();
                }
                mMeetSessionController.setSwitchToNormalViewActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        try {
                            showMeet();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                mMeetSessionController.setInviteMemberActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        printLog("click on invitees");
                        //getInviteParticipant(getApplicationContext());
                        show2(mainActivity);
                    }
                });
                mMeetSessionController.setSwitchToFloatingViewActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        try {
                            backPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mMeetSession.setOnMeetEndedEventListener(new EventListener<Void>() {
                    @Override
                    public void onEvent(Void aVoid) {
                        printLog("end event occur");
                        callEndMeet(sessionKey);
                    }
                });


                MeetSessionConfig meetSessionConfig = new MeetSessionConfig();
                meetSessionConfig.setVoIPEnabled(true);
                meetSessionConfig.setAutoJoinVoIPEnabled(true);
                meetSessionConfig.setMeetIDEnabled(false);
                mMeetSessionController.setMeetSessionConfig(meetSessionConfig);


               /* mMeetSessionController.setEndOrLeaveMeetActionListener(new ActionListener<Void>() {
                    @Override
                    public void onAction(View view, Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Leave", Toast.LENGTH_SHORT).show();
                    }
                });
*/
                showProgressDialog(false);
            }
        });
    }

    RecyclerView recyclerView;
    MeetInviteParticipantsAdapter adapter;


    private void show2(Context view) {

        Dialog dialog = new Dialog(view);

        dialog.setContentView(R.layout.fragment_message_invite_participants);

        dialog.setTitle("Custom Dialog");

        dialog.show();

    }

    private void showDialog1(Context view) {

        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_meet_invite_participants);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

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

        adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), mainActivity,UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {


            }
        });
        recyclerView.setAdapter(adapter);


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                if (meetInviteParticipantsModel != null) {
                    MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModel.getEpisodeParticipantList(), mainActivity,UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {


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
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();

    }


    private void submitInviteList() {
        showProgressDialog(true);
        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        if (isConnectedToNetwork(mainActivity)) {
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

        MeetInviteParticipantsAdapter adapter = new MeetInviteParticipantsAdapter(meetInviteParticipantsModelSearch, mainActivity.getApplicationContext(),UserIds, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

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


    private void getInviteParticipant(final Context view) {
        showProgressDialog(true);
        final Context context = view;
        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        if (isConnectedToNetwork(mainActivity)) {
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
                                    //showDialog1(context);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mMeetSession != null) {
            mMeetSession.cleanup();
        }
        if (mMeetSessionController != null) {
            mMeetSessionController.cleanup();
        }
        Log.d(TAG, "onCreate");
    }

    /*
    @Override
    public void onResume() {
        super.onResume();
        getApplicationContext().getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    mainActivity.finish();
                    return true;
                }
                return false;
            }
        });
    }
*/


    private void callEndMeet(String sessionKey) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                this.networkRequestUtil.putDataSecure(BASE_URL + URL_MEET_END + sessionKey, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of meet end:" + response);
                        backPage();
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

    private void backPage() {
  /*      FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("ProfileFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        getFragmentManager().popBackStack();

    }

}
