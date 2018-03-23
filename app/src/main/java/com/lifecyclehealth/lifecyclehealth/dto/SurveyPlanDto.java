package com.lifecyclehealth.lifecyclehealth.dto;

import com.google.gson.annotations.SerializedName;
import com.lifecyclehealth.lifecyclehealth.model.SurveyPlan;

/**
 * Created by satyam on 01/05/2017.
 */

public class SurveyPlanDto {
    private String status;
    private String message;
    @SerializedName("Survey_Plan")
    public SurveyPlan surveyPlan;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SurveyPlan getSurveyPlan() {
        return surveyPlan;
    }

    public void setSurveyPlan(SurveyPlan surveyPlan) {
        this.surveyPlan = surveyPlan;
    }

    @Override
    public String toString() {
        return "SurveyPlanDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", surveyPlan=" + surveyPlan +
                '}';
    }
}
