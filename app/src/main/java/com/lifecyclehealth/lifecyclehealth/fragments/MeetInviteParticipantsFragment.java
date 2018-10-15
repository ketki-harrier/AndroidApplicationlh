package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_INVITE_PARTICIPANT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetInviteParticipantsFragment extends BaseFragmentWithOptions  {


    MeetEventActivity meetEventActivity;
    RecyclerView recyclerView;



    public static MeetInviteParticipantsFragment newInstance() {
        MeetInviteParticipantsFragment holderFragment = new MeetInviteParticipantsFragment();
        return holderFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meet_invite_participants, container, false);
    }

    @Override
    String getFragmentTag() {
        return "MeetInviteParticipantsFragment";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        meetEventActivity = (MeetEventActivity) context;
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);

    }

    private void initializeView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.title_meet));
        //emptyViewTv = (TextView) view.findViewById(R.id.emptyViewTv);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewInvities);
        recyclerView.hasFixedSize();


    }


    /* for clearing recycleview*/
    private void resetRecycleView() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
    }

    /* for aplying adapter list*/
    private void setRecyclerView(MeetInviteParticipantsModel inviteParticipantsModel) {
        recyclerView.setLayoutManager(new LinearLayoutManager(meetEventActivity));
       /* MeetInviteParticipantsAdapter adapter=new MeetInviteParticipantsAdapter(inviteParticipantsModel, meetEventActivity, new MeetInviteParticipantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {

            }
        });*/
        // recyclerView.setAdapter(meetAdapter);

    }

    /* For showing empty view when there is no data*/
   /* private void showEmptyView(boolean flag) {
        if (flag) emptyViewTv.setVisibility(View.VISIBLE);
        else emptyViewTv.setVisibility(View.GONE);
    }*/


    private void getAllParticipantList() {

        showProgressDialog(true);
        String sessionKey = MyApplication.getInstance().getFromSharedPreference(AppConstants.SESSION_KEY);
        if (isConnectedToNetwork(meetEventActivity)) {
            try {
                meetEventActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_MEET_INVITE_PARTICIPANT + sessionKey, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Meet Participant:" + response);
                        if (response != null) {
                            MeetInviteParticipantsModel meetInviteParticipantsModel = new Gson().fromJson(response.toString(), MeetInviteParticipantsModel.class);
                            if (meetInviteParticipantsModel != null) {
                                if (meetInviteParticipantsModel.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    setRecyclerView(meetInviteParticipantsModel);

                                } else {
                                    resetRecycleView();
                                    showDialogWithOkButton(meetInviteParticipantsModel.getMessage());
                                }

                            } else {
                                resetRecycleView();
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

}