package com.lifecyclehealth.lifecyclehealth.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteParticipantsAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MeetInviteesAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.lifecyclehealth.lifecyclehealth.model.MeetDetailsForProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_LIST_DETAILS;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetDetailsForProviderFragment extends BaseFragmentWithOptions implements View.OnClickListener {

    private MainActivity mainActivity;
    private static final String MEET_EXTRAS_HOLDER = "List_holder_meet";
    private MeetListDTO.MeetList meetList;
    private AppCompatRadioButton provider_to_provider, patient_related_episode, provider_patient_with_no_episode, provider_with_multiple_patient;
    private EditText meetingTitle, startDate, endDate, patient_name, episode_name;
    private TextView txt_episode_name, txt_patient_name;
    private Button btnInvitees;
    private  MeetDetailsForProviderResponse meetListDTO;


    public static MeetDetailsForProviderFragment newInstance(String data) {
        MeetDetailsForProviderFragment holderFragment = new MeetDetailsForProviderFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MEET_EXTRAS_HOLDER, data);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }

    public MeetDetailsForProviderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    String getFragmentTag() {
        return "MeetDetailsForProviderFragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meet_details_for, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        meetList = new Gson().fromJson(getArguments().getString(MEET_EXTRAS_HOLDER), MeetListDTO.MeetList.class);
        initialiseView(view);
    }

    private void initialiseView(View view) {

        provider_to_provider = (AppCompatRadioButton) view.findViewById(R.id.provider_to_provider);
        patient_related_episode = (AppCompatRadioButton) view.findViewById(R.id.patient_related_episode);
        provider_patient_with_no_episode = (AppCompatRadioButton) view.findViewById(R.id.provider_patient_with_no_episode);
        provider_with_multiple_patient = (AppCompatRadioButton) view.findViewById(R.id.provider_with_multiple_patient);
        meetingTitle = (EditText) view.findViewById(R.id.meetingTitle);
        startDate = (EditText) view.findViewById(R.id.startDate);
        endDate = (EditText) view.findViewById(R.id.endDate);
        patient_name = (EditText) view.findViewById(R.id.patient_name);
        episode_name = (EditText) view.findViewById(R.id.episode_name);
        txt_episode_name = (TextView) view.findViewById(R.id.txt_episode_name);
        txt_patient_name = (TextView) view.findViewById(R.id.txt_patient_name);
        btnInvitees = (Button) view.findViewById(R.id.btnInvitees);
        patient_name.setVisibility(View.GONE);
        episode_name.setVisibility(View.GONE);
        txt_episode_name.setVisibility(View.GONE);
        txt_patient_name.setVisibility(View.GONE);
        btnInvitees.setOnClickListener(this);
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

        getMeetDetails(meetList.getSession_key());

    }

    private void backPage() {
  /*      FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("ProfileFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
        getFragmentManager().popBackStack();

    }

    private void getMeetDetails(String sessionKey) {
        showProgressDialog(true);

        if (isConnectedToNetwork(mainActivity)) {
            try {
                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_MEET_LIST_DETAILS + sessionKey, new VolleyCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get all Meet:" + response);

                        if (response != null) {
                            meetListDTO = new Gson().fromJson(response.toString(), MeetDetailsForProviderResponse.class);
                            if (meetListDTO != null) {
                                if (meetListDTO.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {

                                    startDate.setText(convertedLocalDate(meetListDTO.getMeetDetails().getScheduled_starts()));
                                    endDate.setText(convertedLocalDate(meetListDTO.getMeetDetails().getScheduled_ends()));
                                    meetingTitle.setText(meetListDTO.getMeetDetails().getTopic());

                                    switch (meetListDTO.getMeetDetails().getMeeting_Type()) {
                                        case "Provider to Provider": {
                                            provider_to_provider.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                                            provider_to_provider.setSupportButtonTintList(getColorList());
                                            provider_to_provider.setChecked(true);
                                            patient_name.setVisibility(View.GONE);
                                            episode_name.setVisibility(View.GONE);
                                            txt_episode_name.setVisibility(View.GONE);
                                            txt_patient_name.setVisibility(View.GONE);
                                            break;
                                        }
                                        case "Patient Related Episode": {
                                            patient_related_episode.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                                            patient_related_episode.setSupportButtonTintList(getColorList());
                                            patient_related_episode.setChecked(true);
                                            patient_name.setVisibility(View.VISIBLE);
                                            episode_name.setVisibility(View.VISIBLE);
                                            txt_episode_name.setVisibility(View.VISIBLE);
                                            txt_patient_name.setVisibility(View.VISIBLE);
                                            patient_name.setText(meetListDTO.getMeetDetails().getPatient_Name());
                                            episode_name.setText(meetListDTO.getMeetDetails().getEpisode_Care_Plan_Name());
                                            break;
                                        }
                                        case "Provider Patient With No Episode": {
                                            provider_patient_with_no_episode.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                                            provider_patient_with_no_episode.setSupportButtonTintList(getColorList());
                                            provider_patient_with_no_episode.setChecked(true);
                                            patient_name.setVisibility(View.GONE);
                                            episode_name.setVisibility(View.GONE);
                                            txt_episode_name.setVisibility(View.GONE);
                                            txt_patient_name.setVisibility(View.GONE);
                                            break;
                                        }
                                        case "Provider With Multiple Patient": {
                                            provider_with_multiple_patient.setHighlightColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary));
                                            provider_with_multiple_patient.setSupportButtonTintList(getColorList());
                                            provider_with_multiple_patient.setChecked(true);
                                            patient_name.setVisibility(View.GONE);
                                            episode_name.setVisibility(View.GONE);
                                            txt_episode_name.setVisibility(View.GONE);
                                            txt_patient_name.setVisibility(View.GONE);
                                            break;
                                        }
                                    }

                                } else {
                                    showDialogWithOkButton(meetListDTO.getMessage());
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

    private String convertedLocalDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd KK:mm aa");
        Date d = null;
        Date updatedDate = null;
        try {

            d = sdf.parse(date);
            updatedDate = gmttoLocalDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = output.format(updatedDate);
        return formattedTime;
    }

    private Date gmttoLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInvitees: {
                showDialog(getContext());
                break;
            }
        }
    }

    RecyclerView recyclerView;
    private Dialog dialog;

    private void showDialog(Context view) {

        dialog = new Dialog(view);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.meet_invitees_dialog);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycleViewInvities);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MeetInviteesAdapter adapter=new MeetInviteesAdapter(meetListDTO.getMeetDetails().getInvitees(), getContext(), new MeetInviteesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MeetDetailsForProviderResponse.Invitees item, String Type, String pos) {

            }
        });

        recyclerView.setAdapter(adapter);

        //recyclerView.setAdapter(adapter);


        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
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

}
