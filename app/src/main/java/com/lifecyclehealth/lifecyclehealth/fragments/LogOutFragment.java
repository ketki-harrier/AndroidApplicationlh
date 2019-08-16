package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.LoginActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogOutFragment extends BaseFragmentWithOptions {


    MainActivity mainActivity;
    private ColorCode colorCode;
    String Stringcode;

    public static LogOutFragment newInstance() {

        LogOutFragment holderFragment = new LogOutFragment();
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "LogOutFragment";
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
                /*   } else*/
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }

       /* } catch (Exception e) {
            e.printStackTrace();
        }*/
        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setColorFilter(Color.parseColor("#" + Stringcode));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        TextView back = (TextView) view.findViewById(R.id.back);
        back.setTextColor(Color.parseColor("#" + Stringcode));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        Button cancel = (Button) view.findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        Button btnLogOut = (Button) view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_LOGOUT, true);
                MyApplication.getInstance().addBooleanToSharedPreference(AppConstants.IS_USERNAME_EDITABLE, false);
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

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

        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("LogOutFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
