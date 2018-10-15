package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaibhavi on 24-01-2018.
 */

public class SurveyReportResponse {

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

    public class SurveyQuestion {
        @SerializedName("survey_details")
        private SurveyDetails surveyDetails;

        public SurveyDetails getSurveyDetails() {
            return surveyDetails;
        }

        public void setSurveyDetails(SurveyDetails surveyDetails) {
            this.surveyDetails = surveyDetails;
        }
    }

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

    }


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

    }


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


        private int seekProgress;

        public int getSeekProgress() {
            return seekProgress;
        }

        public void setSeekProgress(int seekProgress) {
            this.seekProgress = seekProgress;
        }
    }

}
