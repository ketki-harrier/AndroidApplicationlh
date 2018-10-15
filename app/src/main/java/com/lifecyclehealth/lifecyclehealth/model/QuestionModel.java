package com.lifecyclehealth.lifecyclehealth.model;

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
    @SerializedName("Deliminater")
    private String Deliminater;
    @SerializedName("Box_Before_Deliminater")
    private String Box_Before_Deliminater;
    @SerializedName("Box_After_Deliminater")
    private String Box_After_Deliminater;

    public String getDeliminater() {
        return Deliminater;
    }

    public void setDeliminater(String deliminater) {
        Deliminater = deliminater;
    }

    public String getBox_Before_Deliminater() {
        return Box_Before_Deliminater;
    }

    public void setBox_Before_Deliminater(String box_Before_Deliminater) {
        Box_Before_Deliminater = box_Before_Deliminater;
    }

    public String getBox_After_Deliminater() {
        return Box_After_Deliminater;
    }

    public void setBox_After_Deliminater(String box_After_Deliminater) {
        Box_After_Deliminater = box_After_Deliminater;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    @SerializedName("Image_Url")
    private String Image_Url;
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


    @SerializedName("WidgetID")
    private String WidgetID;
    @SerializedName("Widget_Name")
    private String Widget_Name;
    @SerializedName("Widget_Type")
    private String Widget_Type;
    @SerializedName("Widget_Orientation")
    private String Widget_Orientation;
    @SerializedName("Min_Value")
    private int Min_Value;
    @SerializedName("Max_Value")
    private int Max_Value;
    @SerializedName("scoreValue")
    private String scoreValue;

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getWidgetID() {
        return WidgetID;
    }

    public void setWidgetID(String widgetID) {
        WidgetID = widgetID;
    }

    public String getWidget_Name() {
        return Widget_Name;
    }

    public void setWidget_Name(String widget_Name) {
        Widget_Name = widget_Name;
    }

    public String getWidget_Type() {
        return Widget_Type;
    }

    public void setWidget_Type(String widget_Type) {
        Widget_Type = widget_Type;
    }

    public String getWidget_Orientation() {
        return Widget_Orientation;
    }

    public void setWidget_Orientation(String widget_Orientation) {
        Widget_Orientation = widget_Orientation;
    }

    public int getMin_Value() {
        return Min_Value;
    }

    public void setMin_Value(int min_Value) {
        Min_Value = min_Value;
    }

    public int getMax_Value() {
        return Max_Value;
    }

    public void setMax_Value(int max_Value) {
        Max_Value = max_Value;
    }

    @SerializedName("Attachment_Url")
    private String Attachment_Url;
    @SerializedName("Attachment_Name")
    private String Attachment_Name;
    @SerializedName("Video_Url")
    private String Video_Url;

    public String getAttachment_Type() {
        return Attachment_Type;
    }

    public void setAttachment_Type(String attachment_Type) {
        Attachment_Type = attachment_Type;
    }

    @SerializedName("Attachment_Type")
    private String Attachment_Type;

    public String getAttachment_Url() {
        return Attachment_Url;
    }

    public void setAttachment_Url(String attachment_Url) {
        Attachment_Url = attachment_Url;
    }

    public String getAttachment_Name() {
        return Attachment_Name;
    }

    public void setAttachment_Name(String attachment_Name) {
        Attachment_Name = attachment_Name;
    }

    public String getVideo_Url() {
        return Video_Url;
    }

    public void setVideo_Url(String video_Url) {
        Video_Url = video_Url;
    }
}
