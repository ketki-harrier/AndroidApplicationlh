package com.harrier.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 14/04/2017.
 */

public class QuestionModel {
    @SerializedName("Patient_Survey_QuestionID")
    private String patientSurveyId;
    @SerializedName("Description")
    private String description;
    @SerializedName("Serial_Number")
    private int serialNumber;
    @SerializedName("IsRequired")
    private boolean isRequired;
    @SerializedName("Type_Of_Answer")
    private int typeOfAnswer;
    @SerializedName("AnswerID")
    private int answerId;
    @SerializedName("HTML_Description")
    private String htmlDescription;
    @SerializedName("Descriptive_Answer")
    private String descriptiveAnswer;
    @SerializedName("Subjective_Answer")
    private String subjectiveAnswer;
    @SerializedName("question_options")
    private List<QuestionOptions> questionOptions;


    public List<QuestionOptions> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOptions> questionOptions) {
        this.questionOptions = questionOptions;
    }

    public QuestionModel() {
    }

    public String getPatientSurveyId() {
        return patientSurveyId;
    }

    public void setPatientSurveyId(String patientSurveyId) {
        this.patientSurveyId = patientSurveyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public int getTypeOfAnswer() {
        return typeOfAnswer;
    }

    public void setTypeOfAnswer(int typeOfAnswer) {
        this.typeOfAnswer = typeOfAnswer;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getHtmlDescription() {
        return htmlDescription;
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public String getDescriptiveAnswer() {
        return descriptiveAnswer;
    }

    public void setDescriptiveAnswer(String descriptiveAnswer) {
        this.descriptiveAnswer = descriptiveAnswer;
    }

    public String getSubjectiveAnswer() {
        return subjectiveAnswer;
    }

    public void setSubjectiveAnswer(String subjectiveAnswer) {
        this.subjectiveAnswer = subjectiveAnswer;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "patientSurveyId='" + patientSurveyId + '\'' +
                ", description='" + description + '\'' +
                ", serialNumber=" + serialNumber +
                ", isRequired=" + isRequired +
                ", typeOfAnswer=" + typeOfAnswer +
                ", answerId=" + answerId +
                ", htmlDescription='" + htmlDescription + '\'' +
                ", descriptiveAnswer='" + descriptiveAnswer + '\'' +
                ", subjectiveAnswer='" + subjectiveAnswer + '\'' +
                ", questionOptions=" + questionOptions +
                '}';
    }
}
