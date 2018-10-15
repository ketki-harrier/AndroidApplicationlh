package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.NotificationDialogResponse;

import java.util.List;

/**
 * Created by vaibhavi on 18-01-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationDialogResponse.AlertList> notificationAlertLists;
    Context context;
    private NotificationAdapter.OnItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_notification_fragment_inflator, parent, false);
        return new MyViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(NotificationDialogResponse.AlertList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NotificationDialogResponse.AlertList alertList = notificationAlertLists.get(position);
        Spannable title = new SpannableString(alertList.getTitle());
        title.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color)), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.conversationTitleTv.setText(title + "  ");

        Spannable date = new SpannableString(alertList.getDate_Formatted());
        date.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_color_grey)), 0, date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.conversationTitleTv.append(date);

        holder.conversationMessageTv.setText(alertList.getDescription());
        holder.conversationTitleTv.setPaintFlags(holder.conversationTitleTv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        holder.deleteNotification.setVisibility(View.VISIBLE);
        if (alertList.getStatus().equals("Deleted")) {
            holder.deleteNotification.setVisibility(View.GONE);
            holder.circle_count.setBackground(context.getResources().getDrawable(R.drawable.badge_circle_bg_gray));
            holder.conversationTitleTv.setPaintFlags(holder.conversationTitleTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (alertList.getStatus().equals("New")) {
            holder.circle_count.setBackground(context.getResources().getDrawable(R.drawable.badge_circle_bg_orange));
        } else {
            holder.circle_count.setBackground(context.getResources().getDrawable(R.drawable.badge_circle_bg_gray));
        }

        holder.itemView.setTag(notificationAlertLists);
        holder.bind(alertList, listener, String.valueOf(position));

    }


    @Override
    public int getItemCount() {
        if (notificationAlertLists != null) {
            return notificationAlertLists.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout notificationRelative, viewForeground, viewBackground;
        TextView conversationTitleTv, conversationMessageTv, circle_count, deleteNotification;

        public MyViewHolder(View itemView) {
            super(itemView);
            conversationTitleTv = (TextView) itemView.findViewById(R.id.conversationTitleTv);
            conversationMessageTv = (TextView) itemView.findViewById(R.id.conversationMessageTv);
            circle_count = (TextView) itemView.findViewById(R.id.circle_count);
            deleteNotification = (TextView) itemView.findViewById(R.id.deleteNotification);
            notificationRelative = (RelativeLayout) itemView.findViewById(R.id.notificationRelative);
        }

        public void bind(final NotificationDialogResponse.AlertList item, final NotificationAdapter.OnItemClickListener listener, final String pos) {

            notificationRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "n", pos);
                }
            });

            deleteNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "d", pos);
                }
            });
        }
    }


    public NotificationAdapter(List<NotificationDialogResponse.AlertList> unread_messageList, Context context, OnItemClickListener listener) {
        this.notificationAlertLists = unread_messageList;
        this.context = context;
        this.listener = listener;
    }

}
