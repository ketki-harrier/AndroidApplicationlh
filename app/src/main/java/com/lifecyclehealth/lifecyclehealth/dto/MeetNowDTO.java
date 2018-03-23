package com.lifecyclehealth.lifecyclehealth.dto;

import java.util.ArrayList;

/**
 * Created by vaibhavi on 27-10-2017.
 */

public class MeetNowDTO {

    private MeetDetails meetDetails;

    public MeetDetails getMeetDetails() {
        return meetDetails;
    }

    public void setMeetDetails(MeetDetails meetDetails) {
        this.meetDetails = meetDetails;
    }

    @Override
    public String toString() {
        return "MeetNowDTO{" +
                "meetDetails=" + meetDetails +
                '}';
    }

    public class MeetDetails {

        private String binder_id;
        private String Name;
        private String Episode_Care_Plan_Name;
        private String Episode_Care_PlanID;
        private ArrayList UserIDs;
        private String Meeting_Type;
        private String Patient_UserID;
        private String PatientID;
        private String OffSet;

        public String getOffSet() {
            return OffSet;
        }

        public void setOffSet(String offSet) {
            OffSet = offSet;
        }

        public String getAgenda() {
            return Agenda;
        }

        public void setAgenda(String agenda) {
            Agenda = agenda;
        }

        private String Agenda;

        public String getBinder_id() {
            return binder_id;
        }

        public void setBinder_id(String binder_id) {
            this.binder_id = binder_id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
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

        public ArrayList getUserIDs() {
            return UserIDs;
        }

        public void setUserIDs(ArrayList userIDs) {
            UserIDs = userIDs;
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


        @Override
        public String toString() {
            return "MeetDetails{" +
                    "binder_id='" + binder_id + '\'' +
                    ", Name='" + Name + '\'' +
                    ", Episode_Care_Plan_Name='" + Episode_Care_Plan_Name + '\'' +
                    ", Episode_Care_PlanID='" + Episode_Care_PlanID + '\'' +
                    ", UserIDs='" + UserIDs + '\'' +
                    ", Meeting_Type='" + Meeting_Type + '\'' +
                    ", Patient_UserID='" + Patient_UserID + '\'' +
                    ", PatientID='" + PatientID + '\'' +
                    ", Agenda='" + Agenda + '\'' +
                    '}';
        }
    }


}
