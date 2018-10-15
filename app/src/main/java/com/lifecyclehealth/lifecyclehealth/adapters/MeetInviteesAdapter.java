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

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.MeetDetailsForProviderResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 10-10-2017.
 */

public class MeetInviteesAdapter extends RecyclerView.Adapter<MeetInviteesAdapter.MyViewHolder> {

    private List<MeetDetailsForProviderResponse.Invitees> meetResponseList;
    private MeetInviteesAdapter.OnItemClickListener listener;
    Context context;

    @Override
    public MeetInviteesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_invitees_list_inflater, parent, false);
        return new MeetInviteesAdapter.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(MeetDetailsForProviderResponse.Invitees item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MeetInviteesAdapter.MyViewHolder holder, int position) {

        final MeetDetailsForProviderResponse.Invitees meetList = meetResponseList.get(position);
        holder.name.setText(meetList.getFirstName() + " " + meetList.getLastName());

        holder.itemView.setTag(meetList);
        holder.bind(meetList, listener, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return meetResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(final MeetDetailsForProviderResponse.Invitees item, final MeetInviteesAdapter.OnItemClickListener listener, final String pos) {

        }
    }

    public MeetInviteesAdapter(List<MeetDetailsForProviderResponse.Invitees> responseList, Context context, MeetInviteesAdapter.OnItemClickListener listener) {
        this.meetResponseList = responseList;
        this.context = context;
        this.listener = listener;
    }
}