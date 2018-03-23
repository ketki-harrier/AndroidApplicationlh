package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 24-11-2017.
 */

public class ScheduleMeetDetailsDto {

    public MeetDetails meetDetails;

    public MeetDetails getMeetDetails() {
        return meetDetails;
    }

    public void setMeetDetails(MeetDetails meetDetails) {
        this.meetDetails = meetDetails;
    }

    @Override
    public String toString() {
        return "ScheduleMeetDetailsDto{" +
                "meetDetails=" + meetDetails +
                '}';
    }

    public class MeetDetails{

        private String Name;
        private String Agenda;
        private String StartDate;
        private String StartTime;
        private String DurationInMinute;
        private String Episode_Care_Plan_Name;
        private String Episode_Care_PlanID;

        public ArrayList<String> getUserIDs() {
            return UserIDs;
        }

        public void setUserIDs(ArrayList<String> userIDs) {
            UserIDs = userIDs;
        }

        private ArrayList<String> UserIDs;
        private String OffSet;
        private String Meeting_Type;
        private String Patient_UserID;
        private String PatientID;


        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getAgenda() {
            return Agenda;
        }

        public void setAgenda(String agenda) {
            Agenda = agenda;
        }

        public String getStartDate() {
            return StartDate;
        }

        public void setStartDate(String startDate) {
            StartDate = startDate;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }

        public String getDurationInMinute() {
            return DurationInMinute;
        }

        public void setDurationInMinute(String durationInMinute) {
            DurationInMinute = durationInMinute;
        }

        public String getEpisode_Care_Plan_Name() {
            return Episode_Care_Plan_Name;
        }

        public void setEpisode_Care_Plan_Name(String episode_Care_Plan_Name) {
            Episode_Care_Plan_Name = episode_Care_Plan_Name;
        }

        public String getEpisode_Care_PlanID() {
            return Episode_Care_PlanID;
        }

        public void setEpisode_Care_PlanID(String episode_Care_PlanID) {
            Episode_Care_PlanID = episode_Care_PlanID;
        }



        public String getOffSet() {
            return OffSet;
        }

        public void setOffSet(String offSet) {
            OffSet = offSet;
        }

        public String getMeeting_Type() {
            return Meeting_Type;
        }

        public void setMeeting_Type(String meeting_Type) {
            Meeting_Type = meeting_Type;
        }

        public String getPatient_UserID() {
            return Patient_UserID;
        }

        public void setPatient_UserID(String patient_UserID) {
            Patient_UserID = patient_UserID;
        }

        public String getPatientID() {
            return PatientID;
        }

        public void setPatientID(String patientID) {
            PatientID = patientID;
        }

    }


}
