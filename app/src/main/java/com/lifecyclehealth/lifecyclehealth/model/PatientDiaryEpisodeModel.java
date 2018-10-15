package com.lifecyclehealth.lifecyclehealth.model;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 09-03-2018.
 */

public class PatientDiaryEpisodeModel {

    private String status;
    private String message;
    private ArrayList<EpisodePlanList> episodePlanList;

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

    public ArrayList<EpisodePlanList> getEpisodePlanList() {
        return episodePlanList;
    }

    public void setEpisodePlanList(ArrayList<EpisodePlanList> episodePlanList) {
        this.episodePlanList = episodePlanList;
    }

    public class EpisodePlanList {

        private String PatientID;
        private String FirstName;
        private String LastName;
        private String PatientStatus;
        private String FullName;
        private String HealthCare_OrgID;
        private String Episode_Care_PlanID;
        private String Surgical_ProcedureID;
        private String Episode_Care_Plan_Name;
        private String Surgical_Procedure;
        private String Surgery_Date;
        private String Created_DateTime;
        private String Location;
        private String Scheduled_Surgery_Date;
        private String Estimate_Surgery_Date;
        private String Episode_Status;
        private Current_Episode_Stages Current_Episode_Stage;
        private boolean Patient_Log_Exists;

        private String Episode_Completion_Date;
        private String Episode_Start_Date;
        private ArrayList<Episode_Care_Plan_Stages> Episode_Care_Plan_Stage;
       // private ArrayList<Payment_Detail> Payment_Details;
        private ArrayList<Stage_Payment_Detail> Stage_Payment_Details;


        public Current_Episode_Stages getCurrent_Episode_Stage() {
            return Current_Episode_Stage;
        }

        public void setCurrent_Episode_Stage(Current_Episode_Stages current_Episode_Stage) {
            Current_Episode_Stage = current_Episode_Stage;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

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

        public String getPatientStatus() {
            return PatientStatus;
        }

        public void setPatientStatus(String patientStatus) {
            PatientStatus = patientStatus;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getHealthCare_OrgID() {
            return HealthCare_OrgID;
        }

        public void setHealthCare_OrgID(String healthCare_OrgID) {
            HealthCare_OrgID = healthCare_OrgID;
        }

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }

        public String getSurgical_ProcedureID() {
            return Surgical_ProcedureID;
        }

        public void setSurgical_ProcedureID(String surgical_ProcedureID) {
            Surgical_ProcedureID = surgical_ProcedureID;
        }

        public String getEpisode_Care_Plan_Name() {
            return Episode_Care_Plan_Name;
        }

        public void setEpisode_Care_Plan_Name(String episode_Care_Plan_Name) {
            Episode_Care_Plan_Name = episode_Care_Plan_Name;
        }

        public String getSurgical_Procedure() {
            return Surgical_Procedure;
        }

        public void setSurgical_Procedure(String surgical_Procedure) {
            Surgical_Procedure = surgical_Procedure;
        }

        public String getSurgery_Date() {
            return Surgery_Date;
        }

        public void setSurgery_Date(String surgery_Date) {
            Surgery_Date = surgery_Date;
        }

        public String getCreated_DateTime() {
            return Created_DateTime;
        }

        public void setCreated_DateTime(String created_DateTime) {
            Created_DateTime = created_DateTime;
        }

        public String getLocation() {
            return Location;
        }

        public void setLocation(String location) {
            Location = location;
        }

        public String getScheduled_Surgery_Date() {
            return Scheduled_Surgery_Date;
        }

        public void setScheduled_Surgery_Date(String scheduled_Surgery_Date) {
            Scheduled_Surgery_Date = scheduled_Surgery_Date;
        }

        public String getEstimate_Surgery_Date() {
            return Estimate_Surgery_Date;
        }

        public void setEstimate_Surgery_Date(String estimate_Surgery_Date) {
            Estimate_Surgery_Date = estimate_Surgery_Date;
        }

        public String getEpisode_Status() {
            return Episode_Status;
        }

        public void setEpisode_Status(String episode_Status) {
            Episode_Status = episode_Status;
        }

        public boolean isPatient_Log_Exists() {
            return Patient_Log_Exists;
        }

        public void setPatient_Log_Exists(boolean patient_Log_Exists) {
            Patient_Log_Exists = patient_Log_Exists;
        }

        public String getEpisode_Completion_Date() {
            return Episode_Completion_Date;
        }

        public void setEpisode_Completion_Date(String episode_Completion_Date) {
            Episode_Completion_Date = episode_Completion_Date;
        }

        public String getEpisode_Start_Date() {
            return Episode_Start_Date;
        }

        public void setEpisode_Start_Date(String episode_Start_Date) {
            Episode_Start_Date = episode_Start_Date;
        }

        public ArrayList<Episode_Care_Plan_Stages> getEpisode_Care_Plan_Stage() {
            return Episode_Care_Plan_Stage;
        }

        public void setEpisode_Care_Plan_Stage(ArrayList<Episode_Care_Plan_Stages> episode_Care_Plan_Stage) {
            Episode_Care_Plan_Stage = episode_Care_Plan_Stage;
        }

       /* public ArrayList<Payment_Detail> getPayment_Details() {
            return Payment_Details;
        }

        public void setPayment_Details(ArrayList<Payment_Detail> payment_Details) {
            Payment_Details = payment_Details;
        }*/

        public ArrayList<Stage_Payment_Detail> getStage_Payment_Details() {
            return Stage_Payment_Details;
        }

        public void setStage_Payment_Details(ArrayList<Stage_Payment_Detail> stage_Payment_Details) {
            Stage_Payment_Details = stage_Payment_Details;
        }
    }


