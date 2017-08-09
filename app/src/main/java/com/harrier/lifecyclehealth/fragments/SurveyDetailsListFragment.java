package com.harrier.lifecyclehealth.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.harrier.lifecyclehealth.R;
import com.harrier.lifecyclehealth.activities.MainActivity;
import com.harrier.lifecyclehealth.model.PatientSurveyItem;

import java.util.List;

/**
 * Created by satyam on 14/04/2017.
 */

public class SurveyDetailsListFragment extends BaseFragmentWithOptions {
    private List<PatientSurveyItem> modelList;
    MainActivity mainActivity;
    private static final String SURVEY_LIST_EXTRAS_HOLDER = "List_holder_extra_List";
    private static final String SURVEY_LIST_EXTRAS_HOLDER_TITLE = "List_holder_extra_title";
    private static final String SURVEY_LIST_EXTRAS_HOLDER_POSITION = "List_holder_extra_position";
    private String titleOfSurvey;
    private int positionOfSurvey;


    public static SurveyDetailsListFragment newInstance(String data, String title, int position) {
        SurveyDetailsListFragment holderFragment = new SurveyDetailsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SURVEY_LIST_EXTRAS_HOLDER, data);
        bundle.putInt(SURVEY_LIST_EXTRAS_HOLDER_POSITION, position);
        bundle.putString(SURVEY_LIST_EXTRAS_HOLDER_TITLE, title);
        holderFragment.setArguments(bundle);
        return holderFragment;
    }

    @Override
    String getFragmentTag() {
        return "SurveyDetailsListFragment";
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_survey_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        printLog("ModelInFragment" + getArguments().getString(SURVEY_LIST_EXTRAS_HOLDER));
        modelList = mainActivity.getListConverted(getArguments().getString(SURVEY_LIST_EXTRAS_HOLDER));
        positionOfSurvey = getArguments().getInt(SURVEY_LIST_EXTRAS_HOLDER_POSITION);
        titleOfSurvey = getArguments().getString(SURVEY_LIST_EXTRAS_HOLDER_TITLE);
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, titleOfSurvey);
        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(onClickListener);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabsLayout);
        SurveyHolderAdapter pagerAdapter = new SurveyHolderAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(positionOfSurvey);
        TextView textViewTitle = getScreenTitleTextView();
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backFromSurveyDetailsView();
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backArrowBtn:
                    mainActivity.backFromSurveyDetailsView();
                    break;
            }
        }
    };


    private class SurveyHolderAdapter extends FragmentPagerAdapter {
        public SurveyHolderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String data = new Gson().toJson(modelList.get(position));
            return SurveyDetailsItemFragment.newInstance(data);
        }

        @Override
        public int getCount() {
            return modelList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return modelList.get(position).getName() + " [ " + modelList.get(position).getScheduleDate() + " ]";
        }
    }

}
