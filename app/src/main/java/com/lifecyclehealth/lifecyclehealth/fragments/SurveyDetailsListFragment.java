package com.lifecyclehealth.lifecyclehealth.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.adapters.CustomViewPager;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;
import com.lifecyclehealth.lifecyclehealth.utils.PreferenceUtils;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

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
    public static boolean isToDo = false;
    public static boolean isCompleted = false;
    public static boolean isSchedule = false;
    private int positionOfSurvey;
    TabLayout tabLayout;
    private ColorCode colorCode;
    String Stringcode;
    PreferenceUtils preferenceUtils;


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
        //initView(view);
        initViewFragment(view);
    }


    private void initViewFragment(View view) {
        // try {
        String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
        colorCode = new Gson().fromJson(resposne, ColorCode.class);
        String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
        String Stringcode = "";
        String hashcode = "";

        if (demo == null) {
            hashcode = "Green";
            Stringcode = "259b24";
        } else if (demo != null) {
            String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
            hashcode = arr[0].trim();
            Stringcode = arr[1].trim();
      /*  }
            else*/
            if (hashcode.equals("Black") && Stringcode.length() < 6) {
                Stringcode = "333333";
            }
        }
        // }catch (Exception e){e.printStackTrace();}
        Analytics.with(getContext()).screen("Survey Questions");
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, titleOfSurvey);
        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setColorFilter(Color.parseColor("#" + Stringcode));
        imageView.setOnClickListener(onClickListener);
        // ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        CustomViewPager viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabsLayout);
        tabLayout.setBackgroundColor(Color.parseColor("#" + Stringcode));
        //tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#"+Stringcode));

        String surveyTitle = null;

        if (titleOfSurvey.equals("To Do")) {
            isToDo = true;
            isCompleted = false;
            surveyTitle = titleOfSurvey;
        } else if (titleOfSurvey.equals("Completed")) {
            isCompleted = true;
            isToDo = false;
            surveyTitle = titleOfSurvey;
        } else if (titleOfSurvey.contains("Provider Survey")) {
            isCompleted = false;
            isToDo = true;
            titleOfSurvey = "To Do";
            surveyTitle = "Survey";
        } else if (titleOfSurvey.contains("Survey")) {
            isCompleted = false;
            isToDo = true;
            titleOfSurvey = "To Do";
            surveyTitle = "Survey";
        } else {
            isToDo = false;
            isCompleted = false;
            isSchedule = true;
            surveyTitle = titleOfSurvey;
        }

        newsetupToolbarTitle(toolbar, surveyTitle, colorCode.getVisualBrandingPreferences().getColorPreference());

        for (int position = 0; position < modelList.size(); position++) {


            if (modelList.get(position).getScheduleDate().equals(""))
                tabLayout.addTab(tabLayout.newTab().setText(modelList.get(position).getName()));
            else

                tabLayout.addTab(tabLayout.newTab().setText(modelList.get(position).getName() + " [ " + modelList.get(position).getScheduleDate() + " ]"));
            tabLayout.setTag(position);
        }

        if (modelList.size() > 0) {
            Boolean checkESign = Boolean.valueOf(modelList.get(positionOfSurvey).getRequireESignature());
            PreferenceUtils.saveESignature(getContext(), checkESign);
            String data = new Gson().toJson(modelList.get(positionOfSurvey));
            printLog("Tab selected data" + data);
            Analytics.with(getContext()).track("Patient Survey Question", new Properties().putValue("category", "Mobile"));
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    //tabLayout.getTabAt(positionOfSurvey).select();
                    tabLayout.setSmoothScrollingEnabled(true);
                    tabLayout.setScrollPosition(positionOfSurvey, 0f, true);
                }
            });
            Bundle bundle = new Bundle();
            bundle.putString("holder_extra", data);
            Fragment fragment = new SurveyDetailsItemFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("tab position", tab.getPosition() + "");
                String data = new Gson().toJson(modelList.get(tab.getPosition()));
                printLog("Tab selected data" + data);
                positionOfSurvey = tab.getPosition();
                Bundle bundle = new Bundle();
                bundle.putString("holder_extra", data);
                Fragment fragment = new SurveyDetailsItemFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
                // SurveyDetailsItemFragment.newInstance(data);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        TextView TextViewTitle = getScreenTitleTextView();
        TextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backFromSurveyDetailsView();
            }
        });
    }


   /* private void initView(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setupToolbarTitle(toolbar, titleOfSurvey);
        ImageView imageView = (ImageView) view.findViewById(R.id.backArrowBtn);
        imageView.setOnClickListener(onClickListener);
        // ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        CustomViewPager viewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabsLayout);
        SurveyHolderAdapter pagerAdapter = new SurveyHolderAdapter(getChildFragmentManager());
        viewPager.setAdapter(null);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(positionOfSurvey);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (titleOfSurvey.equals("To Do")) {
            viewPager.disableScroll(true);
            isToDo = true;
            isCompleted = false;
        } else if (titleOfSurvey.equals("Completed")) {
            viewPager.disableScroll(true);
            isCompleted = true;
            isToDo = false;
        } else {
            viewPager.disableScroll(false);
            isToDo = false;
            isCompleted = false;
        }
        TextView TextViewTitle = getScreenTitleTextView();
        TextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.backFromSurveyDetailsView();
            }
        });

    }*/

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
            if (modelList.get(position).getScheduleDate().equals(""))
                return modelList.get(position).getName();
            else
                return modelList.get(position).getName() + " [ " + modelList.get(position).getScheduleDate() + " ]";
        }
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

                    //Toast.makeText(getContext(),"back_arrow key pressed",Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack("SurveyDetailsListFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;

                }

                return false;
            }
        });

    }

}