    public class Episode_Care_Plan_Stages {

        private String Episode_Care_Plan_Stage_MappingID;
        private String Status;
        private String Admission_Date;
        private String Discharge_Date;
        private String Planned_Admission_Date;
        private String Planned_Discharge_Date;
        private String Provider_Location;
        private String Primary_Provider_Contact_UserID;
        private String Dependent_Stage_Date_Field;
        private String Dependent_Stage_Date_Field_Selected;
        private String Estimate_Duration;
        private String Actual_Duration;
        private String Estimated_Visits;
        private String Actual_Visits;
        private String Adjustment_In_Days;
        private String Dependent_Episode_StageID;
        private String Provider_Clinic;
        private String Provider_Responsible_ClinicID;
        private String Clinic_Type;
        private String Patient_Location_For_service;
        private String Provider_Location_1;
        private String Episode_Care_Plan_StageID;
        private boolean IsRequired;
        private String Name;
        private String Stage_Name_For_DB_Use;
        private String Visit_TypeID;
        private String Facility_TypeID;
        private String Primary_Provider_Contact;
        private String Primary_Provider_Contact_Participant_MappingID;
        private String Primary_Provider_Contact_Type;
        private String Primary_Provider_Contact_UserID_1;
        private String Provider_Type;

        public String getEpisode_Care_Plan_Stage_MappingID() {
            return Episode_Care_Plan_Stage_MappingID;
        }

