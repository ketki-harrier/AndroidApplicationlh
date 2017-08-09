package com.harrier.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 01/05/2017.
 */

public class PatientSurveyItem {
    @SerializedName("Patient_Survey_ResponseID")
    private int patientSurveyResponseId;
    @SerializedName("Patient_SurveyID")
    private int patientSurveyId;
    @SerializedName("Patient_Treatment_PlanID")
    private String patientTreatmentPlanId;
    @SerializedName("ClinicID")
    private String clinicId;
    @SerializedName("Patient_SurveyID_1")
    private int patientSurveyId1;
    @SerializedName("Name")
    private String name;
    @SerializedName("Description")
    private String description;
    @SerializedName("PhysicianID")
    private String physicianId;
    @SerializedName("HealthCare_OrgID")
    private int healthCareOrgId;
    @SerializedName("Survey_TypeID")
    private String surveyTypeId;
    @SerializedName("Scheduled_Date")
    private String scheduleDate;
    @SerializedName("Status")
    private String status;

    public int getPatientSurveyResponseId() {
        return patientSurveyResponseId;
    }

    public void setPatientSurveyResponseId(int patientSurveyResponseId) {
        this.patientSurveyResponseId = patientSurveyResponseId;
    }

    public int getPatientSurveyId() {
        return patientSurveyId;
    }

    public void setPatientSurveyId(int patientSurveyId) {
        this.patientSurveyId = patientSurveyId;
    }

    public String getPatientTreatmentPlanId() {
        return patientTreatmentPlanId;
    }

    public void setPatientTreatmentPlanId(String patientTreatmentPlanId) {
        this.patientTreatmentPlanId = patientTreatmentPlanId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public int getPatientSurveyId1() {
        return patientSurveyId1;
    }

    public void setPatientSurveyId1(int patientSurveyId1) {
        this.patientSurveyId1 = patientSurveyId1;
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

    public String getPhysicianId() {
        return physicianId;
    }

    public void setPhysicianId(String physicianId) {
        this.physicianId = physicianId;
    }

    public int getHealthCareOrgId() {
        return healthCareOrgId;
    }

    public void setHealthCareOrgId(int healthCareOrgId) {
        this.healthCareOrgId = healthCareOrgId;
    }

    public String getSurveyTypeId() {
        return surveyTypeId;
    }

    public void setSurveyTypeId(String surveyTypeId) {
        this.surveyTypeId = surveyTypeId;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PatientSurveyItem{" +
                "patientSurveyResponseId='" + patientSurveyResponseId + '\'' +
                ", patientSurveyId='" + patientSurveyId + '\'' +
                ", patientTreatmentPlanId='" + patientTreatmentPlanId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", patientSurveyId1=" + patientSurveyId1 +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", physicianId='" + physicianId + '\'' +
                ", healthCareOrgId='" + healthCareOrgId + '\'' +
                ", surveyTypeId='" + surveyTypeId + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
