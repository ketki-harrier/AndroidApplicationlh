package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.meet.model.Meet;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;

/**
 * Created by vaibhavi on 31-10-2017.
 */

public class MessageDialogMeetAdapter extends RecyclerView.Adapter<MessageDialogMeetAdapter.MessageDialogViewHolder> {

    private List<Meet> unread_messageList;
    Context context;
    private MessageDialogMeetAdapter.OnItemClickListener listener;

    @Override
    public MessageDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_message_mmet_inflator, parent, false);
        return new MessageDialogViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(Meet item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MessageDialogViewHolder holder, int position) {
        final Meet meet = unread_messageList.get(position);


        holder.conversationTitleTv.setText(meet.getTopic());
        //holder.conversationMessageTv.setText(meet.getStatus());
        String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(meet.getScheduleStartTime());
        holder.conversationDateTv.setText(s);
        holder.conversationDateTv1.setText(s);

        holder.btnJoin.setVisibility(View.GONE);
        holder.relative_accept.setVisibility(View.GONE);


        if (meet.isInProgress()) {
            holder.relative_join.setVisibility(View.VISIBLE);
            holder.relative_accept.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);
            holder.conversationDateTv1.setVisibility(View.GONE);
        } else if (meet.getScheduleStartTime()>0) {
            holder.relative_join.setVisibility(View.GONE);
            holder.relative_accept.setVisibility(View.VISIBLE);
            holder.conversationDateTv.setVisibility(View.GONE);
        }


        //-------set meetTime--------
        String ScheduleStart = "";

     /*   if (meet.getStatus().equals("SESSION_STARTED")) {
            ScheduleStart = meet.getStarts();
        }

        if (!ScheduleStart.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = null;
            Date updatedDate = null;
            try {

                d = sdf.parse(ScheduleStart);
                updatedDate = gmttoLocalDate(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.conversationDateTv.setText(new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(updatedDate));
        }
*/

   /*     if (meet.isInProgress()) {
            holder.relative_join.setVisibility(View.VISIBLE);
            holder.relative_accept.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);
            holder.conversationDateTv1.setVisibility(View.GONE);

        } *//*else {
            holder.relative_join.setVisibility(View.VISIBLE);
            holder.relative_accept.setVisibility(View.GONE);
            holder.btnJoin.setVisibility(View.VISIBLE);
            holder.conversationDateTv1.setVisibility(View.GONE);
        }*//*

        if (meet.isRecurrentMeet()) {
            holder.relative_join.setVisibility(View.GONE);
            holder.relative_accept.setVisibility(View.VISIBLE);
            holder.conversationDateTv.setVisibility(View.GONE);
        }*/


        holder.itemView.setTag(unread_messageList);
        holder.bind(meet, listener, String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return unread_messageList.size();
    }

    public class MessageDialogViewHolder extends RecyclerView.ViewHolder {

        private TextView btnJoin, btnAccept;
        private RelativeLayout chatRelative, relative_join, relative_accept;
        ImageView cancelMeet, conversationImage;
        TextView conversationTitleTv, conversationMessageTv, conversationDateTv, conversationDateTv1, tv_badge;

        public MessageDialogViewHolder(View itemView) {
            super(itemView);
            conversationTitleTv = (TextView) itemView.findViewById(R.id.conversationTitleTv);
            conversationMessageTv = (TextView) itemView.findViewById(R.id.conversationMessageTv);
            conversationDateTv = (TextView) itemView.findViewById(R.id.conversationDateTv);
            conversationDateTv1 = (TextView) itemView.findViewById(R.id.conversationDateTv1);
            tv_badge = (TextView) itemView.findViewById(R.id.tv_badge);
            chatRelative = (RelativeLayout) itemView.findViewById(R.id.chatRelative);
            relative_join = (RelativeLayout) itemView.findViewById(R.id.relative_join);
            relative_accept = (RelativeLayout) itemView.findViewById(R.id.relative_accept);
            btnJoin = (TextView) itemView.findViewById(R.id.btnJoin);
            btnAccept = (TextView) itemView.findViewById(R.id.btnAccept);
            cancelMeet = (ImageView) itemView.findViewById(R.id.cancelMeet);
            conversationImage = (ImageView) itemView.findViewById(R.id.conversationImage);
        }

        public void bind(final Meet item, final MessageDialogMeetAdapter.OnItemClickListener listener, final String pos) {

            chatRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });

            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "J", pos);
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "A", pos);
                }
            });

            cancelMeet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "C", pos);
                }
            });
        }
    }


    public MessageDialogMeetAdapter(List<Meet> unread_messageList, Context context, OnItemClickListener listener) {
        this.unread_messageList = unread_messageList;
        this.context = context;
        this.listener = listener;
    }


    public static Date gmttoLocalDate(Date date) {
        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

}
