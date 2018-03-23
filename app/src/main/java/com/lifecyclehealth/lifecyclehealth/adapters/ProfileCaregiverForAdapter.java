package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.ProfileCaregiverForResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by vaibhavi on 06-02-2018.
 */

public class ProfileCaregiverForAdapter extends RecyclerView.Adapter<ProfileCaregiverForAdapter.MyViewHolder> {

    private List<ProfileCaregiverForResponse.PatientList> meetResponseList;
    private ArrayList<ProfileCaregiverForResponse.PatientList> meetResponseArrayList;

    private ProfileCaregiverAdapter.OnItemClickListener listener;
    public static ArrayList selectedParticipant = new ArrayList<>();
    Context context;

    @Override
    public ProfileCaregiverForAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_caregiver_adapter, parent, false);
        return new ProfileCaregiverForAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(ProfileCaregiverForResponse.PatientList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final ProfileCaregiverForAdapter.MyViewHolder holder, int position) {

        final ProfileCaregiverForResponse.PatientList meetList = meetResponseList.get(position);
        selectedParticipant = new ArrayList<>();
        holder.text1.setText(meetList.getPatient_FullName());
        // holder.text2.setVisibility(View.GONE);
        holder.text2.setText(meetList.getStatus());
        if (meetList.isAccepted_Status()) {
            holder.text2.setTextColor(context.getResources().getColor(R.color.false_text));
            holder.text2.setBackgroundColor(context.getResources().getColor(R.color.false_background));
        } else {
            holder.text2.setTextColor(context.getResources().getColor(R.color.true_text));
            holder.text2.setBackgroundColor(context.getResources().getColor(R.color.true_background));
        }

        if (meetList.getStatus().equals("Pending")) {
            holder.text2.setVisibility(View.VISIBLE);
            holder.text2.setText("Invited, awaiting confirmation");
        }

        holder.itemView.setTag(meetList);
        holder.bind(meetList, listener, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return meetResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView text1, text2;

        public MyViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
        }

        public void bind(final ProfileCaregiverForResponse.PatientList item, final ProfileCaregiverAdapter.OnItemClickListener listener, final String pos) {

        }
    }

    public ProfileCaregiverForAdapter(List<ProfileCaregiverForResponse.PatientList> responseList, Context context, ProfileCaregiverAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        this.context = context;
        this.listener = listener;
        meetResponseArrayList=new ArrayList<>();
        meetResponseArrayList.addAll(responseList);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        meetResponseList.clear();
        if (charText.length() == 0) {
            meetResponseList.addAll(meetResponseArrayList);
        } else {
            for (ProfileCaregiverForResponse.PatientList wp : meetResponseArrayList) {
                if (wp.getPatient_FullName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    meetResponseList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
