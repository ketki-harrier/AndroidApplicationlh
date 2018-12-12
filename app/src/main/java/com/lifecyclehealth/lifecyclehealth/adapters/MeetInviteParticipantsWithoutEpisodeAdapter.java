package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.fragments.ScheduleMeet;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsWithoutEpisodeModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_CHECK_IS_DESIGNATE_SELECT;

/**
 * Created by vaibhavi on 24-10-2017.
 */

public class MeetInviteParticipantsWithoutEpisodeAdapter extends RecyclerView.Adapter<MeetInviteParticipantsWithoutEpisodeAdapter.MyViewHolder> {

    private List<MeetInviteParticipantsWithoutEpisodeModel.UserList> meetResponseList;
    private ArrayList<String> selectedUsers = new ArrayList<>();

    private MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener listener;
    public static ArrayList selectedParticipantWithout = new ArrayList<>();
    Context context;

    @Override
    public MeetInviteParticipantsWithoutEpisodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_invite_participant_inflater, parent, false);
        return new MeetInviteParticipantsWithoutEpisodeAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(MeetInviteParticipantsWithoutEpisodeModel.UserList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MeetInviteParticipantsWithoutEpisodeAdapter.MyViewHolder holder, int position) {

        final MeetInviteParticipantsWithoutEpisodeModel.UserList meetList = meetResponseList.get(position);
        holder.addParticipant.setVisibility(View.GONE);
        holder.name.setText(meetList.getFullName());
        //holder.provider.setText(meetList.getRoleName().toString());
        String role = TextUtils.join(",", meetList.getRoleName());
        holder.provider.setText(role);


        if (selectedUsers != null) {
            if (selectedUsers.size() > 0) {
                if (selectedUsers.contains(meetList.getUserID())) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipantWithout.add(meetList.getUserID());
                }
            } else {
                if (meetResponseList.size() == 1) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipantWithout.add(meetList.getUserID());
                }
                if (meetList.isLoggedInUser()) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipantWithout.add(meetList.getUserID());
                } else if (role.contains("Patient") && !ScheduleMeet.comingFromMultiplePatient) {
                    if (meetList.isPatientSelected()) {
                    } else {
                        holder.addParticipant.setVisibility(View.VISIBLE);
                        selectedParticipantWithout.add(meetList.getUserID());
                    }
                }
                if (meetList.isChecked()) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipantWithout.add(meetList.getUserID());
                }

            }
        } else {
            if (meetResponseList.size() == 1) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipantWithout.add(meetList.getUserID());
            }
            if (meetList.isLoggedInUser()) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipantWithout.add(meetList.getUserID());
            } else if (role.contains("Patient")) {
                if (meetList.isPatientSelected()) {
                } else {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipantWithout.add(meetList.getUserID());
                }
            }
            if (meetList.isChecked()) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipantWithout.add(meetList.getUserID());
            }
        }

        if (meetList.isDesignate_Exist() && meetList.getDesignate_FullName() != null) {
            holder.designate_image.setVisibility(View.VISIBLE);
            holder.designate.setVisibility(View.VISIBLE);
            holder.designate.setText(meetList.getDesignate_FullName() + " is Designate");
        } else {
            holder.designate_image.setVisibility(View.GONE);
            holder.designate.setVisibility(View.GONE);
        }

        holder.itemView.setTag(meetList);
        holder.bind(meetList, listener, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return meetResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, provider, designate;
        ImageView addParticipant, designate_image;
        CircularImageView imageView;
        LinearLayout linear;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            provider = (TextView) itemView.findViewById(R.id.provider);
            designate = (TextView) itemView.findViewById(R.id.designate);
            addParticipant = (ImageView) itemView.findViewById(R.id.addParticipant);
            designate_image = (ImageView) itemView.findViewById(R.id.designate_image);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            imageView = (CircularImageView) itemView.findViewById(R.id.imageView2);
        }

        public void bind(final MeetInviteParticipantsWithoutEpisodeModel.UserList item, final MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener listener, final String pos) {

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });
        }
    }

    public MeetInviteParticipantsWithoutEpisodeAdapter(List<MeetInviteParticipantsWithoutEpisodeModel.UserList> responseList, Context context, ArrayList<String> selectedUsers, MeetInviteParticipantsWithoutEpisodeAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        selectedParticipantWithout = new ArrayList();
        selectedParticipantWithout.clear();
        this.selectedUsers = selectedUsers;
        this.context = context;
        this.listener = listener;
    }


}
