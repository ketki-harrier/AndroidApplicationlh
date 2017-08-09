package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class MeetFragment extends BaseFragmentWithOptions {
    private MainActivity mainActivity;
    private HorizontalCalendar horizontalCalendar;
    private RecyclerView recyclerView;
    private TextView dateDisplayTextView, emptyViewTv;

    @Override
    String getFragmentTag() {
        return "MeetFragment";
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

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_meet, parent, false);

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
        setupToolbarTitle(toolbar, getString(R.string.title_meet));
        dateDisplayTextView = (TextView) view.findViewById(R.id.selectedDateTv);
        emptyViewTv = (TextView) view.findViewById(R.id.emptyViewTv);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.hasFixedSize();
        setupCalendar(view);
    }

    /* for clearing recycleview*/
    private void resetRecycleView() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
    }

    /* for aplying adapter list*/
    private void setRecyclerView(List dataList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        //recyclerView.setAdapter();
    }

    /* For showing empty view when there is no data*/
    private void showEmptyView(boolean flag) {
        if (flag) emptyViewTv.setVisibility(View.VISIBLE);
        else emptyViewTv.setVisibility(View.GONE);
    }

    private void setUpCurrentDate(Date date) {
        dateDisplayTextView.setText(new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH).format(date.getTime()));
    }

    private void setupCalendar(View view) {
        Calendar currentCalendar = Calendar.getInstance();
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        printLog("Current Month" + currentMonth);
        setUpCurrentDate(currentCalendar.getTime());
        /** current Month*/
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
        startDate.set(Calendar.DAY_OF_MONTH, 1);

        //printLog("Start" + startDate.get(Calendar.MONTH) + startDate.get(Calendar.DAY_OF_MONTH));
        /**
         * end after 1 month from now
         */
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(startDate.getTime());
        endDate.add(Calendar.DATE, 60);

        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setupToolbarTitle(toolbar, getString(R.string.title_message));
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(5)
                .dayNameFormat("EEE")
                .dayNumberFormat("dd")
                .monthFormat("MMM")
                .textSize(14f, 24f, 14f)
                .showDayName(true)
                .showMonthName(false)
                .textColor(Color.LTGRAY, Color.BLACK)

                .selectorColor(ContextCompat.getColor(mainActivity, R.color.colorPrimary))
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                printLog("dateFormat" + DateFormat.getDateInstance().format(date));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                setUpCurrentDate(calendar.getTime());
            }

        });


    }

}