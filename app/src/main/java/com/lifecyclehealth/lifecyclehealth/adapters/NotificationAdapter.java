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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.model.NotificationDialogResponse;
import com.lifecyclehealth.lifecyclehealth.model.PatientSurveyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhavi on 18-01-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> implements Filterable {

    private List<NotificationDialogResponse.AlertList> notificationAlertLists;
    private List<NotificationDialogResponse.AlertList> notificationAlertListsFiltered;
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
      //  NotificationDialogResponse.AlertList alertList = notificationAlertLists.get(position);
        NotificationDialogResponse.AlertList alertList = notificationAlertListsFiltered.get(position);
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
        if (notificationAlertListsFiltered != null) {
            return notificationAlertListsFiltered.size();
        } else {
            return 0;
        }

        //return notificationAlertListsFiltered.size();
        //return notificationAlertLists.size();
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

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(List<NotificationDialogResponse.AlertList> unread_messageList) {
        this.notificationAlertListsFiltered = unread_messageList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    notificationAlertListsFiltered = notificationAlertLists;
                } else {
                    List<NotificationDialogResponse.AlertList> filteredList = new ArrayList<>();
                    for (NotificationDialogResponse.AlertList row : notificationAlertLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name  match
                        if (row.getDescription().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    notificationAlertListsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = notificationAlertListsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notificationAlertListsFiltered = (ArrayList<NotificationDialogResponse.AlertList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public NotificationAdapter(List<NotificationDialogResponse.AlertList> unread_messageList, Context context, OnItemClickListener listener) {
        this.notificationAlertLists = unread_messageList;
        this.context = context;
        this.listener = listener;
        this.notificationAlertListsFiltered = unread_messageList;
    }

   /* public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }*/
}
