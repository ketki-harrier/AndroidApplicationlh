package com.lifecyclehealth.lifecyclehealth.fragments;

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
import com.lifecyclehealth.lifecyclehealth.model.ChatBinderDialogResponse;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 27-10-2017.
 */

public class ChatBinderDialog extends RecyclerView.Adapter<ChatBinderDialog.MyViewHolder> {

    private ChatBinderDialogResponse.BinderDetails meetResponseList;

    private ChatBinderDialog.OnItemClickListener listener;
    Context context;

    @Override
    public ChatBinderDialog.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_invite_participant_inflater, parent, false);
        return new ChatBinderDialog.MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(ChatBinderDialogResponse.BinderDetails item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final ChatBinderDialog.MyViewHolder holder, int position) {

        ChatBinderDialogResponse.Invitees invitees = meetResponseList.getInvitees().get(position);
        holder.addParticipant.setVisibility(View.GONE);
        holder.name.setText(invitees.getName());

        // String role = TextUtils.join(",", meetList.getRoleName());
        holder.provider.setText(invitees.getRole().get(0));



        holder.addParticipant.setVisibility(View.GONE);


        if (invitees.getPicture_uri() != null) {
            com.android.volley.toolbox.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

            imageLoader.get(invitees.getPicture_uri(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
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

        holder.itemView.setTag(invitees);
        holder.bind(invitees, listener, String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return meetResponseList.getInvitees().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, provider;
        ImageView addParticipant;
        CircularImageView imageView;
        LinearLayout linear;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            provider = (TextView) itemView.findViewById(R.id.provider);
            addParticipant = (ImageView) itemView.findViewById(R.id.addParticipant);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            imageView = (CircularImageView) itemView.findViewById(R.id.imageView2);
        }

        public void bind(final ChatBinderDialogResponse.Invitees item, final ChatBinderDialog.OnItemClickListener listener, final String pos) {

           /* linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });*/
        }
    }

    public ChatBinderDialog(ChatBinderDialogResponse.BinderDetails responseList, Context context, ChatBinderDialog.OnItemClickListener listener) {

        this.context = context;
        this.listener = listener;
        meetResponseList = responseList;
    }

}
