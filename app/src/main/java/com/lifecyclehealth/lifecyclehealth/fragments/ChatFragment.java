package com.lifecyclehealth.lifecyclehealth.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.moxtra.sdk.ChatClient;
import com.moxtra.sdk.chat.controller.ChatController;
import com.moxtra.sdk.chat.impl.ChatDetailImpl;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.chat.model.ChatDetail;
import com.moxtra.sdk.chat.repo.ChatRepo;
import com.moxtra.sdk.client.ChatClientDelegate;
import com.moxtra.sdk.common.ApiCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragmentWithOptions {

    private MainActivity mainActivity;
    private static final String KEY_CHAT = "chat";
    private static final String KEY_ACTION = "action";
    private  final String KEY_TOPIC = "topic";
    private  final String KEY_UNIQUE_ID_LIST = "uniqueIdList";

    private static final String ACTION_SHOW = "show";
    private  final String ACTION_START = "start";

    private static final String TAG = "DEMO_ChatActivity";

    private final Handler mHandler = new Handler();
    private ChatClientDelegate mChatClientDelegate;
    private ChatController mChatController;
    private ChatRepo mChatRepo;

    private Chat mChat;

    public static ChatFragment showChat(Chat chat) {

        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ACTION, ACTION_SHOW);
        args.putParcelable(KEY_CHAT, chat);
        fragment.setArguments(args);
        return fragment;
    }

 /*   public static void startGroupChat(Context ctx, String topic, ArrayList<String> uniqueIdList) {
        Intent intent = new Intent(ctx, ChatActivity.class);
        intent.putExtra(KEY_ACTION, ACTION_START);
        intent.putExtra(KEY_TOPIC, topic);
        intent.putStringArrayListExtra(KEY_UNIQUE_ID_LIST, uniqueIdList);
        ctx.startActivity(intent);
     }*/

    @Override
    String getFragmentTag() {
        return "ChatFragment";
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
        return inflater.inflate(R.layout.activity_chat, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        Bundle intent = getArguments();
        if (intent == null) {
            Log.e(TAG, "no intent received");
            finishInMainThread();
            return;
        }
        mChatClientDelegate = ChatClient.getClientDelegate();
        if (mChatClientDelegate == null) {
            Log.e(TAG, "ChatClient is null");
            finishInMainThread();
            return;
        }
        String action = intent.getString(KEY_ACTION);
        showProgressDialog(true);
        if (ACTION_SHOW.equals(action)) {
            //showChat(intent);
            mChat = getArguments().getParcelable(KEY_CHAT);
            if (mChat == null) {
                Log.e(TAG, "No chat found");
                finishInMainThread();
                return;
            }
            showChatFragment();
        } else if (ACTION_START.equals(action)) {
            startChat();
        } else {
            finishInMainThread();
        }
    }


    private void startChat() {
        String topic = getArguments().getString(KEY_TOPIC);
        final List<String> uniqueIdList = getArguments().getStringArrayList(KEY_UNIQUE_ID_LIST);
        final String orgId = null;
        mChatRepo = mChatClientDelegate.createChatRepo();
        mChatRepo.createGroupChat(topic, new ApiCallback<Chat>() {
            @Override
            public void onCompleted(Chat chat) {
                Log.i(TAG, "Create group chat successfully.");
                mChat = chat;

                ChatDetail chatDetail;
                chatDetail =  new ChatDetailImpl(chat);

                chatDetail.inviteMembers(orgId, uniqueIdList, new ApiCallback<Void>() {
                    @Override
                   public void onCompleted(Void result) {
                        Log.i(TAG, "Invite members successfully.");
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        Log.e(TAG, "Failed to invite members, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                    }
                });
                showChatFragment();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Log.e(TAG, "Failed to create group chat, errorCode=" + errorCode + ", errorMsg=" + errorMsg);
                finishInMainThread();
            }
        });
    }


    private void finishInMainThread() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                backPage();
            }
        });
    }

    private void showChatFragment() {
        mChatController = mChatClientDelegate.createChatController(mChat);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.chat_frame);
                if (fragment == null) {
                    fragment = mChatController.createChatFragment();
                    getFragmentManager().beginTransaction().add(R.id.chat_frame, fragment).commit();
                }
                showProgressDialog(false);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroy();
        if (mChatController != null) {
            mChatController.cleanup();
        }
        Log.d(TAG, "onCreate");
    }

    private void backPage() {
        getFragmentManager().popBackStack();

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
