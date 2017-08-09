package com.harrier.lifecyclehealth.dto;

import com.google.gson.annotations.SerializedName;
import com.harrier.lifecyclehealth.model.SurveyQuestion;

/**
 * Created by satyam on 02/05/2017.
 */

public class SurveyDetailsQuestionDto {
    private String status;
    private String message;
    @SerializedName("SurveyQuestion")
    private SurveyQuestion surveyQuestion;

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

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    @Override
    public String toString() {
        return "SurveyDetailsQuestionDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", surveyQuestion=" + surveyQuestion +
                '}';
    }
}
