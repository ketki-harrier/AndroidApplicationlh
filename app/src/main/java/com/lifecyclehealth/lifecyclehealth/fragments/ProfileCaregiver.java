package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.ProfileCaregiverAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.ProfileCaregiverResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;


import org.json.JSONObject;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_PROFILE_CAREGIVER;


public class ProfileCaregiver extends BaseFragmentWithOptions {

    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private ColorCode colorCode;
    String Stringcode;

    @Override
    String getFragmentTag() {
        return "ProfileCaregiver";
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
        return inflater.inflate(R.layout.fragment_profile_caregiver, parent, false);
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
      //  try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcode = "";
            String hashcode = "";

            if(demo == null){
                hashcode = "Green";
                Stringcode = "259b24";
            }
            else if(demo !=null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
           /* }
           else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
       // }catch (Exception e){e.printStackTrace();}
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));

        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setColorFilter(Color.parseColor("#"+Stringcode));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        TextView text_title_caregiver = (TextView) view.findViewById(R.id.text_title_caregiver);
        back.setTextColor(Color.parseColor("#"+Stringcode));
        text_title_caregiver.setTextColor(Color.parseColor("#"+Stringcode));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        getCaregiverData();
    }

    private void getCaregiverData() {
        showProgressDialog(true);
        if (isConnectedToNetwork(mainActivity)) {
            try {

                mainActivity.networkRequestUtil.getDataSecure(BASE_URL + URL_PROFILE_CAREGIVER, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response Of get profile caregiver:" + response);

                        if (response != null) {
                            ProfileCaregiverResponse profileCaregiverResponse = new Gson().fromJson(response.toString(), ProfileCaregiverResponse.class);
                            if (profileCaregiverResponse != null) {
                                if (profileCaregiverResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    ProfileCaregiverAdapter profileCaregiverAdapter = new ProfileCaregiverAdapter(profileCaregiverResponse.getCaregiverList(), mainActivity, new ProfileCaregiverAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(ProfileCaregiverResponse.CaregiverLists item, String Type, String pos) {

                                        }
                                    });
                                    recyclerView.setAdapter(profileCaregiverAdapter);

                                } else {
                                    showDialogWithOkButton(profileCaregiverResponse.getMessage());
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
}