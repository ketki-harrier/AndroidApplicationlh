package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;

public class MoreOptionsFragment extends BaseFragmentWithOptions {


    MainActivity mainActivity;
    boolean isPatient;
    String messageCount;

    @Override
    String getFragmentTag() {
        return "MoreOptionsFragment";
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

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file_show for the fragment
        return inflater.inflate(R.layout.fragment_more_options, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {
        messageCount = MyApplication.getInstance().getFromSharedPreference(AppConstants.messageCount);
        isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);
        RelativeLayout healthLayout = (RelativeLayout) view.findViewById(R.id.healthLayout);
        healthLayout.setOnClickListener(healthClickListener);
        RelativeLayout treatmentLayout = (RelativeLayout) view.findViewById(R.id.treatmentLayout);
        treatmentLayout.setOnClickListener(treatmentLayoutListener);
        RelativeLayout patientSurveyLayout = (RelativeLayout) view.findViewById(R.id.patientSurveyLayout);
        patientSurveyLayout.setOnClickListener(patientSurveyLayoutListener);

        RelativeLayout patientDiaryLayout = (RelativeLayout) view.findViewById(R.id.patientDiaryLayout);
        patientDiaryLayout.setOnClickListener(patientDiaryLayoutListener);

        RelativeLayout patientToDoLayout = (RelativeLayout) view.findViewById(R.id.patientToDoLayout);
        patientToDoLayout.setOnClickListener(patientToDoLayoutListener);

        RelativeLayout addPatientExpressLayout = (RelativeLayout) view.findViewById(R.id.addPatientExpressLayout);
        addPatientExpressLayout.setOnClickListener(addPatientExpressLayoutListener);


        RelativeLayout notificationLayout = (RelativeLayout) view.findViewById(R.id.notificationLayout);
        notificationLayout.setOnClickListener(notificationClickListener);
        RelativeLayout logoutLayout = (RelativeLayout) view.findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(logoutClickListener);
        RelativeLayout profileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);
        profileLayout.setOnClickListener(profileClickListener);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.more));
      /*  if (isPatient) {
            patientSurveyLayout.setVisibility(View.VISIBLE);
            patientDiaryLayout.setVisibility(View.GONE);
            patientToDoLayout.setVisibility(View.GONE);
            addPatientExpressLayout.setVisibility(View.GONE);
        } else {
            patientSurveyLayout.setVisibility(View.GONE);
            patientDiaryLayout.setVisibility(View.VISIBLE);
            patientToDoLayout.setVisibility(View.VISIBLE);
            addPatientExpressLayout.setVisibility(View.VISIBLE);
        }*/

        patientSurveyLayout.setVisibility(View.GONE);
        patientDiaryLayout.setVisibility(View.GONE);
        patientToDoLayout.setVisibility(View.GONE);
        addPatientExpressLayout.setVisibility(View.GONE);

        RelativeLayout notificationHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.notificationHolder);
        RelativeLayout messageHolderLayout = (RelativeLayout) toolbar.findViewById(R.id.messageHolder);
/*

        TextView notificationCountTextView = (TextView) view.findViewById(R.id.notificationCountTextView);
        ImageView imageViewMessage = (ImageView) view.findViewById(R.id.imageViewMessage);
        if (!messageCount.equals("0")) {
            notificationCountTextView.setText(messageCount);
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            imageViewMessage.setColorFilter(getContext().getResources().getColor(R.color.uvv_gray));
            notificationCountTextView.setVisibility(View.GONE);
        }
*/

        //notificationCountTextView.setText(MainActivity.messageCount);

        notificationHolderLayout.setVisibility(View.GONE);
        messageHolderLayout.setVisibility(View.GONE);
    }


    private View.OnClickListener healthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
            mainActivity.changeToComingSoon("Health");
        }
    };
    private View.OnClickListener patientSurveyLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
            mainActivity.changeToComingSoon("Patient Summary");
        }
    };
    private View.OnClickListener treatmentLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
            mainActivity.changeToComingSoon("Treatment");
        }
    };

    private View.OnClickListener patientDiaryLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
            mainActivity.changeToComingSoon("Patient Diary");
        }
    };

    private View.OnClickListener patientToDoLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
            mainActivity.changeToComingSoon("To Do");
        }
    };

    private View.OnClickListener notificationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = new NotificationFragment();
            String fragmentTag = fragment.getClass().getName();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                    .replace(R.id.content_main, fragment, fragmentTag)
                    .addToBackStack("NotificationFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    };

    private View.OnClickListener logoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            mainActivity.moreLogout();
        }
    };

    private View.OnClickListener addPatientExpressLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mainActivity.changeToComingSoon("Add Patient Express");
        }
    };

    private View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = new ProfileFragment();
            String fragmentTag = fragment.getClass().getName();
            printLog("tag" + fragmentTag);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
                    .replace(R.id.content_main, fragment, fragmentTag)
                    .addToBackStack("ProfileFragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            /*ProfileFragment profileFragment = new ProfileFragment();
            replaceFragment(profileFragment);*/
            //mainActivity.changeToComingSoon();

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
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


}