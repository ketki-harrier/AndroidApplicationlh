package com.harrier.lifecyclehealth.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.callbacks.OnOkClick;

/**
 * Created by satyam on 14/04/2017.
 */

public abstract class BaseFragmentWithOptions extends Fragment {
    private ProgressDialog progressDialog;
    private TextView screenTitleTextView;
    public static final String RIGHT = "right_menu";
    public static final String LEFT = "left_menu";

    abstract String getFragmentTag();

    public void printLog(String message) {
        Log.e(getFragmentTag(), message + " ");
    }

    public boolean isValid(String value) {
        return (value != null) && !value.isEmpty();
    }

    public boolean isValid(View view) {
        return (view != null);
    }

    /**************************
     * NETWORK AVAILABILITY
     *****************************/

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        // boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public void showErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(context.getString(R.string.error_title));
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressDialog();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /* For changing Title of Toolbar*/
    public void setupToolbarTitle(Toolbar toolbar, String title) {
        screenTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        screenTitleTextView.setText(title);
    }

    public TextView getScreenTitleTextView() {
        return screenTitleTextView;
    }


    public void getDialogSetup(Activity context, String menu) {
        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View dialogView = layoutInflater.inflate
                (R.layout.layout_dilog_top_left, null);

        builder.setView(dialogView);

        RelativeLayout frameLayout = (RelativeLayout) dialogView.findViewById(R.id.layout_frame);
        View viewRightBubble = (View) dialogView.findViewById(R.id.viewBubbleRight);
        View viewLeftBubble = (View) dialogView.findViewById(R.id.viewBubbleLeft);

        if (menu.equalsIgnoreCase(RIGHT)) {
            setBackground(frameLayout, R.drawable.bg_round_corner);
            viewRightBubble.setVisibility(View.VISIBLE);
            viewLeftBubble.setVisibility(View.GONE);
        } else {
            setBackground(frameLayout, R.drawable.bg_round_corner_right_top);
            viewRightBubble.setVisibility(View.GONE);
            viewLeftBubble.setVisibility(View.VISIBLE);
        }

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 30;//x position
        wmlp.y = 100;//y position
        wmlp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(wmlp);
        alertDialog.show();

    }

    private void setBackground(RelativeLayout background, @DrawableRes int rource) {
        if (Build.VERSION.SDK_INT >= 16) {
            background.setBackground(ContextCompat.getDrawable(getActivity(), rource));
        } else {
            background.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), rource));
        }
    }

    private void setProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), null, null);
        progressDialog.setContentView(R.layout.layout_progress_bar);
        if (progressDialog.getWindow() != null)
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_indeterminate));
        progressDialog.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_indeterminate));
        progressDialog.setCancelable(false);
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void showProgressDialog(boolean flag) {
        if (flag) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void showDialogWithOkButton(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        textViewMessage.setText(message);
        Button buttonOk = (Button) dialogView.findViewById(R.id.ok_btn);
        buttonOk.setText(getString(R.string.ok));
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                onOkClick.OnOkClicked();
            }
        });
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    public void showDialogWithOkButton(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.layout_dialog_ok, null);
        builder.setView(dialogView);
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.messageTv);
        textViewMessage.setText(message);
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

    public void showNoNetworkMessage() {
        showDialogWithOkButton(getString(R.string.error_no_network));
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
