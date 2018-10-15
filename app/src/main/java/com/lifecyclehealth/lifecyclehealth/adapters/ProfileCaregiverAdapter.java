package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.ProfileCaregiverResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 20-09-2017.
 */

public class ProfileCaregiverAdapter extends RecyclerView.Adapter<ProfileCaregiverAdapter.MyViewHolder> {

    private List<ProfileCaregiverResponse.CaregiverLists> meetResponseList;

    private ProfileCaregiverAdapter.OnItemClickListener listener;
    public static ArrayList selectedParticipant = new ArrayList<>();
    Context context;

    @Override
    public ProfileCaregiverAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_caregiver_adapter, parent, false);
        return new ProfileCaregiverAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(ProfileCaregiverResponse.CaregiverLists item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final ProfileCaregiverAdapter.MyViewHolder holder, int position) {

        final ProfileCaregiverResponse.CaregiverLists meetList = meetResponseList.get(position);
        selectedParticipant = new ArrayList<>();
        holder.text1.setText(meetList.getUser_FullName());
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

        public void bind(final ProfileCaregiverResponse.CaregiverLists item, final ProfileCaregiverAdapter.OnItemClickListener listener, final String pos) {

        }
    }

    public ProfileCaregiverAdapter(List<ProfileCaregiverResponse.CaregiverLists> responseList, Context context, ProfileCaregiverAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        this.context = context;
        this.listener = listener;
    }

}
