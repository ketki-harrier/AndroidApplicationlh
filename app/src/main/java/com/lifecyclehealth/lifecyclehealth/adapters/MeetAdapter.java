package com.lifecyclehealth.lifecyclehealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.dto.MeetListDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.PREF_IS_PATIENT;

/**
 * Created by vaibhavi on 21-08-2017.
 */

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MyViewHolder> {

    private List<MeetListDTO.MeetList> meetResponseList;

    private OnItemClickListener listener;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meet_inflater, parent, false);
        return new MyViewHolder(view);
    }


    public interface OnItemClickListener {
        void onItemClick(MeetListDTO.MeetList item, String Type, String pos);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MeetListDTO.MeetList meetList = meetResponseList.get(position);
        holder.meet_subject.setText(meetList.getTopic());
        holder.btn_meet.setVisibility(View.GONE);
        boolean isPatient = MyApplication.getInstance().getBooleanFromSharedPreference(PREF_IS_PATIENT);


        if (!meetList.getStatus().equals("SESSION_ENDED")) {
            if ((meetList.getStatus().equals("SESSION_STARTED") && meetList.isSelf_hosted() == true) || meetList.isSelf_hosted() == false) {
                holder.btn_meet.setText("JOIN MEET");
                holder.btn_meet.setVisibility(View.VISIBLE);
            } else if (meetList.getStatus().equals("SESSION_SCHEDULED") && meetList.isSelf_hosted() == true) {
                holder.btn_meet.setText("START MEET");
                holder.btn_meet.setVisibility(View.VISIBLE);
            } else {
                holder.btn_meet.setVisibility(View.GONE);
            }
        }


        //-------set meetTime--------
        String ScheduleStart = "";

        if (meetList.getStatus().equals("SESSION_ENDED")) {
            ScheduleStart = meetList.getStarts();

        } else if (meetList.getStatus().equals("SESSION_STARTED")) {
            ScheduleStart = meetList.getStarts();
        } else if (meetList.getStatus().equals("SESSION_SCHEDULED")) {
            ScheduleStart = meetList.getScheduled_starts();
        }


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("KK:mm aa");
        Date d = null;
        Date updatedDate = null;
        try {

            d = sdf.parse(ScheduleStart);
            updatedDate = gmttoLocalDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTime = output.format(updatedDate);
        holder.meet_time.setText(formattedTime + "");

        holder.itemView.setTag(meetList);
        holder.bind(meetList, listener, String.valueOf(position));
    }

    public static Date gmttoLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    @Override
    public int getItemCount() {
        //return meetResponseList.size();
        return meetResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView meet_subject, meet_time;
        private Button btn_meet;
        private LinearLayout linear;


        public MyViewHolder(View itemView) {
            super(itemView);
            meet_subject = (TextView) itemView.findViewById(R.id.meet_subject);
            meet_time = (TextView) itemView.findViewById(R.id.meet_time);
            btn_meet = (Button) itemView.findViewById(R.id.btn_meet);
            linear= (LinearLayout) itemView.findViewById(R.id.linear);
        }

        public void bind(final MeetListDTO.MeetList item, final OnItemClickListener listener, final String pos) {

            btn_meet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "M", pos);
                }
            });
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, "D", pos);
                }
            });

        }
    }

    public MeetAdapter(List<MeetListDTO.MeetList> responseList, Context context, OnItemClickListener listener) {
        this.meetResponseList = responseList;
        this.context = context;
        this.listener = listener;
    }

}
