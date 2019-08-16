package com.lifecyclehealth.lifecyclehealth.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.ChatActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.MessageDialogAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MessageDialogMeetAdapter;
import com.lifecyclehealth.lifecyclehealth.adapters.MessageDialogNotificationAdapter;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.OnOkClick;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.InviteUserMeetResponse;
import com.lifecyclehealth.lifecyclehealth.model.MessageDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.NotificationDialogResponse;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.meet.model.Meet;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_CREATE_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_GET_CONVERSATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MESSAGE_GET_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_READ_NOTIFICATION;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.messageCount;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.notificationCount;

/**
 * Created by satyam on 14/04/2017.
 */

public abstract class BaseFragmentWithOptions extends Fragment {
    private ProgressDialog progressDialog;
    private TextView screenTitleTextView;
    public static final String RIGHT = "right_menu";
    public static final String LEFT = "left_menu";
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewMeet;
    private TextView messageHint;
    public Button view_all_btn;
    static TextView textViewCount;
    MainActivity activity;

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


    android.support.v7.app.AlertDialog alertDialogNotification;

    /* For changing Title of Toolbar*/
    public void setupToolbarTitle(Toolbar toolbar, String title) {
        screenTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        screenTitleTextView.setText(title);
    }

    public void newsetupToolbarTitle(Toolbar toolbar, String title,String hashCode) {
        try {
            screenTitleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
            screenTitleTextView.setText(title);
            String[] arr  = hashCode.split("#");
            String hashcode = arr[0].trim();
            String Stringcode = arr[1].trim();
            if (hashcode.equals("Black") && Stringcode.length() < 6){
                Stringcode = "333333";
            }
            screenTitleTextView.setTextColor(Color.parseColor("#"+Stringcode));
        }catch (Exception e){e.printStackTrace();}
    }

    public TextView getScreenTitleTextView() {
        return screenTitleTextView;
    }

    View dialogViewNotification;

    public void getDialogSetup(MainActivity context, String menu, TextView textViewCount) {
        activity = context;
        this.textViewCount = textViewCount;
        Analytics.with(context).track("Read notification from NotificaitonPopUp", new Properties().putValue("category", "Mobile"));
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        dialogViewNotification = layoutInflater.inflate(R.layout.layout_dilog_top_left, null);

        builder.setView(dialogViewNotification);

        RelativeLayout frameLayout = (RelativeLayout) dialogViewNotification.findViewById(R.id.layout_frame);
        View viewRightBubble = (View) dialogViewNotification.findViewById(R.id.viewBubbleRight);
        View viewLeftBubble = (View) dialogViewNotification.findViewById(R.id.viewBubbleLeft);


        alertDialogNotification = builder.create();
        Window window = alertDialogNotification.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
        wmlp.x = 30;//x position
        wmlp.y = 100;//y position
        wmlp.windowAnimations = R.style.dialog_animation;
        window.setAttributes(wmlp);
        alertDialogNotification.show();


        if (menu.equalsIgnoreCase(RIGHT)) {
            Analytics.with(getContext()).track("Read message from MessageNotificaitonPopUp", new Properties().putValue("category", "Mobile"));
            setBackground(frameLayout, R.drawable.bg_round_corner);
            viewRightBubble.setVisibility(View.VISIBLE);
            viewLeftBubble.setVisibility(View.GONE);
            setMessage();
        } else {
            Analytics.with(getContext()).track("Read notification from NotificaitonPopUp", new Properties().putValue("category", "Mobile"));
            setBackground(frameLayout, R.drawable.bg_round_corner_right_top);
            viewRightBubble.setVisibility(View.GONE);
            viewLeftBubble.setVisibility(View.VISIBLE);
            setNotification();
        }
    }

    NestedScrollView nested;

