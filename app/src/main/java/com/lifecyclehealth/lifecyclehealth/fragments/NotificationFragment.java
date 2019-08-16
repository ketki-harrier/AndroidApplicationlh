package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.NotificationAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.NotificationFilterDto;
import com.lifecyclehealth.lifecyclehealth.dto.SurveyPlanDto;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.InviteUserMeetResponse;
import com.lifecyclehealth.lifecyclehealth.model.NotificationDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.model.SurveyPlan;
import com.lifecyclehealth.lifecyclehealth.swipe_delete.SwipeController;
import com.lifecyclehealth.lifecyclehealth.swipe_delete.SwipeControllerActions;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_DELETE_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_FILTER_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_READ_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SURVEY_CHECK_AVAILABILITY_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.notificationCount;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragmentWithOptions {

    private MainActivity mainActivity;
    boolean isPatient;
    RecyclerView notificationRecycler;
    String messageCount, notificationCount1;
    NotificationAdapter notificationAdapter;
    TextView notificationCountTextViewMessage;
    Switch include_archived_notifications, episode_level_notification;
    boolean statusEpisodeLevel, statusArchivedNotifications = false;
    NotificationDialogResponse notificationDialogResponse;
    LinearLayout linearEpisodeLevel;
    //PlayersDataAdapter mAdapter;
    private ColorCode colorCode;
    String Stringcode;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    String getFragmentTag() {
        return "BaseFragmentWithOptions";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        initializeView(view);
    }


    private void initializeView(View view) {
      //  try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String Stringcode = "";
            String hashcode = "";
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            if (demo == null) {
                hashcode = "Green";
                Stringcode = "259b24";
            } else if (demo != null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
            /*}
            else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
       // }catch (Exception e){e.printStackTrace();}
        messageCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.messageCount);
        notificationCount1 = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);
        linearEpisodeLevel = (LinearLayout) view.findViewById(R.id.linearEpisodeLevel);
        include_archived_notifications = (Switch) view.findViewById(R.id.include_archived_notifications);
        episode_level_notification = (Switch) view.findViewById(R.id.episode_level_notification);

        if (isPatient) {
            linearEpisodeLevel.setVisibility(View.GONE);
        }

        include_archived_notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statusArchivedNotifications = true;
                    filterNotification();
                } else {
                    statusArchivedNotifications = false;
                    filterNotification();
                }
            }
        });

        episode_level_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statusEpisodeLevel = true;
                    filterNotification();
                } else {
                    statusEpisodeLevel = false;
                    filterNotification();
                }

            }
        });

        notificationRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setRecyclerView();

        RelativeLayout notificationHolderLayout = (RelativeLayout) view.findViewById(R.id.notificationHolder);
        notificationCountTextViewMessage = (TextView) view.findViewById(R.id.countTextViewNotificatione);
        ImageView imageViewNotification = (ImageView) view.findViewById(R.id.imageViewNotification);

        if (!notificationCount1.equals("0")) {
            notificationCountTextViewMessage.setText(notificationCount1);
            //imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
            imageViewNotification.setColorFilter(Color.parseColor("#"+Stringcode));
        } else {
            notificationCountTextViewMessage.setVisibility(View.GONE);
            imageViewNotification.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
        }
        notificationHolderLayout.setOnClickListener(onClickListener);

        ImageView backArrowBtn = (ImageView) view.findViewById(R.id.backArrowBtn);
        backArrowBtn.setColorFilter(Color.parseColor("#"+Stringcode));
        TextView back = (TextView) view.findViewById(R.id.back);
        back.setTextColor(Color.parseColor("#"+Stringcode));
        back.setOnClickListener(onClickListener);
        backArrowBtn.setOnClickListener(onClickListener);
        back.setText("Notification");
    }

    private void setRecyclerView() {

        notificationAdapter = new NotificationAdapter(MainActivity.notificationDialogResponse.getAlertList(), getContext(), new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NotificationDialogResponse.AlertList item, String Type, String pos) {

                switch (Type) {
                    case "n": {
                        if (item.getStatus().trim().equals("New")) {
                            printLog("position" + pos);
                            int position = MainActivity.notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                            for (int i = 0; i < MainActivity.notificationDialogResponse.getAlertList().size(); i++) {
                                if (MainActivity.notificationDialogResponse.getAlertList().get(i).getAlertID().equals(item.getAlertID())) {
                                    position = i;
                                }
                            }
                            if (position != -1)
                                readNotification(item.getAlertID(), position);
                        } else {
                            switch (item.getAlert_Type().trim()) {
                                case "Survey": {
                                    ArrayList<PatientSurveyItem> patientSurveyItemArrayList = new ArrayList<>();
                                    PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                    patientSurveyItem.setPatientSurveyResponseId(Integer.parseInt(item.getPatient_Survey_Scheduled_ResponseID()));
                                    patientSurveyItem.setPatientSurveyId(Integer.parseInt(item.getPatientID()));
                                    patientSurveyItem.setName(item.getTitle());
                                    patientSurveyItem.setDescription(item.getDescription());
                                    patientSurveyItem.setScheduleDate("");
                                    patientSurveyItemArrayList.add(patientSurveyItem);

                                    String data = new Gson().toJson(patientSurveyItemArrayList);
                                    CheckSurveyAvailabilityNotification(item, data, 0, "Survey");
                                    break;
                                }
                                case "Provider Survey": {
                                    ArrayList<PatientSurveyItem> patientSurveyItemArrayList = new ArrayList<>();
                                    PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                    patientSurveyItem.setPatientSurveyResponseId(Integer.parseInt(item.getPatient_Survey_Scheduled_ResponseID()));
                                    patientSurveyItem.setPatientSurveyId(Integer.parseInt(item.getPatientID()));
                                    patientSurveyItem.setName(item.getTitle());
                                    patientSurveyItem.setDescription(item.getDescription());
                                    patientSurveyItem.setScheduleDate("");
                                    patientSurveyItemArrayList.add(patientSurveyItem);

                                    String data = new Gson().toJson(patientSurveyItemArrayList);
                                    CheckSurveyAvailabilityNotification(item, data, 0, "Provider Survey");
                                    break;
                                }case "Participant Alert": {
                                    mainActivity.changeToSurveyReport(item.getPatient_Survey_Scheduled_ResponseID());
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case "d": {
                        if (item.is_Deletable()) {
                            // int Position = MainActivity.notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                            int position = MainActivity.notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                            for (int i = 0; i < MainActivity.notificationDialogResponse.getAlertList().size(); i++) {
                                if (MainActivity.notificationDialogResponse.getAlertList().get(i).getAlertID().equals(item.getAlertID())) {
                                    position = i;
                                }
                            }
                            if (position != -1)
                                deleteNotification(position);

                        } else {
                            notificationAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }

            }
        });
        notificationRecycler.setAdapter(notificationAdapter);


    }

    //Read notification
    private void readNotification(String alertId, final int pos) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_READ_NOTIFICATION + alertId, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response read notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    MainActivity.notificationDialogResponse.getAlertList().get(pos).setStatus("Read");
                                    notificationAdapter.notifyDataSetChanged();
                                    int value = Integer.parseInt(notificationCount1);
                                    value = value - 1;
                                    notificationCount1 = value + "";
                                    notificationCountTextViewMessage.setText(notificationCount1);
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, notificationCount1);

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

    //Delete Notification
    private void deleteNotification(final int position) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                final String readStatus = MainActivity.notificationDialogResponse.getAlertList().get(position).getStatus();
                String alertId = MainActivity.notificationDialogResponse.getAlertList().get(position).getAlertID();
                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_DELETE_NOTIFICATION + alertId, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response delete notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    notificationAdapter.notifyItemRemoved(position);
                                    if (readStatus.trim().equals("New")) {
                                        int value = Integer.parseInt(notificationCount1);
                                        value = value - 1;
                                        notificationCount1 = value + "";
                                        notificationCountTextViewMessage.setText(notificationCount1);
                                        MyApplication.getInstance().addToSharedPreference(notificationCount, notificationCount1);
                                    }
                                    MainActivity.notificationDialogResponse.getAlertList().remove(position);
                                } else {
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            } else {
                                notificationAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
                notificationAdapter.notifyDataSetChanged();
            }
        } else {
            showProgressDialog(false);
            notificationAdapter.notifyDataSetChanged();
            showNoNetworkMessage();
        }
    }


    //Check survey available or not
    private void CheckSurveyAvailabilityNotification(final NotificationDialogResponse.AlertList alertList, final String data, final int position, final String title) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)){
            try {
                final JSONObject requestJson = new JSONObject(new Gson().toJson(alertList));
                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_SURVEY_CHECK_AVAILABILITY_NOTIFICATION, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response check available of survey notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    MainActivity.bottomBar.selectTabWithId(R.id.tab_survey);
                                    MainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_survey);
                                    mainActivity.changeToSurveyDetailsView(data, position, title);
                                } else {
                                    showDialogWithOkButton(inviteUserMeetResponse.getMessage());
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

    /*Filter notification*/
    private void filterNotification() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                NotificationFilterDto dto = new NotificationFilterDto();
                dto.setEpisode_Level(statusEpisodeLevel);
                dto.setIncludeArchived(statusArchivedNotifications);

                final JSONObject requestJson = new JSONObject(new Gson().toJson(dto));

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_FILTER_NOTIFICATION, requestJson, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response get notification" + response);
                        if (response != null) {
                            notificationDialogResponse = new Gson().fromJson(response.toString(), NotificationDialogResponse.class);
                            if (notificationDialogResponse != null) {
                                if (notificationDialogResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    printLog("filter notification");
                                    notificationRecycler.setAdapter(null);
                                    setRecyclerViewFilter();
                                } else {
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            } else {
                                notificationAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
                notificationAdapter.notifyDataSetChanged();
            }
        } else {
            showProgressDialog(false);
            notificationAdapter.notifyDataSetChanged();
            showNoNetworkMessage();
        }
    }

    private void setRecyclerViewFilter() {
        notificationRecycler.setAdapter(null);
        if (notificationDialogResponse != null) {
            notificationAdapter = new NotificationAdapter(notificationDialogResponse.getAlertList(), getContext(), new NotificationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(NotificationDialogResponse.AlertList item, String Type, String pos) {

                    switch (Type) {
                        case "n": {
                            if (item.getStatus().trim().equals("New")) {
                                printLog("position" + pos);
                                int position = notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                                for (int i = 0; i < notificationDialogResponse.getAlertList().size(); i++) {
                                    if (notificationDialogResponse.getAlertList().get(i).getAlertID().equals(item.getAlertID())) {
                                        position = i;
                                    }
                                }
                                if (position != -1)
                                    readFilterNotification(item.getAlertID(), position);
                            } else {
                                switch (item.getAlert_Type().trim()) {
                                    case "Survey": {
                                        ArrayList<PatientSurveyItem> patientSurveyItemArrayList = new ArrayList<>();
                                        PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                        patientSurveyItem.setPatientSurveyResponseId(Integer.parseInt(item.getPatient_Survey_Scheduled_ResponseID()));
                                        patientSurveyItem.setPatientSurveyId(Integer.parseInt(item.getPatientID()));
                                        patientSurveyItem.setName(item.getTitle());
                                        patientSurveyItem.setDescription(item.getDescription());
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItemArrayList.add(patientSurveyItem);

                                        String data = new Gson().toJson(patientSurveyItemArrayList);
                                        CheckSurveyAvailabilityNotification(item, data, 0, "Survey");
                                        break;
                                    }
                                    case "Provider Survey": {
                                        ArrayList<PatientSurveyItem> patientSurveyItemArrayList = new ArrayList<>();
                                        PatientSurveyItem patientSurveyItem = new PatientSurveyItem();
                                        patientSurveyItem.setPatientSurveyResponseId(Integer.parseInt(item.getPatient_Survey_Scheduled_ResponseID()));
                                        patientSurveyItem.setPatientSurveyId(Integer.parseInt(item.getPatientID()));
                                        patientSurveyItem.setName(item.getTitle());
                                        patientSurveyItem.setDescription(item.getDescription());
                                        patientSurveyItem.setScheduleDate("");
                                        patientSurveyItemArrayList.add(patientSurveyItem);

                                        String data = new Gson().toJson(patientSurveyItemArrayList);
                                        CheckSurveyAvailabilityNotification(item, data, 0, "Provider Survey");
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                        case "d": {
                            if (item.is_Deletable()) {
                                // int Position = notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                                int position = notificationDialogResponse.getAlertList().indexOf(item.getAlertID());
                                for (int i = 0; i < notificationDialogResponse.getAlertList().size(); i++) {
                                    if (notificationDialogResponse.getAlertList().get(i).getAlertID().equals(item.getAlertID())) {
                                        position = i;
                                    }
                                }
                                if (position != -1)
                                    deleteNotificationFilter(position);

                            } else {
                                notificationAdapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
            });
            notificationRecycler.setAdapter(notificationAdapter);
        }
    }

    //Delete notification filter
    private void deleteNotificationFilter(final int position) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {
                final String readStatus = notificationDialogResponse.getAlertList().get(position).getStatus();
                String alertId = notificationDialogResponse.getAlertList().get(position).getAlertID();
                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_DELETE_NOTIFICATION + alertId, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response delete notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    notificationAdapter.notifyItemRemoved(position);
                                    if (readStatus.trim().equals("New")) {
                                        int value = Integer.parseInt(notificationCount1);
                                        value = value - 1;
                                        notificationCount1 = value + "";
                                        notificationCountTextViewMessage.setText(notificationCount1);
                                        MyApplication.getInstance().addToSharedPreference(notificationCount, notificationCount1);
                                    }
                                    notificationDialogResponse.getAlertList().remove(position);
                                } else {
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            } else {
                                notificationAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        showProgressDialog(false);
                        notificationAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                showProgressDialog(false);
                notificationAdapter.notifyDataSetChanged();
            }
        } else {
            showProgressDialog(false);
            notificationAdapter.notifyDataSetChanged();
            showNoNetworkMessage();
        }
    }

    //Read notification filter
    private void readFilterNotification(String alertId, final int pos) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {

                mainActivity.networkRequestUtil.putDataSecure(BASE_URL + URL_READ_NOTIFICATION + alertId, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response read notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    notificationDialogResponse.getAlertList().get(pos).setStatus("Read");
                                    notificationAdapter.notifyDataSetChanged();
                                   /* int value = Integer.parseInt(notificationCount1);
                                    value = value - 1;
                                    notificationCount1 = value + "";
                                    notificationCountTextViewMessage.setText(notificationCount1);
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, notificationCount1);
*/
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.notificationHolder:
                    //Analytics.with(getContext()).track("Read notification from NotificaitonPopUp", new Properties().putValue("category", "Mobile"));
                    getDialogSetup(mainActivity, LEFT, notificationCountTextViewMessage);
                    break;

                case R.id.backArrowBtn:
                    backPage();
                    break;

                case R.id.back:
                    backPage();
                    break;
            }
        }
    };

    private void backPage() {
        MeetFragment.selectedDate = null;
        Fragment fragment = new MoreOptionsFragment();
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        String fragmentTag = fragment.getClass().getName();
        getFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
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
                    backPage();
                    return true;
                }

                return false;
            }
        });
    }


}
