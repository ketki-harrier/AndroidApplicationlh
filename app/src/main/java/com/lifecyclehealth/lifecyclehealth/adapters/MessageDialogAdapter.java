package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.MessageDialogResponse;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ApiCallback;

import java.io.File;
import java.util.List;

/**
 * Created by vaibhavi on 26-10-2017.
 */

public class MessageDialogAdapter extends RecyclerView.Adapter<MessageDialogAdapter.MessageDialogViewHolder> {

    private List<Chat> unread_messageList;
    Context context;
    private MessageDialogAdapter.OnItemClickListener listener;

    @Override
    public MessageDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_message_inflator, parent, false);
        return new MessageDialogViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(Chat item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(final MessageDialogViewHolder holder, int position) {
        Chat chat = unread_messageList.get(position);


        holder.conversationTitleTv.setText(chat.getTopic());
        holder.conversationMessageTv.setText(chat.getLastFeedContent());
        String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(chat.getLastFeedTimeStamp());
        holder.conversationDateTv.setText(s);


        if (chat.getUnreadFeedCount() > 0) {
            holder.tv_badge.setText(String.valueOf(chat.getUnreadFeedCount()));
            holder.tv_badge.setVisibility(View.VISIBLE);
            holder.circle_count.setBackground(context.getResources().getDrawable(R.drawable.badge_circle_bg_orange));
        } else {
            holder.tv_badge.setVisibility(View.INVISIBLE);
            holder.circle_count.setBackground(context.getResources().getDrawable(R.drawable.badge_circle_bg_gray));
        }


        holder.itemView.setTag(unread_messageList);
        holder.bind(chat, listener, String.valueOf(position));

    }

    @Override
    public int getItemCount() {

        if (unread_messageList.size() > 5)
            return 5;
        else
            return unread_messageList.size();
    }

    public class MessageDialogViewHolder extends RecyclerView.ViewHolder {


        private RelativeLayout chatRelative;
        TextView conversationTitleTv, conversationMessageTv, conversationDateTv, tv_badge, circle_count;

        public MessageDialogViewHolder(View itemView) {
            super(itemView);
            conversationTitleTv = (TextView) itemView.findViewById(R.id.conversationTitleTv);
            conversationMessageTv = (TextView) itemView.findViewById(R.id.conversationMessageTv);
            conversationDateTv = (TextView) itemView.findViewById(R.id.conversationDateTv);
            tv_badge = (TextView) itemView.findViewById(R.id.tv_badge);
            circle_count = (TextView) itemView.findViewById(R.id.circle_count);
            chatRelative = (RelativeLayout) itemView.findViewById(R.id.chatRelative);
        }

        public void bind(final Chat item, final MessageDialogAdapter.OnItemClickListener listener, final String pos) {

            chatRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "c", pos);
                }
            });
        }
    }


    public MessageDialogAdapter(List<Chat> unread_messageList, Context context, OnItemClickListener listener) {
        this.unread_messageList = unread_messageList;
        this.context = context;
        this.listener = listener;
    }

}
