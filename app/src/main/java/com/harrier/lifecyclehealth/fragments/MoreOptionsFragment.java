package com.harrier.lifecyclehealth.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.harrier.lifecyclehealth.R;

public class MoreOptionsFragment extends BaseFragment {

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
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
        RelativeLayout healthLayout = (RelativeLayout) view.findViewById(R.id.healthLayout);
        healthLayout.setOnClickListener(healthClickListener);
        RelativeLayout notificationLayout = (RelativeLayout) view.findViewById(R.id.notificationLayout);
        notificationLayout.setOnClickListener(notificationClickListener);
        RelativeLayout logoutLayout = (RelativeLayout) view.findViewById(R.id.logoutLayout);
        logoutLayout.setOnClickListener(logoutClickListener);
        RelativeLayout profileLayout = (RelativeLayout) view.findViewById(R.id.profileLayout);
        profileLayout.setOnClickListener(profileClickListener);
    }


    private View.OnClickListener healthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Coming Soon.", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener notificationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NotificationFragment notificationFragment = new NotificationFragment();
            replaceFragment(notificationFragment);
        }
    };

    private View.OnClickListener logoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener profileClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ProfileFragment profileFragment = new ProfileFragment();
            replaceFragment(profileFragment);
        }
    };

}