    private void setMessage() {

        nested = (NestedScrollView) dialogViewNotification.findViewById(R.id.nested);
        recyclerView = (RecyclerView) dialogViewNotification.findViewById(R.id.recycleView);
        recyclerViewMeet = (RecyclerView) dialogViewNotification.findViewById(R.id.recycleView_Meet);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.hasFixedSize();
        messageHint = (TextView) dialogViewNotification.findViewById(R.id.messageHint);
        recyclerViewMeet.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewMeet.hasFixedSize();
        //getMessageList();
        view_all_btn = (Button) dialogViewNotification.findViewById(R.id.view_all_btn);

        view_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogNotification.dismiss();
                MainActivity.bottomBar.selectTabWithId(R.id.tab_message);
                MainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_message);
                FragmentManager fm = getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment fragment = new MessageFragment();
                String fragmentTag = fragment.getClass().getName();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        setMessageData();
    }

    private void setMessageData() {

        if (MainActivity.meetLists != null) {
            if (MainActivity.meetLists.size() > 0) {
                MessageDialogMeetAdapter meetAdapter = new MessageDialogMeetAdapter(MainActivity.meetLists, getContext(), new MessageDialogMeetAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Meet meet, String Type, final String pos) {

                        try {
                            switch (Type) {
                                case "C": {
                                    printLog("meetId" + meet.getID());
                                    MainActivity.mMeetRepo.declineMeet(meet.getID(), new ApiCallback<Void>() {
                                        @Override
                                        public void onCompleted(Void aVoid) {
                                            printLog("meet declineMeet done");
                                            MainActivity.meetLists.remove(Integer.parseInt(pos));
                                            setMessageData();
                                        }

                                        @Override
                                        public void onError(int i, String s) {

                                        }
                                    });
                                    break;
                                }

                                case "A": {
                                    printLog("meetId accept" + meet.getID());
                                    try {
                                        MainActivity.mMeetRepo.acceptMeet(meet.getID(), new ApiCallback<Meet>() {
                                            @Override
                                            public void onCompleted(Meet meet) {
                                                printLog("meet acceptMeet done");
                                                MainActivity.meetLists.remove(Integer.parseInt(pos));
                                                setMessageData();
                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                                printLog("error for accept meet" + s + " value " + i);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }

                                case "J": {
                                    printLog("meetId" + meet.getID());
                                    alertDialogNotification.dismiss();
                                    MyApplication.getInstance().addToSharedPreference(SESSION_KEY, meet.getID());
                                    MeetEventActivity.joinMeet(getContext());

                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                recyclerViewMeet.setAdapter(meetAdapter);
            }
        }

        if (MainActivity.chatList != null) {
            MessageDialogAdapter adapter = new MessageDialogAdapter(MainActivity.chatList, getContext(), new MessageDialogAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Chat chat, String Type, String pos) {
                    alertDialogNotification.dismiss();
                    ChatActivity.showChat(getActivity(), chat);
                }
            });


            if (MainActivity.chatList.size() > 0) {
                nested.setVisibility(View.VISIBLE);
                messageHint.setVisibility(View.GONE);
            } else if (MainActivity.meetLists.size() > 0) {
                nested.setVisibility(View.VISIBLE);
                messageHint.setVisibility(View.GONE);
            } else {
                messageHint.setText("No new message exist");
                nested.setVisibility(View.GONE);
                messageHint.setVisibility(View.VISIBLE);
            }

            recyclerView.setAdapter(adapter);
        }

    }


    private void setNotification() {

        nested = (NestedScrollView) dialogViewNotification.findViewById(R.id.nested);
        recyclerView = (RecyclerView) dialogViewNotification.findViewById(R.id.recycleView);
        recyclerViewMeet = (RecyclerView) dialogViewNotification.findViewById(R.id.recycleView_Meet);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.hasFixedSize();
        messageHint = (TextView) dialogViewNotification.findViewById(R.id.messageHint);
        recyclerViewMeet.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewMeet.hasFixedSize();
        view_all_btn = (Button) dialogViewNotification.findViewById(R.id.view_all_btn);

        //messageHint.setText("Coming Soon");
        //nested.setVisibility(View.GONE);
        // messageHint.setVisibility(View.VISIBLE);

        view_all_btn = (Button) dialogViewNotification.findViewById(R.id.view_all_btn);

        view_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogNotification.dismiss();
                MainActivity.bottomBar.selectTabWithId(R.id.tab_more);
                MainActivity.bottomBarCaregiver.selectTabWithId(R.id.tab_more);
                FragmentManager fm = getFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                Fragment fragment = new NotificationFragment();
                String fragmentTag = fragment.getClass().getName();
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_main, fragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        setNotificationData();


    }

    MessageDialogNotificationAdapter notificationAdapter;

    private void setNotificationData() {

        if (MainActivity.notificationDialogResponse.getAlertList() != null) {
            nested.setVisibility(View.VISIBLE);
            messageHint.setVisibility(View.GONE);
            notificationAdapter = new MessageDialogNotificationAdapter(MainActivity.notificationDialogResponse.getAlertList(), getContext(), new MessageDialogNotificationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(NotificationDialogResponse.AlertList item, String Type, String pos) {
                    if (item.getStatus().trim().equals("New")) {
                        printLog("position" + pos);
                        readNotification(item.getAlertID(), pos);
                    }
                }
            });
            recyclerView.setAdapter(notificationAdapter);
        } else {
            messageHint.setText("No new notification exist");
            nested.setVisibility(View.GONE);
            messageHint.setVisibility(View.VISIBLE);
        }

    }


    private void setBackground(RelativeLayout background, @DrawableRes int rource) {
        if (Build.VERSION.SDK_INT >= 16) {
            background.setBackground(ContextCompat.getDrawable(getActivity(), rource));
        } else {
            background.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), rource));
        }
    }

    public void setProgressDialog() {
        String Stringcode = "";
        //String Stringcode;
        String hashcode = "";
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);
            if (resposne != null) {
               // ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);

                String demo = colorCode.getVisualBrandingPreferences().getColorPreference();

                if(demo == null){
                    hashcode = "Green";
                    Stringcode = "259b24";
                }
                else {
                    String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                    hashcode = arr[0].trim();
                    Stringcode = arr[1].trim();
                }
                }
            else if(colorCode.getVisualBrandingPreferences().getColorPreference().equals(null)){
            hashcode = "Green";
            Stringcode = "259b24";
        }
/*else if () {
                Toast.makeText(activity, "Response Null", Toast.LENGTH_SHORT).show();
            }*/
            else{
                hashcode = "Green";
                Stringcode = "259b24";
            }




        progressDialog = ProgressDialog.show(getActivity(), null, null);
        if(hashcode.equals("Blue")){
            progressDialog.setContentView(R.layout.progress_blue);
        }else if (hashcode.equals("Black")){
            progressDialog.setContentView(R.layout.progress_black);
        }else if (hashcode.equals("SkyBlue")){
            progressDialog.setContentView(R.layout.progress_skyblue);
        }else {
            progressDialog.setContentView(R.layout.layout_progress_bar);
        }
        //progressDialog.setContentView(R.layout.layout_progress_bar);
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
        if (progressDialog != null) {
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
    }

    public void showDialogWithOkButton(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

    public void showDialogWithOkCancelButton(String message, final OnOkClick onOkClick) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

    public void showNoNetworkMessage() {
        showDialogWithOkButton(getString(R.string.error_no_network));
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void callShow() {
        setProgressDialog();
    }

    protected void replaceFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, fragment, fragmentTag)
                .addToBackStack(fragmentTag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void readNotification(String alertId, final String pos) {
        showProgressDialog(true);
        if (isConnectedToNetwork(activity)) {
            try {

                activity.networkRequestUtil.putDataSecure(BASE_URL + URL_READ_NOTIFICATION + alertId, null, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        showProgressDialog(false);
                        printLog("Response read notification" + response);
                        if (response != null) {
                            InviteUserMeetResponse inviteUserMeetResponse = new Gson().fromJson(response.toString(), InviteUserMeetResponse.class);
                            if (inviteUserMeetResponse != null) {
                                if (inviteUserMeetResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    MainActivity.notificationDialogResponse.getAlertList().get(Integer.parseInt(pos)).setStatus("Read");
                                    notificationAdapter.notifyDataSetChanged();
                                    String notificationCount1 = MyApplication.getInstance().getFromSharedPreference(AppConstants.notificationCount);
                                    int value = Integer.parseInt(notificationCount1);
                                    value = value - 1;
                                    notificationCount1 = value + "";
                                    textViewCount.setText(notificationCount1);
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


    public void getNotificationCount(final TextView textView, MainActivity context) {
        activity = context;
        MyApplication.getInstance().addToSharedPreference(notificationCount, "0");
        if (isConnectedToNetwork(activity)) {
            try {
                activity.networkRequestUtil.getDataSecure(BASE_URL + URL_MESSAGE_GET_NOTIFICATION, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        printLog("Response Of Notification list:" + response);
                        if (response != null) {
                            NotificationDialogResponse notificationDialogResponse = new Gson().fromJson(response.toString(), NotificationDialogResponse.class);
                            if (notificationDialogResponse != null) {
                                if (notificationDialogResponse.getStatus().equalsIgnoreCase(STATUS_SUCCESS)) {
                                    MainActivity.notificationDialogResponse = notificationDialogResponse;
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, notificationDialogResponse.getAlertList().get(0).getNewAlertCount());
                                    textView.setText(notificationDialogResponse.getAlertList().get(0).getNewAlertCount());
                                } else {
                                    MyApplication.getInstance().addToSharedPreference(notificationCount, "0");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (Exception e) {
            }
        } else {
            //showNoNetworkMessage();
        }
    }

}
