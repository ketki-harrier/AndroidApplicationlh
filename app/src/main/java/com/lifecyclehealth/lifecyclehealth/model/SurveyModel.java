package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 14/04/2017.
 */

public class SurveyModel {
    @SerializedName("SurveyName")
    private String surveyName;
    @SerializedName("Name")
    private String name;
    @SerializedName("question")
    private List<QuestionModel> questionModel;


    public SurveyModel() {
    }

    public SurveyModel(String surveyName, String name, List<QuestionModel> questionModel) {
        this.surveyName = surveyName;
        this.name = name;
        this.questionModel = questionModel;
    }

    public List<QuestionModel> getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(List<QuestionModel> questionModel) {
        this.questionModel = questionModel;
    }


    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SurveyModel{" +
                "surveyName='" + surveyName + '\'' +
                ", name='" + name + '\'' +
                ", questionModel=" + questionModel +
                '}';
    }
}
