package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 01/05/2017.
 */

public class SurveyDetails {
    @SerializedName("PatientID")
    private String patientId;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("Survey_Template_Name")
    private String surveyTemplateName;
    @SerializedName("Submitting_User_FirstName")
    private String submittingUserFirstName;
    @SerializedName("Submitting_User_LastName")
    private String submittingUserLastName;
    @SerializedName("SurveyID")
    private int surveyId;
    @SerializedName("SurveyName")
    private String surveyName;
    @SerializedName("Show_Serial_Number")
    private String showSerialNumber;
    @SerializedName("SurveyDescription")
    private String surveyDescription;
    @SerializedName("Initiating_HealthCare_OrgID")
    private String initialHealthCareOrgId;
    @SerializedName("Initiating_ClinicID")
    private String initiatingClinicId;
    @SerializedName("Answer_Display_Orientation")
    private String answerDisplayOrientation;
    @SerializedName("Provider_UserID")
    private int providerUserId;
    @SerializedName("Patient_Survey_ResponseID")
    private String patientSurveyResponseId;
    @SerializedName("Patient_Survey_Response_Status")
    private String patientSurveyResponseStatus;
    @SerializedName("Submission_DateTime")
    private String submissionDateAndTime;
    @SerializedName("Scheduled_Date")
    private String scheduleDate;
    @SerializedName("Maximum_Total_Survey_Score")
    private int maxTotalSurveyScore;
    @SerializedName("Maximum_Total_Survey_Score_Units")
    private String maxTotalSurveyScoreUnits;
    @SerializedName("Patient_Total_Survey_Score")
    private String totalSurveyScore;
    @SerializedName("Patient_Total_Survey_Score_Units")
    private String totalSurveyScoreUnits;
    @SerializedName("Percentage_Patient_Total_Survey_Score")
    private String percentTotalSurveyScore;
    @SerializedName("Survey_Section")
    private List<SurveySection> surveySection;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSurveyTemplateName() {
        return surveyTemplateName;
    }

    public void setSurveyTemplateName(String surveyTemplateName) {
        this.surveyTemplateName = surveyTemplateName;
    }

    public String getSubmittingUserFirstName() {
        return submittingUserFirstName;
    }

    public void setSubmittingUserFirstName(String submittingUserFirstName) {
        this.submittingUserFirstName = submittingUserFirstName;
    }

    public String getSubmittingUserLastName() {
        return submittingUserLastName;
    }

    public void setSubmittingUserLastName(String submittingUserLastName) {
        this.submittingUserLastName = submittingUserLastName;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getShowSerialNumber() {
        return showSerialNumber;
    }

    public void setShowSerialNumber(String showSerialNumber) {
        this.showSerialNumber = showSerialNumber;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public String getInitialHealthCareOrgId() {
        return initialHealthCareOrgId;
    }

    public void setInitialHealthCareOrgId(String initialHealthCareOrgId) {
        this.initialHealthCareOrgId = initialHealthCareOrgId;
    }

    public String getInitiatingClinicId() {
        return initiatingClinicId;
    }

    public void setInitiatingClinicId(String initiatingClinicId) {
        this.initiatingClinicId = initiatingClinicId;
    }

    public String getAnswerDisplayOrientation() {
        return answerDisplayOrientation;
    }

    public void setAnswerDisplayOrientation(String answerDisplayOrientation) {
        this.answerDisplayOrientation = answerDisplayOrientation;
    }

    public int getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(int providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getPatientSurveyResponseId() {
        return patientSurveyResponseId;
    }

    public void setPatientSurveyResponseId(String patientSurveyResponseId) {
        this.patientSurveyResponseId = patientSurveyResponseId;
    }

    public String getPatientSurveyResponseStatus() {
        return patientSurveyResponseStatus;
    }

    public void setPatientSurveyResponseStatus(String patientSurveyResponseStatus) {
        this.patientSurveyResponseStatus = patientSurveyResponseStatus;
    }

    public String getSubmissionDateAndTime() {
        return submissionDateAndTime;
    }

    public void setSubmissionDateAndTime(String submissionDateAndTime) {
        this.submissionDateAndTime = submissionDateAndTime;
    }

    public int getMaxTotalSurveyScore() {
        return maxTotalSurveyScore;
    }

    public void setMaxTotalSurveyScore(int maxTotalSurveyScore) {
        this.maxTotalSurveyScore = maxTotalSurveyScore;
    }

    public String getMaxTotalSurveyScoreUnits() {
        return maxTotalSurveyScoreUnits;
    }

    public void setMaxTotalSurveyScoreUnits(String maxTotalSurveyScoreUnits) {
        this.maxTotalSurveyScoreUnits = maxTotalSurveyScoreUnits;
    }

    public String getTotalSurveyScore() {
        return totalSurveyScore;
    }

    public void setTotalSurveyScore(String totalSurveyScore) {
        this.totalSurveyScore = totalSurveyScore;
    }

    public String getTotalSurveyScoreUnits() {
        return totalSurveyScoreUnits;
    }

    public void setTotalSurveyScoreUnits(String totalSurveyScoreUnits) {
        this.totalSurveyScoreUnits = totalSurveyScoreUnits;
    }

    public String getPercentTotalSurveyScore() {
        return percentTotalSurveyScore;
    }

    public void setPercentTotalSurveyScore(String percentTotalSurveyScore) {
        this.percentTotalSurveyScore = percentTotalSurveyScore;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public List<SurveySection> getSurveySection() {
        return surveySection;
    }

    public void setSurveySection(List<SurveySection> surveySection) {
        this.surveySection = surveySection;
    }

    @Override
    public String toString() {
        return "SurveyDetails{" +
                "patientId='" + patientId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userId='" + userId + '\'' +
                ", surveyTemplateName='" + surveyTemplateName + '\'' +
                ", submittingUserFirstName='" + submittingUserFirstName + '\'' +
                ", submittingUserLastName='" + submittingUserLastName + '\'' +
                ", surveyId=" + surveyId +
                ", surveyName='" + surveyName + '\'' +
                ", showSerialNumber='" + showSerialNumber + '\'' +
                ", surveyDescription='" + surveyDescription + '\'' +
                ", initialHealthCareOrgId='" + initialHealthCareOrgId + '\'' +
                ", initiatingClinicId='" + initiatingClinicId + '\'' +
                ", answerDisplayOrientation='" + answerDisplayOrientation + '\'' +
                ", providerUserId=" + providerUserId +
                ", patientSurveyResponseId='" + patientSurveyResponseId + '\'' +
                ", patientSurveyResponseStatus='" + patientSurveyResponseStatus + '\'' +
                ", submissionDateAndTime='" + submissionDateAndTime + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", maxTotalSurveyScore=" + maxTotalSurveyScore +
                ", maxTotalSurveyScoreUnits='" + maxTotalSurveyScoreUnits + '\'' +
                ", totalSurveyScore='" + totalSurveyScore + '\'' +
                ", totalSurveyScoreUnits='" + totalSurveyScoreUnits + '\'' +
                ", percentTotalSurveyScore='" + percentTotalSurveyScore + '\'' +
                ", surveySection=" + surveySection +
                '}';
    }
}
