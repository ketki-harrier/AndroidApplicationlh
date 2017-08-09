package com.harrier.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 28/04/2017.
 */

public class SurveyPlan {
    @SerializedName("PatientID")
    private String patientId;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("ToDo_Patient_Survey")
    private List<PatientSurveyItem> surveyPlanTodo;
    @SerializedName("Completed_Patient_Survey")
    private List<PatientSurveyItem> surveyPlanCompleted;
    @SerializedName("Scheduled_Patient_Survey")
    private List<PatientSurveyItem> surveyPlanSchedule;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<PatientSurveyItem> getSurveyPlanTodo() {
        return surveyPlanTodo;
    }

    public void setSurveyPlanTodo(List<PatientSurveyItem> surveyPlanTodo) {
        this.surveyPlanTodo = surveyPlanTodo;
    }

    public List<PatientSurveyItem> getSurveyPlanCompleted() {
        return surveyPlanCompleted;
    }

    public void setSurveyPlanCompleted(List<PatientSurveyItem> surveyPlanCompleted) {
        this.surveyPlanCompleted = surveyPlanCompleted;
    }

    public List<PatientSurveyItem> getSurveyPlanSchedule() {
        return surveyPlanSchedule;
    }

    public void setSurveyPlanSchedule(List<PatientSurveyItem> surveyPlanSchedule) {
        this.surveyPlanSchedule = surveyPlanSchedule;
    }

    @Override
    public String toString() {
        return "SurveyPlan{" +
                "patientId='" + patientId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", surveyPlanTodo=" + surveyPlanTodo +
                ", surveyPlanCompleted=" + surveyPlanCompleted +
                ", surveyPlanSchedule=" + surveyPlanSchedule +
                '}';
    }
}
