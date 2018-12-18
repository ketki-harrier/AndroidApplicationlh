package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

public class AnswerOption
{
    @SerializedName("Patient_Survey_Question_OptionID")
    private String patientSurveyQuestionId;
    @SerializedName("Name")
    private String name;



    public String getPatientSurveyQuestionId() {
        return patientSurveyQuestionId;
    }

    public void setPatientSurveyQuestionId(String patientSurveyQuestionId) {
        this.patientSurveyQuestionId = patientSurveyQuestionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
