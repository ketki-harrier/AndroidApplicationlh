package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaibhavi on 12-08-2017.
 */

public class SurveyElectronicSubmitResponse {

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

    public SubmittedScoreData getSubmittedScore() {
        return SubmittedScore;
    }

    public void setSubmittedScore(SubmittedScoreData submittedScore) {
        SubmittedScore = submittedScore;
    }

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("SubmittedScore")
    public SubmittedScoreData SubmittedScore;

    public class SubmittedScoreData{

        @SerializedName("user")
        public String user;
        @SerializedName("percentage_total_survey_score")
        public String percentage_total_survey_score;
        @SerializedName("maximum_total_survey_score")
        public String maximum_total_survey_score;
        @SerializedName("patient_total_survey_score")
        public String patient_total_survey_score;
        @SerializedName("submission_datetime")
        public String submission_datetime;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPercentage_total_survey_score() {
            return percentage_total_survey_score;
        }

        public void setPercentage_total_survey_score(String percentage_total_survey_score) {
            this.percentage_total_survey_score = percentage_total_survey_score;
        }

        public String getMaximum_total_survey_score() {
            return maximum_total_survey_score;
        }

        public void setMaximum_total_survey_score(String maximum_total_survey_score) {
            this.maximum_total_survey_score = maximum_total_survey_score;
        }

        public String getPatient_total_survey_score() {
            return patient_total_survey_score;
        }

        public void setPatient_total_survey_score(String patient_total_survey_score) {
            this.patient_total_survey_score = patient_total_survey_score;
        }

        public String getSubmission_datetime() {
            return submission_datetime;
        }

        public void setSubmission_datetime(String submission_datetime) {
            this.submission_datetime = submission_datetime;
        }
    }
}
