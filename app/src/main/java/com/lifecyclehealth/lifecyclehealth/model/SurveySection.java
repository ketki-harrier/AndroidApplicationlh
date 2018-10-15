package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 01/05/2017.
 */

public class SurveySection {
    @SerializedName("Patient_Survey_SectionID")
    private int patientSurveySectionId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("Type_Of_Section")
    private int typeOfSection;
    @SerializedName("question")
    private List<QuestionModel> questionModels;

    public int getPatientSurveySectionId() {
        return patientSurveySectionId;
    }

    public void setPatientSurveySectionId(int patientSurveySectionId) {
        this.patientSurveySectionId = patientSurveySectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeOfSection() {
        return typeOfSection;
    }

    public void setTypeOfSection(int typeOfSection) {
        this.typeOfSection = typeOfSection;
    }

    public List<QuestionModel> getQuestionModels() {
        return questionModels;
    }

    public void setQuestionModels(List<QuestionModel> questionModels) {
        this.questionModels = questionModels;
    }

    @Override
    public String toString() {
        return "SurveySection{" +
                "patientSurveySectionId=" + patientSurveySectionId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeOfSection=" + typeOfSection +
                ", questionModels=" + questionModels +
                '}';
    }

    public String getPatient_Survey_ResponseID() {
        return Patient_Survey_ResponseID;
    }

    public void setPatient_Survey_ResponseID(String patient_Survey_ResponseID) {
        Patient_Survey_ResponseID = patient_Survey_ResponseID;
    }

    private String Patient_Survey_ResponseID;

    public int getSizeOfSurvey() {
        return sizeOfSurvey;
    }

    public void setSizeOfSurvey(int sizeOfSurvey) {
        this.sizeOfSurvey = sizeOfSurvey;
    }

    private  int sizeOfSurvey;


    public String getSurveyID() {
        return SurveyID;
    }

    public void setSurveyID(String surveyID) {
        SurveyID = surveyID;
    }

    private  String SurveyID;


    public String getPatient_Total_Survey_Score() {
        return Patient_Total_Survey_Score;
    }

    public void setPatient_Total_Survey_Score(String patient_Total_Survey_Score) {
        Patient_Total_Survey_Score = patient_Total_Survey_Score;
    }

    public String getSubmission_DateTime() {
        return Submission_DateTime;
    }

    public void setSubmission_DateTime(String submission_DateTime) {
        Submission_DateTime = submission_DateTime;
    }

    public String getSubmitting_User_FirstName() {
        return Submitting_User_FirstName;
    }

    public void setSubmitting_User_FirstName(String submitting_User_FirstName) {
        Submitting_User_FirstName = submitting_User_FirstName;
    }

    public String getSubmitting_User_LastName() {
        return Submitting_User_LastName;
    }

    public void setSubmitting_User_LastName(String submitting_User_LastName) {
        Submitting_User_LastName = submitting_User_LastName;
    }

    private  String Submission_DateTime;
    private  String Submitting_User_FirstName;
    private  String Submitting_User_LastName;
    private  String Patient_Total_Survey_Score;
    private  String FirstName;
    private  String LastName;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
