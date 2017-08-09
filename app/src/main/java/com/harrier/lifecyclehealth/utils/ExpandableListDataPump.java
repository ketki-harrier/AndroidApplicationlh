package com.harrier.lifecyclehealth.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_COMPLETED;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_SCHEDULE;
import static com.harrier.lifecyclehealth.utils.AppConstants.CONST_SURV_LIST_TODO;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> toDoList = new ArrayList<String>();
        toDoList.add("Pre-op Screen");
        toDoList.add("Hosp Discharge");
        toDoList.add("Home Health Weekly");

        List<String> scheduledList = new ArrayList<String>();
        scheduledList.add("R-ShSurgery - Patient");
        scheduledList.add("R-NonRep8-sh repairpath");
        scheduledList.add("R-ShSurgery-SOS");

        List<String> completedList = new ArrayList<String>();
        completedList.add("Home health discharge");
        completedList.add("Discharge from hospital");

        expandableListDetail.put(CONST_SURV_LIST_TODO, toDoList);
        expandableListDetail.put(CONST_SURV_LIST_SCHEDULE, scheduledList);
        expandableListDetail.put(CONST_SURV_LIST_COMPLETED, completedList);
        return expandableListDetail;
    }
}
