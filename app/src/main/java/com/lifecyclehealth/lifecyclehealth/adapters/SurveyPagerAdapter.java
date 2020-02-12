package com.lifecyclehealth.lifecyclehealth.adapters;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionFourFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionOneFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionThreeFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionTwoFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionZeroFragment;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by satyam on 14/04/2017.
 */

public class SurveyPagerAdapter extends FragmentPagerAdapter {
//public class SurveyPagerAdapter extends FragmentStatePagerAdapter {
    public static List<SurveyDetailsModel> surveyDetails;
    CustomViewPager viewPager;
    public static int surveyPosition;
    private Fragment mFragmentAtPos0;
    FragmentManager mFragmentManager;
    Handler handler = new Handler();
    Runnable Update;
    Timer timer;
    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;


    public SurveyPagerAdapter(FragmentManager fm, List<SurveyDetailsModel> surveyDetails) {
        super(fm);
        this.surveyDetails = surveyDetails;
        this.viewPager = null;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        SurveyDetailsModel surveyDetailsModel = surveyDetails.get(position);
        surveyDetailsModel.setPagePosition(position + 1);
        surveyPosition = position;
        int typeOfSurvey = surveyDetailsModel.getQuestionModel().getTypeOfAnswer();
        Log.e("position", position + "");
     //   final String data = new Gson().toJson(surveyDetailsModel);
      //   Parcelable data1 = new Gson().toJson(surveyDetailsModel);
        final Serializable data = new Gson().toJson(surveyDetailsModel);
        switch (typeOfSurvey) {
            case 0:
                Log.e("call", "0");
                //return SurveyOptionThreeFragment.newInstance(data, position);

                return SurveyOptionZeroFragment.newInstance(data, position);

            /*return*/

            case 1:
                Log.e("call", "1");
                return SurveyOptionOneFragment.newInstance(data, position);
            case 2:
                Log.e("call", "2");
                return SurveyOptionTwoFragment.newInstance(data, position);
            case 3:
                Log.e("call", "3");
                return SurveyOptionThreeFragment.newInstance(data, position);
            case 4:
                Log.e("call", "4");
                return SurveyOptionFourFragment.newInstance(data, position);
            default:
                Log.e("call", "default");
                return SurveyOptionZeroFragment.newInstance(data, position);

        }

    }



    @Override
    public int getCount() {
        return surveyDetails.size();
    }


}
