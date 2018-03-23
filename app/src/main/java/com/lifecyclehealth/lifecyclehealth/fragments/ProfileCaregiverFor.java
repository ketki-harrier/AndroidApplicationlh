package com.lifecyclehealth.lifecyclehealth.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.ProfileCaregiverAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.ProfileCaregiverForAdapter;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ProfileCaregiverForResponse;
import com.lifecyclehealth.lifecyclehealth.model.ProfileCaregiverResponse;

import org.json.JSONObject;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_CAREGIVER_FOR;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileCaregiverFor  extends BaseFragmentWithOptions implements SearchView.OnQueryTextListener{

    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private SearchView searchCaregiverFor;
    private RelativeLayout caregiver_for_relative;
    private TextView caregiver_for ;
    ProfileCaregiverForAdapter profileCaregiverAdapter;

    @Override
    String getFragmentTag() {
        return "ProfileCaregiverFor";
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
        return inflater.inflate(R.layout.fragment_profile_caregiver_for, parent, false);
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


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {

        caregiver_for_relative= (RelativeLayout) view.findViewById(R.id.caregiver_for_relative);
        caregiver_for= (TextView) view.findViewById(R.id.caregiver_for);
        searchCaregiverFor= (SearchView) view.findViewById(R.id.searchCaregiverFor);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

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

        searchCaregiverFor.setOnQueryTextListener(this);
        searchCaregiverFor.setIconified(false);
        getCaregiverForData(view);
        caregiver_for.setVisibility(View.GONE);
        caregiver_for_relative.setVisibility(View.VISIBLE);
        hideKeyboardFrom(getActivity(),view);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getCaregiverForData(final View view) {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {

                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_PROFILE_CAREGIVER_FOR, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get profile caregiver for:" + response);
                        hideKeyboardFrom(getActivity(),view);
                        if (response != null) {
                            ProfileCaregiverForResponse profileCaregiverResponse = new Gson().fromJson(response.toString(), ProfileCaregiverForResponse.class);
                            if (profileCaregiverResponse != null) {
                                if (profileCaregiverResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    profileCaregiverAdapter = new ProfileCaregiverForAdapter(profileCaregiverResponse.getPatientList(), mainActivity, new ProfileCaregiverAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(ProfileCaregiverResponse.CaregiverLists item, String Type, String pos) {

                                        }
                                    });
                                    recyclerView.setAdapter(profileCaregiverAdapter);

                                } else {
                                   // showDialogWithOkButton(profileCaregiverResponse.getMessage());
                                    caregiver_for.setVisibility(View.VISIBLE);
                                    caregiver_for_relative.setVisibility(View.GONE);
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

    private void backPage() {
        getFragmentManager().popBackStack();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        profileCaregiverAdapter.filter(text);
        return false;
    }
}