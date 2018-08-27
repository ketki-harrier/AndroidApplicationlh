package com.lifecyclehealth.lifecyclehealth.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsWithoutEpisodeAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.dto.MeetNowDTO;
import com.lifecyclehealth.lifecyclehealth.dto.ScheduleMeetDetailsDto;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsWithoutEpisodeModel;
import com.lifecyclehealth.lifecyclehealth.model.MeetNowResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageEpisodeListResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessagePatientListModel;
import com.lifecyclehealth.lifecyclehealth.utils.AESHelper;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkAdapter;
import com.moxtra.sdk.common.model.User;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.LOGIN_ID;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_EPISODE_LIST_FOR_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_WITH_MULTIPLE_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_PATIENT_LIST;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_SCHEDULE_MEET;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.seedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleMeet extends BaseFragmentWithOptions {


    String Name, Patient_UserID, PatientID, Title, Episode_Care_PlanID, Episode_Care_Plan_Name;
    ArrayList<String> UserIDs = new ArrayList<>();
    private Button btnInvitees, btnCancel, startMeet;
    private EditText meetingTitle;
    private Spinner spinner_no_episode_PatientName, spinner_episode_name, spinnerPatientName, duration;
    private AppCompatRadioButton provider_to_provider, patient_related_episode, provider_patient_with_no_episode, provider_with_multiple_patient;
    private LinearLayout linear_provider_patient_with_no_episode, linear_patient_related_episode;
    private RadioGroup radioGroup;
    private ArrayList<String> PatientNameList = new ArrayList<>();
    private ArrayList<String> PatientNameListUserID = new ArrayList<>();
    private ArrayList<String> PatientNameListId = new ArrayList<>();
    private ArrayList<String> EpisodeNameList = new ArrayList<>();
    private ArrayList<String> EpisodeNameListId = new ArrayList<>();
    private ArrayList<String> EpisodeStatusList = new ArrayList<>();
    private ArrayList<String> durationList = new ArrayList<>();

    private TextView start_date, start_time;
    private MeetInviteParticipantsModel meetInviteParticipantsModelMeetNow;
    ArrayList<MeetInviteParticipantsModel.EpisodeParticipantList> meetInviteParticipantsModelMeetNowSearch;
    private MeetInviteParticipantsWithoutEpisodeModel meetInviteParticipantsWithoutEpisodeModel;
    ArrayList<MeetInviteParticipantsWithoutEpisodeModel.UserList> meetInviteParticipantsWithoutEpisodeModelSearch;

    private int selectedTypePatient = 0;
    Context context;
    private String selectedEpisode = "";
    MeetInviteParticipantsWithEpisodeAdapter adapterWithEpisode;
    MeetInviteParticipantsWithoutEpisodeAdapter adapterWithoutEpisode;

    MainActivity mainActivity;


    public ScheduleMeet() {
        // Required empty public constructor
    }


    public static ScheduleMeet newInstance() {
        ScheduleMeet scheduleMeet = new ScheduleMeet();
        return scheduleMeet;
    }

    @Override
    String getFragmentTag() {
        return "ScheduleMeet";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_meet, container, false);
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
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        Analytics.with(getContext()).screen("ScheduleMeet");

        context = getContext();

        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        start_date = (TextView) view.findViewById(R.id.start_date);
        start_time = (TextView) view.findViewById(R.id.start_time);
        btnInvitees = (Button) view.findViewById(R.id.btnInvitees);
        startMeet = (Button) view.findViewById(R.id.startMeet);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        meetingTitle = (EditText) view.findViewById(R.id.meetingTitle);
        spinner_no_episode_PatientName = (Spinner) view.findViewById(R.id.spinner_no_episode_PatientName);
        spinner_episode_name = (Spinner) view.findViewById(R.id.spinner_episode_name);
        spinnerPatientName = (Spinner) view.findViewById(R.id.spinnerPatientName);
        duration = (Spinner) view.findViewById(R.id.duration);

        patient_related_episode = (AppCompatRadioButton) view.findViewById(R.id.patient_related_episode);
        provider_to_provider = (AppCompatRadioButton) view.findViewById(R.id.provider_to_provider);
        provider_with_multiple_patient = (AppCompatRadioButton) view.findViewById(R.id.provider_with_multiple_patient);
        provider_patient_with_no_episode = (AppCompatRadioButton) view.findViewById(R.id.provider_patient_with_no_episode);
        linear_provider_patient_with_no_episode = (LinearLayout) view.findViewById(R.id.linear_provider_patient_with_no_episode);
        linear_patient_related_episode = (LinearLayout) view.findViewById(R.id.linear_patient_related_episode);

        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
        linear_patient_related_episode.setVisibility(View.GONE);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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

                    if (Patient_UserID.equals("-99")) {
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
                backPage();
            }
        });

        btnInvitees.setOnClickListener(inviteesListener);

        startMeet.setOnClickListener(scheduleMeetListener);
        start_date.setOnClickListener(startDateListener);
        start_time.setOnClickListener(start_time_MeetListener);

        durationList = new ArrayList<>();
        durationList.add("60 min");
        durationList.add("45 min");
        durationList.add("30 min");
        durationList.add("15 min");

        ArrayAdapter<String> dataAdapterDuration = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, durationList);
        dataAdapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        duration.setAdapter(dataAdapterDuration);

        start_date.setText(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        start_time.setText(new SimpleDateFormat("h:mm a").format(Calendar.getInstance().getTime()));

    }


    private View.OnClickListener inviteesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Analytics.with(getContext()).track("Invite participants for evisit during scheduling eVisit", new Properties().putValue("category", "Mobile"));

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
                    if (meetingTitle.getText().toString().trim().equals("")) {
                        showDialogWithOkButton("Please enter meeting title");
                        return;
                    } else if (spinner_no_episode_PatientName.getSelectedItemPosition() == 0) {
                        showDialogWithOkButton("Please select Patient");
                        return;
                    }

                    Name = meetingTitle.getText().toString();

                    getInviteesWithOutEpisode(PatientID);
                    break;
                }
            }

        }
    };


    private void setSpinnerAdapter() {


        EpisodeNameListId = new ArrayList<>();
        selectedTypePatient = 2;
        provider_patient_with_no_episode.setSupportButtonTintList(deSelectColorList());
        provider_to_provider.setSupportButtonTintList(deSelectColorList());
        provider_with_multiple_patient.setSupportButtonTintList(deSelectColorList());
        linear_patient_related_episode.setVisibility(View.VISIBLE);
        linear_provider_patient_with_no_episode.setVisibility(View.GONE);
        patient_related_episode.setSupportButtonTintList(getColorList());
        patient_related_episode.setChecked(true);

        EpisodeNameList = new ArrayList<>();
        EpisodeNameList.add("Select Episode");
        EpisodeNameListId.add("-99");


        ArrayAdapter<String> dataAdapterEpisode = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, EpisodeNameList);
        dataAdapterEpisode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_episode_name.setAdapter(dataAdapterEpisode);

    }

    private View.OnClickListener start_time_MeetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    //start_time.setText( selectedHour + ":" + selectedMinute);
                    updateTime(selectedHour, selectedMinute);
                }
            }, hour, minute, false);

            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
    };


    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        start_time.setText(aTime);
    }

    private View.OnClickListener scheduleMeetListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isConnectedToNetwork(mainActivity)) {
                try {
                    String type = null;

                    if (meetingTitle.getText().toString().trim().equals("")) {
                        showDialogWithOkButton("Please enter meeting title");
                        return;
                    } /*else if (UserIDs.size() <= 0) {
                        showDialogWithOkButton("Please select Invitees");
                        return;
                    }*/

                    switch (selectedTypePatient) {
                        case 1:
                            Episode_Care_Plan_Name = "";
                            Episode_Care_PlanID = "";
                            PatientID = "";
                            Patient_UserID = "";
                            type = "Provider to Provider";
                            break;
                        case 2: {
                            type = "Patient Related Episode";
                            int position = spinner_episode_name.getSelectedItemPosition();
                            int positionPatient = spinnerPatientName.getSelectedItemPosition();
                            if (positionPatient == 0) {
                                showDialogWithOkButton("Please select Patient");
                                return;
                            } else if (position == 0) {
                                showDialogWithOkButton("Please select episode");
                                return;
                            }
                            break;
                        }
                        case 3: {
                            int position = spinner_no_episode_PatientName.getSelectedItemPosition();
                            if (position == 0) {
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

                    showProgressDialog(true);

                    ScheduleMeetDetailsDto.MeetDetails meetDetails = new ScheduleMeetDetailsDto().new MeetDetails();

                    TimeZone tz = TimeZone.getDefault();
                    Date now = new Date();
                    int offsetFromUtc = (tz.getOffset(now.getTime()) / 1000) * (-1);

                    Analytics.with(getContext()).track("Schedule eVisit", new Properties().putValue("category", "Mobile"));

                    meetDetails.setName(Name);
                    meetDetails.setAgenda("");
                    meetDetails.setStartDate(start_date.getText().toString());
                    meetDetails.setStartTime(start_time.getText().toString());
                    meetDetails.setDurationInMinute(duration.getSelectedItem().toString().substring(0, 2));
                    meetDetails.setEpisode_Care_Plan_Name(Episode_Care_Plan_Name);
                    meetDetails.setEpisode_Care_PlanID(Episode_Care_PlanID);
                    meetDetails.setUserIDs(UserIDs);
                    meetDetails.setOffSet(offsetFromUtc + "");
                    meetDetails.setMeeting_Type(type);
                    meetDetails.setPatientID(PatientID);
                    meetDetails.setPatient_UserID(Patient_UserID);

                    ScheduleMeetDetailsDto scheduleMeetDetailsDto = new ScheduleMeetDetailsDto();
                    scheduleMeetDetailsDto.setMeetDetails(meetDetails);


                    final JSONObject requestJson = new JSONObject(new Gson().toJson(scheduleMeetDetailsDto));
                    printLog("getAgenda:" + meetDetails.getAgenda());
                    printLog("getEpisode_Care_Plan_Name:" + meetDetails.getEpisode_Care_Plan_Name());
                    printLog("getEpisode_Care_PlanID:" + meetDetails.getEpisode_Care_PlanID());
                    printLog("getMeeting_Type:" + meetDetails.getMeeting_Type());
                    printLog("getName:" + meetDetails.getName());
                    printLog("getPatient_UserID:" + meetDetails.getPatient_UserID());
                    printLog("getPatientID:" + meetDetails.getPatientID());
                    printLog("getUserIDs:" + meetDetails.getUserIDs());
                    printLog("getDuration:" + meetDetails.getDurationInMinute());

                    //printLog("SendingValue:" + requestJson.toString());
                    mainActivity.networkRequestUtil.postDataSecure(BASE_URL + URL_SCHEDULE_MEET, requestJson, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            showProgressDialog(false);
                            printLog("response:Create Meet" + response);
                            MeetNowResponse messageCreateConversationResponse = new Gson().fromJson(response.toString(), MeetNowResponse.class);
                            if (messageCreateConversationResponse != null) {
                                if (messageCreateConversationResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    backPage();

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
    };

    private View.OnClickListener startDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final Calendar c = Calendar.getInstance();

            DatePickerDialog dpd = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            start_date.setText(year + "-"
                                    + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.show();


        }
    };

    private ColorStateList getColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(getContext(), R.color.colorPrimary)}
        );
    }

    private ColorStateList deSelectColorList() {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{ContextCompat.getColor(getContext(), R.color.black)}
        );
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

    private void backPage() {
        getFragmentManager().popBackStack();
    }


    RecyclerView recyclerViewDialog;
    MeetInviteParticipantsAdapter adapter;
    Dialog dialog;


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
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, PatientNameList);
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerPatientName.setAdapter(dataAdapter);
                                    spinner_no_episode_PatientName.setAdapter(dataAdapter);
                                    if (selectedTypePatient == 2) {
                                        if (PatientNameList.size() > 0) {
                                            //callPatientEpisodeList(PatientNameListId.get(0));
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
                                    } else if (EpisodeNameList.size() > 1) {
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

        if (isConnectedToNetwork(mainActivity)) {
            try {
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

                                                            adapterWithEpisode = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos) {
                                                                    setAdapterWithEpisodeMeet(item, Type, pos);
                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapterWithEpisode);

                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }

                                        showDialogInviteesWithEpisode(getContext());
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

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.fragment_message_invite_participants, null);
        builder.setView(dialogView);
        dialog = builder.create();


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialogView.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

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

        adapterWithEpisode = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNow.getEpisodeParticipantList(), getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
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
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
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
                                            if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                                printLog("checked provider");
                                                int r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(response.getDesignateList().get(0).getProvider_UserID());
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

                                           /* if (meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).getUserID().equals(response.getDesignateList().get(0).getDesignate_UserID())) {
                                                meetInviteParticipantsModelMeetNow.getEpisodeParticipantList().get(i).setChecked(false);
                                               // MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.add(finalEpisodeParticipantForDesignate.getDesignate_Id());
                                                printLog("checked designate");
                                                int r = MeetInviteParticipantsWithEpisodeAdapter.selectedParticipant.indexOf(response.getDesignateList().get(0).getDesignate_UserID());
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

    private void notifyDataSetChangedInviteesWithEpisode1() {

        MeetInviteParticipantsWithEpisodeAdapter adapter = new MeetInviteParticipantsWithEpisodeAdapter(meetInviteParticipantsModelMeetNowSearch, getContext(), UserIDs, new MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener() {
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


                                                            adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos) {
                                                                    setAdapterWithOutEpisode(item, Type, pos);
                                                                }
                                                            });
                                                            recyclerViewDialog.setAdapter(adapterWithoutEpisode);
                                                        }

                                                        @Override
                                                        public void onError(int error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }


                                        showDialogInviteesWithOutEpisode(getContext());
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.fragment_message_invite_participants, null);
        builder.setView(dialogView);
        dialog = builder.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setContentView(R.layout.fragment_message_invite_participants);

        recyclerViewDialog = (RecyclerView) dialogView.findViewById(R.id.recycleViewInvities);
        recyclerViewDialog.hasFixedSize();
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(getContext()));

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

        adapterWithoutEpisode = new MeetInviteParticipantsWithoutEpisodeAdapter(meetInviteParticipantsWithoutEpisodeModel.getUserList(), getContext(), UserIDs, new MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener() {
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

        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            networkAdapter.removeProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
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

                                            /*if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(finalEpisodeParticipantForDesignate.getUserID())) {
                                                meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).setChecked(false);
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
                public void onError(int error) {

                }
            });
        }


        /*For checked*/
        else {
            printLog("add");

            if (meetList.isDesignate_Exist()) {

                NetworkAdapter networkAdapter = new NetworkAdapter();
                networkAdapter.checkProviderList(getContext(), mainActivity.networkRequestUtil, meetList.getUserID(), new DesignateCallBack() {
                    @Override
                    public void onSuccess(final CheckProviderResponse response) {

                        boolean containsAlready = MeetInviteParticipantsWithoutEpisodeAdapter.selectedParticipantWithout.contains(response.getDesignateList().get(0).getDesignate_UserID());

                        if (containsAlready) {
                            for (int i = 0; i < meetInviteParticipantsWithoutEpisodeModel.getUserList().size(); i++) {
                               /* if (meetInviteParticipantsWithoutEpisodeModel.getUserList().get(i).getUserID().equals(response.getDesignateList().get(0).getProvider_UserID())) {
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


    public void showDialogWithOkCancelButton1(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
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

}
