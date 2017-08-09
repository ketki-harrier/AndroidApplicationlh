package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;
import com.harrier.lifecyclehealth.adapters.ConversationAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    private RecyclerView recyclerView;

    @Override
    String getFragmentTag() {
        return "MessageFragment";
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_message, parent, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initializeView(view);
    }

    private void initializeView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, getString(R.string.title_message));
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        setRecyclerView(new ArrayList());
    }

    /* for clearing recycleview*/
    private void resetRecycleView() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
    }

    /* for aplying adapter list*/
    private void setRecyclerView(List dataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        ConversationAdapter conversationAdapter = new ConversationAdapter();
        recyclerView.setAdapter(conversationAdapter);
    }
}