        public void setEpisode_Care_Plan_Stage_MappingID(String episode_Care_Plan_Stage_MappingID) {
            Episode_Care_Plan_Stage_MappingID = episode_Care_Plan_Stage_MappingID;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getAdmission_Date() {
            return Admission_Date;
        }

        public void setAdmission_Date(String admission_Date) {
            Admission_Date = admission_Date;
        }

        public String getDischarge_Date() {
            return Discharge_Date;
        }

        public void setDischarge_Date(String discharge_Date) {
            Discharge_Date = discharge_Date;
        }

        public String getPlanned_Admission_Date() {
            return Planned_Admission_Date;
        }

        public void setPlanned_Admission_Date(String planned_Admission_Date) {
            Planned_Admission_Date = planned_Admission_Date;
        }

        public String getPlanned_Discharge_Date() {
            return Planned_Discharge_Date;
        }

        public void setPlanned_Discharge_Date(String planned_Discharge_Date) {
            Planned_Discharge_Date = planned_Discharge_Date;
        }

        public String getProvider_Location() {
            return Provider_Location;
        }

        public void setProvider_Location(String provider_Location) {
            Provider_Location = provider_Location;
        }

        public String getPrimary_Provider_Contact_UserID() {
            return Primary_Provider_Contact_UserID;
        }

        public void setPrimary_Provider_Contact_UserID(String primary_Provider_Contact_UserID) {
            Primary_Provider_Contact_UserID = primary_Provider_Contact_UserID;
        }

        public String getDependent_Stage_Date_Field() {
            return Dependent_Stage_Date_Field;
        }

        public void setDependent_Stage_Date_Field(String dependent_Stage_Date_Field) {
            Dependent_Stage_Date_Field = dependent_Stage_Date_Field;
        }

        public String getDependent_Stage_Date_Field_Selected() {
            return Dependent_Stage_Date_Field_Selected;
        }

        public void setDependent_Stage_Date_Field_Selected(String dependent_Stage_Date_Field_Selected) {
            Dependent_Stage_Date_Field_Selected = dependent_Stage_Date_Field_Selected;
        }

        public String getEstimate_Duration() {
            return Estimate_Duration;
        }

        public void setEstimate_Duration(String estimate_Duration) {
            Estimate_Duration = estimate_Duration;
        }

        public String getActual_Duration() {
            return Actual_Duration;
        }

        public void setActual_Duration(String actual_Duration) {
            Actual_Duration = actual_Duration;
        }

        public String getEstimated_Visits() {
            return Estimated_Visits;
        }

        public void setEstimated_Visits(String estimated_Visits) {
            Estimated_Visits = estimated_Visits;
        }

        public String getActual_Visits() {
            return Actual_Visits;
        }

        public void setActual_Visits(String actual_Visits) {
            Actual_Visits = actual_Visits;
        }

        public String getAdjustment_In_Days() {
            return Adjustment_In_Days;
        }

        public void setAdjustment_In_Days(String adjustment_In_Days) {
            Adjustment_In_Days = adjustment_In_Days;
        }

        public String getDependent_Episode_StageID() {
            return Dependent_Episode_StageID;
        }

        public void setDependent_Episode_StageID(String dependent_Episode_StageID) {
            Dependent_Episode_StageID = dependent_Episode_StageID;
        }

        public String getProvider_Clinic() {
            return Provider_Clinic;
        }

        public void setProvider_Clinic(String provider_Clinic) {
            Provider_Clinic = provider_Clinic;
        }

        public String getProvider_Responsible_ClinicID() {
            return Provider_Responsible_ClinicID;
        }

        public void setProvider_Responsible_ClinicID(String provider_Responsible_ClinicID) {
            Provider_Responsible_ClinicID = provider_Responsible_ClinicID;
        }

        public String getClinic_Type() {
            return Clinic_Type;
        }

        public void setClinic_Type(String clinic_Type) {
            Clinic_Type = clinic_Type;
        }

        public String getPatient_Location_For_service() {
            return Patient_Location_For_service;
        }

        public void setPatient_Location_For_service(String patient_Location_For_service) {
            Patient_Location_For_service = patient_Location_For_service;
        }

        public String getProvider_Location_1() {
            return Provider_Location_1;
        }

        public void setProvider_Location_1(String provider_Location_1) {
            Provider_Location_1 = provider_Location_1;
        }

        public String getEpisode_Care_Plan_StageID() {
            return Episode_Care_Plan_StageID;
        }

        public void setEpisode_Care_Plan_StageID(String episode_Care_Plan_StageID) {
            Episode_Care_Plan_StageID = episode_Care_Plan_StageID;
        }

        public boolean isRequired() {
            return IsRequired;
        }

        public void setRequired(boolean required) {
            IsRequired = required;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getStage_Name_For_DB_Use() {
            return Stage_Name_For_DB_Use;
        }

        public void setStage_Name_For_DB_Use(String stage_Name_For_DB_Use) {
            Stage_Name_For_DB_Use = stage_Name_For_DB_Use;
        }

        public String getVisit_TypeID() {
            return Visit_TypeID;
        }

        public void setVisit_TypeID(String visit_TypeID) {
            Visit_TypeID = visit_TypeID;
        }

        public String getFacility_TypeID() {
            return Facility_TypeID;
        }

        public void setFacility_TypeID(String facility_TypeID) {
            Facility_TypeID = facility_TypeID;
        }

        public String getPrimary_Provider_Contact() {
            return Primary_Provider_Contact;
        }

        public void setPrimary_Provider_Contact(String primary_Provider_Contact) {
            Primary_Provider_Contact = primary_Provider_Contact;
        }

        public String getPrimary_Provider_Contact_Participant_MappingID() {
            return Primary_Provider_Contact_Participant_MappingID;
        }

        public void setPrimary_Provider_Contact_Participant_MappingID(String primary_Provider_Contact_Participant_MappingID) {
            Primary_Provider_Contact_Participant_MappingID = primary_Provider_Contact_Participant_MappingID;
        }

        public String getPrimary_Provider_Contact_Type() {
            return Primary_Provider_Contact_Type;
        }

        public void setPrimary_Provider_Contact_Type(String primary_Provider_Contact_Type) {
            Primary_Provider_Contact_Type = primary_Provider_Contact_Type;
        }

        public String getPrimary_Provider_Contact_UserID_1() {
            return Primary_Provider_Contact_UserID_1;
        }

        public void setPrimary_Provider_Contact_UserID_1(String primary_Provider_Contact_UserID_1) {
            Primary_Provider_Contact_UserID_1 = primary_Provider_Contact_UserID_1;
        }

        public String getProvider_Type() {
            return Provider_Type;
        }

        public void setProvider_Type(String provider_Type) {
            Provider_Type = provider_Type;
        }
    }


    public class Payment_Detail {
        private String Cost_Unit;
        private String Planned_Cost;
        private String Actual_Cost;
        private String Estimated_Cost;

        public String getCost_Unit() {
            return Cost_Unit;
        }

        public void setCost_Unit(String cost_Unit) {
            Cost_Unit = cost_Unit;
        }

        public String getPlanned_Cost() {
            return Planned_Cost;
        }

        public void setPlanned_Cost(String planned_Cost) {
            Planned_Cost = planned_Cost;
        }

        public String getActual_Cost() {
            return Actual_Cost;
        }

        public void setActual_Cost(String actual_Cost) {
            Actual_Cost = actual_Cost;
        }

        public String getEstimated_Cost() {
            return Estimated_Cost;
        }

        public void setEstimated_Cost(String estimated_Cost) {
            Estimated_Cost = estimated_Cost;
        }
    }

    public class Stage_Payment_Detail {
        private String Cost_Unit;
        private String Episode_Care_Plan_Stage_MappingID;
        private String Name;
        private String Order_Admission_Date;
        private String Order_Discharge_Date;
        private boolean Show_Bracket;
        private String Planned_Cost;
        private String Actual_Cost;
        private String Estimated_Cost;

        public String getCost_Unit() {
            return Cost_Unit;
        }

        public void setCost_Unit(String cost_Unit) {
            Cost_Unit = cost_Unit;
        }

        public String getEpisode_Care_Plan_Stage_MappingID() {
            return Episode_Care_Plan_Stage_MappingID;
        }

        public void setEpisode_Care_Plan_Stage_MappingID(String episode_Care_Plan_Stage_MappingID) {
            Episode_Care_Plan_Stage_MappingID = episode_Care_Plan_Stage_MappingID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getOrder_Admission_Date() {
            return Order_Admission_Date;
        }

        public void setOrder_Admission_Date(String order_Admission_Date) {
            Order_Admission_Date = order_Admission_Date;
        }

        public String getOrder_Discharge_Date() {
            return Order_Discharge_Date;
        }

        public void setOrder_Discharge_Date(String order_Discharge_Date) {
            Order_Discharge_Date = order_Discharge_Date;
        }

        public boolean isShow_Bracket() {
            return Show_Bracket;
        }

        public void setShow_Bracket(boolean show_Bracket) {
            Show_Bracket = show_Bracket;
        }

        public String getPlanned_Cost() {
            return Planned_Cost;
        }

        public void setPlanned_Cost(String planned_Cost) {
            Planned_Cost = planned_Cost;
        }

        public String getActual_Cost() {
            return Actual_Cost;
        }

        public void setActual_Cost(String actual_Cost) {
            Actual_Cost = actual_Cost;
        }

        public String getEstimated_Cost() {
            return Estimated_Cost;
        }

        public void setEstimated_Cost(String estimated_Cost) {
            Estimated_Cost = estimated_Cost;
        }
    }

    public class Current_Episode_Stages{

        private String Episode_Care_Plan_Stage_MappingID;
        private String Admission_Date;
        private String Status;
        private String Episode_Care_Plan_StageID;
        private String Name;
        private String Stage_Name_For_DB_Use;
        private String Visit_TypeID;
        private String Facility_TypeID;
        private String Provider_Clinic;
        private String Provider_Responsible_ClinicID;
        private String Clinic_Type;
        private String Patient_Location_For_service;
        private String Provider_Location;
        private String Primary_Provider_Contact_Participant_MappingID;
        private String Primary_Provider_Contact_Type;
        private String Primary_Provider_Contact_UserID;
        private String Provider_Type;
        private String Stage_Days;

        public String getEpisode_Care_Plan_Stage_MappingID() {
            return Episode_Care_Plan_Stage_MappingID;
        }

        public void setEpisode_Care_Plan_Stage_MappingID(String episode_Care_Plan_Stage_MappingID) {
            Episode_Care_Plan_Stage_MappingID = episode_Care_Plan_Stage_MappingID;
        }

        public String getAdmission_Date() {
            return Admission_Date;
        }

        public void setAdmission_Date(String admission_Date) {
            Admission_Date = admission_Date;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getEpisode_Care_Plan_StageID() {
            return Episode_Care_Plan_StageID;
        }

        public void setEpisode_Care_Plan_StageID(String episode_Care_Plan_StageID) {
            Episode_Care_Plan_StageID = episode_Care_Plan_StageID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getStage_Name_For_DB_Use() {
            return Stage_Name_For_DB_Use;
        }

        public void setStage_Name_For_DB_Use(String stage_Name_For_DB_Use) {
            Stage_Name_For_DB_Use = stage_Name_For_DB_Use;
        }

        public String getVisit_TypeID() {
            return Visit_TypeID;
        }

        public void setVisit_TypeID(String visit_TypeID) {
            Visit_TypeID = visit_TypeID;
        }

        public String getFacility_TypeID() {
            return Facility_TypeID;
        }

        public void setFacility_TypeID(String facility_TypeID) {
            Facility_TypeID = facility_TypeID;
        }

        public String getProvider_Clinic() {
            return Provider_Clinic;
        }

        public void setProvider_Clinic(String provider_Clinic) {
            Provider_Clinic = provider_Clinic;
        }

        public String getProvider_Responsible_ClinicID() {
            return Provider_Responsible_ClinicID;
        }

        public void setProvider_Responsible_ClinicID(String provider_Responsible_ClinicID) {
            Provider_Responsible_ClinicID = provider_Responsible_ClinicID;
        }

        public String getClinic_Type() {
            return Clinic_Type;
        }

        public void setClinic_Type(String clinic_Type) {
            Clinic_Type = clinic_Type;
        }

        public String getPatient_Location_For_service() {
            return Patient_Location_For_service;
        }

        public void setPatient_Location_For_service(String patient_Location_For_service) {
            Patient_Location_For_service = patient_Location_For_service;
        }

        public String getProvider_Location() {
            return Provider_Location;
        }

        public void setProvider_Location(String provider_Location) {
            Provider_Location = provider_Location;
        }

        public String getPrimary_Provider_Contact_Participant_MappingID() {
            return Primary_Provider_Contact_Participant_MappingID;
        }

        public void setPrimary_Provider_Contact_Participant_MappingID(String primary_Provider_Contact_Participant_MappingID) {
            Primary_Provider_Contact_Participant_MappingID = primary_Provider_Contact_Participant_MappingID;
        }

        public String getPrimary_Provider_Contact_Type() {
            return Primary_Provider_Contact_Type;
        }

        public void setPrimary_Provider_Contact_Type(String primary_Provider_Contact_Type) {
            Primary_Provider_Contact_Type = primary_Provider_Contact_Type;
        }

        public String getPrimary_Provider_Contact_UserID() {
            return Primary_Provider_Contact_UserID;
        }

        public void setPrimary_Provider_Contact_UserID(String primary_Provider_Contact_UserID) {
            Primary_Provider_Contact_UserID = primary_Provider_Contact_UserID;
        }

        public String getProvider_Type() {
            return Provider_Type;
        }

        public void setProvider_Type(String provider_Type) {
            Provider_Type = provider_Type;
        }

        public String getStage_Days() {
            return Stage_Days;
        }

        public void setStage_Days(String stage_Days) {
            Stage_Days = stage_Days;
        }
    }

}
