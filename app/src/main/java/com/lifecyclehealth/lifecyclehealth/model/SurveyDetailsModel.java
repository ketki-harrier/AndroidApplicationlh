package com.lifecyclehealth.lifecyclehealth.model;

/**
 *
 * this is custom list made for view pager logic as answer type is inside sub-list and need to sum main list during view creation
 * this is combination of SURVEY STATUS MODEL and QUESTION MODEL;
 */

public class SurveyDetailsModel {
    private QuestionModel questionModel;
    private int patientSurveySectionId;
    private String name;
    private String description;
    private int typeOfSection;

    public String getPatient_Survey_ResponseID() {
        return Patient_Survey_ResponseID;
    }

    public void setPatient_Survey_ResponseID(String patient_Survey_ResponseID) {
        Patient_Survey_ResponseID = patient_Survey_ResponseID;
    }

    private String Patient_Survey_ResponseID;


    public QuestionModel getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(QuestionModel questionModel) {
        this.questionModel = questionModel;
    }

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

    @Override
    public String toString() {
        return "SurveyDetailsModel{" +
                "questionModel=" + questionModel +
                ", patientSurveySectionId=" + patientSurveySectionId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", typeOfSection=" + typeOfSection +
                '}';
    }

    public int getPagesQuantity() {
        return pagesQuantity;
    }

    public void setPagesQuantity(int pagesQuantity) {
        this.pagesQuantity = pagesQuantity;
    }

    private int pagesQuantity;

    public String getSurveyID() {
        return SurveyID;
    }

    public void setSurveyID(String surveyID) {
        SurveyID = surveyID;
    }

    public String SurveyID;

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

    private  String FirstName;
    private  String LastName;
    private  String Patient_FirstName;

    public String getPatient_FirstName() {
        return Patient_FirstName;
    }

    public void setPatient_FirstName(String patient_FirstName) {
        Patient_FirstName = patient_FirstName;
    }

    private  String Submitting_User_LastName;
    private  String Patient_Total_Survey_Score;



    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    private int pagePosition;

}
