package com.harrier.lifecyclehealth.model;

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
}
