package com.harrier.lifecyclehealth.model;

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
}
