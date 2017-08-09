package com.harrier.lifecyclehealth.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.harrier.lifecyclehealth.fragments.SurveyOptionOneFragment;
import com.harrier.lifecyclehealth.fragments.SurveyOptionZeroFragment;
import com.harrier.lifecyclehealth.model.SurveyDetailsModel;

import java.util.List;

/**
 * Created by satyam on 14/04/2017.
 */

public class SurveyPagerAdapter extends FragmentPagerAdapter {
    private List<SurveyDetailsModel> surveyDetails;

    public SurveyPagerAdapter(FragmentManager fm, List<SurveyDetailsModel> surveyDetails) {
        super(fm);
        this.surveyDetails = surveyDetails;
    }

    @Override
    public Fragment getItem(int position) {
        SurveyDetailsModel surveyDetailsModel = surveyDetails.get(position);
        int typeOfSurvey = surveyDetailsModel.getQuestionModel().getTypeOfAnswer();
        String data = new Gson().toJson(surveyDetailsModel);
        switch (typeOfSurvey) {
            case 0:
                return SurveyOptionZeroFragment.newInstance(data);
            case 1:
                return SurveyOptionOneFragment.newInstance(data);
            default:
                return SurveyOptionZeroFragment.newInstance(data);

        }
    }

    @Override
    public int getCount() {
        return surveyDetails.size();
    }


}
