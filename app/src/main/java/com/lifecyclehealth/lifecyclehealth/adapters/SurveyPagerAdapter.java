package com.lifecyclehealth.lifecyclehealth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionFourFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionOneFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionThreeFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionTwoFragment;
import com.lifecyclehealth.lifecyclehealth.fragments.SurveyOptionZeroFragment;
import com.lifecyclehealth.lifecyclehealth.model.SurveyDetailsModel;

import java.util.List;

/**
 * Created by satyam on 14/04/2017.
 */

public class SurveyPagerAdapter extends FragmentPagerAdapter {
    public static List<SurveyDetailsModel> surveyDetails;
    CustomViewPager viewPager;
    public static int surveyPosition;

    public SurveyPagerAdapter(FragmentManager fm, List<SurveyDetailsModel> surveyDetails) {
        super(fm);
        this.surveyDetails = surveyDetails;
        this.viewPager = null;
    }

    @Override
    public Fragment getItem(int position) {
        SurveyDetailsModel surveyDetailsModel = surveyDetails.get(position);
        surveyDetailsModel.setPagePosition(position + 1);
        surveyPosition = position;
        int typeOfSurvey = surveyDetailsModel.getQuestionModel().getTypeOfAnswer();
        Log.e("position", position + "");
        String data = new Gson().toJson(surveyDetailsModel);
        switch (typeOfSurvey) {
            case 0:
                Log.e("call", "0");
                //return SurveyOptionThreeFragment.newInstance(data, position);
                return SurveyOptionZeroFragment.newInstance(data, position);
            case 1:
                Log.e("call", "1");
                return SurveyOptionOneFragment.newInstance(data, position);
            case 2:
                Log.e("call", "2");
                return SurveyOptionTwoFragment.newInstance(data, position);
            case 3:
                Log.e("call", "3");
                //return SurveyOptionZeroFragment.newInstance(data, position);
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
