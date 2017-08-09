package com.harrier.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 02/05/2017.
 */

public class SurveyQuestion {
    @SerializedName("survey_details")
    private SurveyDetails surveyDetails;

    public SurveyDetails getSurveyDetails() {
        return surveyDetails;
    }

    public void setSurveyDetails(SurveyDetails surveyDetails) {
        this.surveyDetails = surveyDetails;
    }

    @Override
    public String toString() {
        return "SurveyQuestion{" +
                "surveyDetails=" + surveyDetails +
                '}';
    }
}
