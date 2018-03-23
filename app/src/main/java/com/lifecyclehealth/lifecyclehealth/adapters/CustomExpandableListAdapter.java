package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;

import java.util.LinkedHashMap;
import java.util.List;


public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    //private HashMap<String, List<String>> expandableListDetail;
    private LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       LinkedHashMap<String, List<PatientSurveyItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        if (expandableListDetail == null) {
            this.expandableListDetail = new LinkedHashMap<>();
        } else {
            this.expandableListDetail = expandableListDetail;
        }
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        final PatientSurveyItem patientSurveyItem = (PatientSurveyItem) getChild(listPosition, expandedListPosition);


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        if (patientSurveyItem != null) {
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItemTitle);
            expandedListTextView.setText(patientSurveyItem.getName());
            TextView expandedListTextViewDate = (TextView) convertView
                    .findViewById(R.id.expandedListItemDate);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.expandedListItemArrow);
            LinearLayout rootLayout = (LinearLayout) convertView.findViewById(R.id.rootLayout);
            if (!patientSurveyItem.getScheduleDate().equals("")) {
                expandedListTextViewDate.setText("[" + patientSurveyItem.getScheduleDate() + "]");
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            TextView expandedListTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItemTitle);
            expandedListTextView.setText("No item to display");
            TextView expandedListTextViewDate = (TextView) convertView
                    .findViewById(R.id.expandedListItemDate);
            expandedListTextViewDate.setText("");

            ImageView imageView = (ImageView) convertView.findViewById(R.id.expandedListItemArrow);
            LinearLayout rootLayout = (LinearLayout) convertView.findViewById(R.id.rootLayout);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        List<PatientSurveyItem> childList = this.expandableListDetail.get(this.expandableListTitle.get(listPosition));
        if (childList != null && !childList.isEmpty()) {
            return childList.size();
        }
        return 0;
       /* return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();*/
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
//        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}