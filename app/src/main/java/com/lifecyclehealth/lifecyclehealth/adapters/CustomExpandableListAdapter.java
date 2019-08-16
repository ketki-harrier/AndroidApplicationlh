package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.ColorCode;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;
import com.lifecyclehealth.lifecyclehealth.utils.AppConstants;

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
        PatientSurveyItem patientSurveyItem = null;
        patientSurveyItem = (PatientSurveyItem) getChild(listPosition, expandedListPosition);


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
                expandedListTextViewDate.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
                expandedListTextViewDate.setVisibility(View.GONE);
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
        ImageView expandedListGroupArrow = (ImageView) convertView
                .findViewById(R.id.expandedListGroupArrow);



      //  try {
            String resposne = MyApplication.getInstance().getColorCodeJson(AppConstants.SET_COLOR_CODE);
            ColorCode colorCode = new Gson().fromJson(resposne, ColorCode.class);
            String demo = colorCode.getVisualBrandingPreferences().getColorPreference();
            String Stringcode = "";
            String hashcode = "";

            if(demo == null){
                hashcode = "Green";
                Stringcode = "259b24";
            }
            else if(demo !=null) {
                String[] arr = colorCode.getVisualBrandingPreferences().getColorPreference().split("#");
                hashcode = arr[0].trim();
                Stringcode = arr[1].trim();
          /*  }
           else */
                if (hashcode.equals("Black") && Stringcode.length() < 6) {
                    Stringcode = "333333";
                }
            }
            listTitleTextView.setTextColor(Color.parseColor("#"+Stringcode));
            if (isExpanded){
                expandedListGroupArrow.setImageResource(R.drawable.green_arrow_down);
            }else {
                expandedListGroupArrow.setImageResource(R.drawable.green_arrownext);
            }

            expandedListGroupArrow.setColorFilter(Color.parseColor("#"+Stringcode));
       // }catch (Exception e){e.printStackTrace();}

//        listTitleTextView.setTypeface(null, Typeface.BOLD);
        if (listTitle.length() > 0)
            listTitleTextView.setText(listTitle);
        else
            listTitleTextView.setText("");
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