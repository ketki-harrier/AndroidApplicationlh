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

import com.android.volley.VolleyError;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 23-08-2017.
 */

public class MeetInviteParticipantsAdapter extends RecyclerView.Adapter<MeetInviteParticipantsAdapter.MyViewHolder> {

    private List<MeetInviteParticipantsModel.EpisodeParticipantList> meetResponseList;

    private MeetInviteParticipantsAdapter.OnItemClickListener listener;
    public static ArrayList selectedParticipant = new ArrayList<>();
    Context context;
    ArrayList<String> UserIDs = new ArrayList<>();

    @Override
    public MeetInviteParticipantsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_invite_participant_inflater, parent, false);
        return new MeetInviteParticipantsAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MeetInviteParticipantsAdapter.MyViewHolder holder, int position) {

        final MeetInviteParticipantsModel.EpisodeParticipantList meetList = meetResponseList.get(position);
        holder.addParticipant.setVisibility(View.GONE);
        holder.name.setText(meetList.getFullName());
        // holder.provider.setText(meetList.getRoleName().toString());
        String role = TextUtils.join(",", meetList.getRoleName());
        holder.provider.setText(role);

        if (UserIDs != null) {
            if (UserIDs.size() > 0) {
                if (UserIDs.contains(meetList.getUserID())) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipant.add(meetList.getUserID());

                }
            } else {
                if (meetResponseList.size() == 1) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipant.add(meetList.getUserID());
                }

                if (meetList.isLoggedInUser()) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipant.add(meetList.getUserID());
                } else if (role.contains("Patient")) {
                    if (meetList.isPatientSelected()) {
                    } else {
                        holder.addParticipant.setVisibility(View.VISIBLE);
                        selectedParticipant.add(meetList.getUserID());
                    }
                }
                if (meetList.isChecked()) {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipant.add(meetList.getUserID());
                }
                if (meetList.isDesignate_Exist() && meetList.getDesignate_FullName() != null) {
                    holder.designate_image.setVisibility(View.VISIBLE);
                    holder.designate.setVisibility(View.VISIBLE);
                    holder.designate.setText(meetList.getDesignate_FullName() + " is Designate");
                }
            }
        } else {
            if (meetResponseList.size() == 1) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipant.add(meetList.getUserID());
            }

            if (meetList.isLoggedInUser()) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipant.add(meetList.getUserID());
            } else if (role.contains("Patient")) {
                if (meetList.isPatientSelected()) {
                } else {
                    holder.addParticipant.setVisibility(View.VISIBLE);
                    selectedParticipant.add(meetList.getUserID());
                }
            }
            if (meetList.isChecked()) {
                holder.addParticipant.setVisibility(View.VISIBLE);
                selectedParticipant.add(meetList.getUserID());
            }
        }

        if (meetList.isDesignate_Exist() && meetList.getDesignate_FullName() != null) {
            holder.designate_image.setVisibility(View.VISIBLE);
            holder.designate.setVisibility(View.VISIBLE);
            holder.designate.setText(meetList.getDesignate_FullName() + " is Designate");
        }



        if (meetList.getProfile_Pic_Path() != null) {
            com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

            imageLoader.get(meetList.getProfile_Pic_Path(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Log", "Image Load Error: " + error.getMessage());
                }

                @Override
                public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        holder.imageView.setImageBitmap(response.getBitmap());
                    }
                }
            });
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
            addParticipant = (ImageView) itemView.findViewById(R.id.addParticipant);
            designate = (TextView) itemView.findViewById(R.id.designate);
            designate_image = (ImageView) itemView.findViewById(R.id.designate_image);
            imageView = (CircularImageView) itemView.findViewById(R.id.imageView2);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
        }

        public void bind(final MeetInviteParticipantsModel.EpisodeParticipantList item, final MeetInviteParticipantsAdapter.OnItemClickListener listener, final String pos) {

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

    public MeetInviteParticipantsAdapter(List<MeetInviteParticipantsModel.EpisodeParticipantList> responseList, Context context, ArrayList<String> UserIDs, MeetInviteParticipantsAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        selectedParticipant = new ArrayList();
        selectedParticipant.clear();
        this.context = context;
        this.UserIDs = UserIDs;
        this.listener = listener;
    }

}
