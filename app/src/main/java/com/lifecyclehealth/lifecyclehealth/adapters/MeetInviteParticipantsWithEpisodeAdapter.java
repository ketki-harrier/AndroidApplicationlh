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
import com.google.gson.Gson;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.callbacks.VolleyCallback;
import com.lifecyclehealth.lifecyclehealth.designate.DesignateCallBack;
import com.lifecyclehealth.lifecyclehealth.model.CheckProviderResponse;
import com.lifecyclehealth.lifecyclehealth.model.MeetInviteParticipantsModel;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkAdapter;
import com.lifecyclehealth.lifecyclehealth.utils.NetworkRequestUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.BASE_URL;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.STATUS_SUCCESS;
import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.URL_MEET_CHECK_IS_DESIGNATE_SELECT;

/**
 * Created by vaibhavi on 27-10-2017.
 */

public class MeetInviteParticipantsWithEpisodeAdapter extends RecyclerView.Adapter<MeetInviteParticipantsWithEpisodeAdapter.MyViewHolder> {

    List<MeetInviteParticipantsModel.EpisodeParticipantList> meetResponseList;
    private MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener listener;
    public static ArrayList selectedParticipant = new ArrayList<>();
    ArrayList<String> selectedUsers = new ArrayList<>();
    Context context;

    @Override
    public MeetInviteParticipantsWithEpisodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_invite_participant_inflater, parent, false);
        return new MeetInviteParticipantsWithEpisodeAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(MeetInviteParticipantsModel.EpisodeParticipantList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MeetInviteParticipantsWithEpisodeAdapter.MyViewHolder holder, int position) {

        final MeetInviteParticipantsModel.EpisodeParticipantList meetList = meetResponseList.get(position);

        holder.addParticipant.setVisibility(View.GONE);
        holder.name.setText(meetList.getFullName());
        // holder.provider.setText(meetList.getRoleName().toString());
        String role = TextUtils.join(",", meetList.getRoleName());
        holder.provider.setText(role);

        if (selectedUsers != null) {
            if (selectedUsers.size() > 0) {
                if (selectedUsers.contains(meetList.getUserID())) {
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

        holder.itemView.setTag(meetList.getUserID());
        holder.bind(meetList, listener, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return meetResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, provider, designate;
        public ImageView addParticipant, designate_image;
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

        public void bind(final MeetInviteParticipantsModel.EpisodeParticipantList item, final MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener listener, final String pos) {

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });
        }
    }

    public MeetInviteParticipantsWithEpisodeAdapter(List<MeetInviteParticipantsModel.EpisodeParticipantList> responseList, Context context, ArrayList<String> selectedUsers, MeetInviteParticipantsWithEpisodeAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        this.selectedUsers = selectedUsers;
        selectedParticipant = new ArrayList();
        this.context = context;
        this.listener = listener;

    }

    public void add(List<MeetInviteParticipantsModel.EpisodeParticipantList> meetResponseList) {

        this.meetResponseList.clear();
        this.meetResponseList.addAll(meetResponseList);
        notifyDataSetChanged();

    }
}
