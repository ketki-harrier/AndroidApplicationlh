package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 14/04/2017.
 */

public class QuestionOptions {
    @SerializedName("Patient_Survey_Question_OptionID")
    private int optionId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Question_Option_Value")
    private int optionValue;
    @SerializedName("Serial_Number")
    private int serialNumber;
    @SerializedName("patient_survey_question_answeroptionid")
    private int questionAnswerId;
    @SerializedName("Patient_Survey_Response_Question_AnswerID")
    private int responseQuestionAnswerId;
    @SerializedName("Question_Option_Value_From")
    private int Question_Option_Value_From;

    public int getMultiple_Options() {
        return Multiple_Options;
    }

    public void setMultiple_Options(int multiple_Options) {
        Multiple_Options = multiple_Options;
    }

    @SerializedName("Patient_Survey_Question_OptionID1")
    private int Multiple_Options;


    public int getQuestion_Option_Value_From() {
        return Question_Option_Value_From;
    }

    public void setQuestion_Option_Value_From(int question_Option_Value_From) {
        Question_Option_Value_From = question_Option_Value_From;
    }

    public int getQuestion_Option_Value_To() {
        return Question_Option_Value_To;
    }

    public void setQuestion_Option_Value_To(int question_Option_Value_To) {
        Question_Option_Value_To = question_Option_Value_To;
    }

    @SerializedName("Question_Option_Value_To")
    private int Question_Option_Value_To;


    public QuestionOptions() {
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(int optionValue) {
        this.optionValue = optionValue;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(int questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    public int getResponseQuestionAnswerId() {
        return responseQuestionAnswerId;
    }

    public void setResponseQuestionAnswerId(int responseQuestionAnswerId) {
        this.responseQuestionAnswerId = responseQuestionAnswerId;
    }

    @Override
    public String toString() {
        return "QuestionOptions{" +
                "optionId=" + optionId +
                ", name='" + name + '\'' +
                ", optionValue=" + optionValue +
                ", serialNumber=" + serialNumber +
                ", questionAnswerId=" + questionAnswerId +
                ", responseQuestionAnswerId=" + responseQuestionAnswerId +
                '}';
    }
}
