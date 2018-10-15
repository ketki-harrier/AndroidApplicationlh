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
import com.lifecyclehealth.lifecyclehealth.activities.ChatActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MainActivity;
import com.lifecyclehealth.lifecyclehealth.activities.MeetEventActivity;
import com.lifecyclehealth.lifecyclehealth.application.MyApplication;
import com.lifecyclehealth.lifecyclehealth.custome.Session;
import com.lifecyclehealth.lifecyclehealth.model.MessageMeetModel;
import com.lifecyclehealth.lifecyclehealth.model.MessageMeetModel1;
import com.moxtra.sdk.chat.model.Chat;
import com.moxtra.sdk.common.ApiCallback;
import com.moxtra.sdk.meet.model.Meet;
import com.segment.analytics.Analytics;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.lifecyclehealth.lifecyclehealth.utils.AppConstants.SESSION_KEY;

/**
 * Created by vaibhavi on 09-11-2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter {

    static public Context context;
    private ArrayList<MessageMeetModel1> messageMeetModel;
    final List<Session> sessionList = new ArrayList<>();
    List<Chat> chatList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message_conversation, parent, false);
                return new ChatTypeViewHolder(view);
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_message_mmet_inflator, parent, false);
                return new MeetTypeViewHolder(view);

        }
        return null;

   /*     View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_message_conversation, parent, false);*/
        //return new ViewHolder(v);
    }


    @Override
    public int getItemViewType(int position) {

       /* switch (messageMeetModel.getChatType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }*/
        return messageMeetModel.get(position).getChatType();
    }

    public static class ChatTypeViewHolder extends RecyclerView.ViewHolder {


        ImageView conversationImage;
        TextView conversationTitleTv, conversationMessageTv, conversationDateTv;
        RelativeLayout chatRelative;
        Session session;
        final TextView tvBadge;
        final View itemView;


        public ChatTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            conversationImage = (ImageView) itemView.findViewById(R.id.conversationImage);
            conversationImage = (ImageView) itemView.findViewById(R.id.conversationImage);
            conversationTitleTv = (TextView) itemView.findViewById(R.id.conversationTitleTv);
            conversationMessageTv = (TextView) itemView.findViewById(R.id.conversationMessageTv);
            conversationDateTv = (TextView) itemView.findViewById(R.id.conversationDateTv);
            chatRelative = (RelativeLayout) itemView.findViewById(R.id.chatRelative);
            tvBadge = (TextView) itemView.findViewById(R.id.tv_badge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.showChat(context, session.chat);
                }
            });
        }
    }

    public static class MeetTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView btnJoin, btnAccept;
        private RelativeLayout chatRelative, relative_join, relative_accept;
        ImageView cancelMeet, conversationImage;
        TextView conversationTitleTv, conversationMessageTv, conversationDateTv, conversationDateTv1, tv_badge;

        public MeetTypeViewHolder(View itemView) {
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

        public void bind(final MessageMeetModel1 item, final MessageDialogMeetAdapter.OnItemClickListener listener, final String pos) {

            chatRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(item, "c", pos);
                }
            });


            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // listener.onItemClick(item, "A", pos);
                }
            });

            cancelMeet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // listener.onItemClick(item, "C", pos);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (messageMeetModel.get(position).getChatType()) {
            case 1: {
                final ChatTypeViewHolder theHolder = (ChatTypeViewHolder) holder;
                //Session session = sessionList.get(position + messageMeetModel.getMeetListDTO().getMeetList().size());
                int ss = messageMeetModel.size() - sessionList.size();
                Session session = sessionList.get(position-ss);
                theHolder.session = session;

                final Chat chat = session.chat;

                theHolder.conversationTitleTv.setText(chat.getTopic());
                theHolder.conversationMessageTv.setText(chat.getLastFeedContent());
                String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(chat.getLastFeedTimeStamp());
                theHolder.conversationDateTv.setText(s);

                chat.fetchCover(new ApiCallback<String>() {
                    @Override
                    public void onCompleted(final String avatarPath) {
                        Log.d("", " Chat cover=" + avatarPath);
                        if (!TextUtils.isEmpty(avatarPath)) {
                            theHolder.conversationImage.setImageURI(Uri.fromFile(new File(avatarPath)));
                        } else {
                            theHolder.conversationImage.setImageResource(R.drawable.meet_profile);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                    }
                });

                if (chat.getUnreadFeedCount() > 0) {
                    theHolder.tvBadge.setText(String.valueOf(chat.getUnreadFeedCount()));
                    theHolder.tvBadge.setVisibility(View.VISIBLE);
                } else {
                    theHolder.tvBadge.setVisibility(View.GONE);
                }
                break;
            }


            case 0: {

                final Meet meet = messageMeetModel.get(position).getMeetList();
                final MeetTypeViewHolder holderMessage = (MeetTypeViewHolder) holder;

                holderMessage.conversationTitleTv.setText(meet.getTopic());
                //holder.conversationMessageTv.setText(meet.getStatus());
                String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(meet.getScheduleStartTime());
                holderMessage.conversationDateTv.setText(s);
                holderMessage.conversationDateTv1.setText(s);

                holderMessage.btnJoin.setVisibility(View.GONE);
                holderMessage.relative_accept.setVisibility(View.GONE);


                if (meet.isInProgress()) {
                    holderMessage.relative_join.setVisibility(View.VISIBLE);
                    holderMessage.relative_accept.setVisibility(View.GONE);
                    holderMessage.btnJoin.setVisibility(View.VISIBLE);
                    holderMessage.conversationDateTv1.setVisibility(View.GONE);
                } else if (meet.getScheduleStartTime() > 0) {
                    holderMessage.relative_join.setVisibility(View.GONE);
                    holderMessage.relative_accept.setVisibility(View.VISIBLE);
                    holderMessage.conversationDateTv.setVisibility(View.GONE);
                }

                holderMessage.conversationTitleTv.setText(meet.getTopic());
                holderMessage.btnJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.getInstance().addToSharedPreference(SESSION_KEY, meet.getID());
                        MeetEventActivity.joinMeet(context);
                    }
                });

                holderMessage.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        try {
                            MainActivity.mMeetRepo.acceptMeet(meet.getID(), new ApiCallback<Meet>() {
                                @Override
                                public void onCompleted(Meet meet) {
                                    for (int i = 0; i < MainActivity.meetLists.size(); i++) {
                                        if (MainActivity.meetLists.get(i).getID().equals(meet.getID())) {
                                            MainActivity.meetLists.remove(i);
                                        }
                                    }
                                    ChatListAdapter.this.notify();
                                }

                                @Override
                                public void onError(int i, String s) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holderMessage.cancelMeet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.mMeetRepo.declineMeet(meet.getID(), new ApiCallback<Void>() {
                            @Override
                            public void onCompleted(Void aVoid) {
                                Log.e("remove", "meet");
                                for (int i = 0; i < MainActivity.meetLists.size(); i++) {
                                    if (MainActivity.meetLists.get(i).getID().equals(meet.getID())) {
                                        MainActivity.meetLists.remove(i);
                                    }
                                }
                                messageMeetModel.remove(position);
                                notifyItemRemoved(position);

                            }

                            @Override
                            public void onError(int i, String s) {

                            }
                        });
                    }
                });

                break;
            }

        }
    }

    @Override
    public int getItemCount() {

        int size = 0;


     /*   if (messageMeetModel.getMeetListDTO().getMeetList().size() > 0) {
            size = messageMeetModel.getMeetListDTO().getMeetList().size();
        } else {*/
        //size = size + sessionList.size();
        //}

        size = messageMeetModel.size();

        return size;
    }


    public ChatListAdapter(Context context) {
        super();
        this.context = context;
    }


    private void sortData() {

        Collections.sort(sessionList, new Comparator<Session>() {

            @Override
            public int compare(Session arg0, Session arg1) {
                SimpleDateFormat format = new SimpleDateFormat("KK:mm aa yyyy-MM-dd");

                int compareResult = 0;
                try {
                    String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg0.chat.getLastFeedTimeStamp());
                    String s1 = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg1.chat.getLastFeedTimeStamp());
                    Date arg0Date = format.parse(s);
                    Date arg1Date = format.parse(s1);
                    compareResult = arg1Date.compareTo(arg0Date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return compareResult;
            }
        });
    }

    public void updateChats(ArrayList<MessageMeetModel1> messageMeetModel, List<Chat> chatList) {
        sortData();
        this.messageMeetModel = messageMeetModel;
        this.chatList = chatList;
        refreshData();
    }


    /* public void updateChats(List<Chat> chats) {
        sortData();

        this.chatList = chats;
        refreshData();
    }*/


    void refreshData() {

        sessionList.clear();
        if (chatList != null) {
            for (Chat chat : chatList) {
                sessionList.add(new Session(chat));
            }
        }
        sortData();
        notifyDataSetChanged();
    }


    public static Date gmttoLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

}










/*
ChatListAdapter extends RecyclerView.Adapter {

static public Context context;
private ArrayList<MessageMeetModel> messageMeetModel;
final List<Session> sessionList = new ArrayList<>();
        List<Chat> chatList;

@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
        case 1:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_message_conversation, parent, false);
        return new ChatTypeViewHolder(view);
        case 0:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_message_mmet_inflator, parent, false);
        return new MeetTypeViewHolder(view);

        }
        return null;

   */
/*     View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_message_conversation, parent, false);*//*

        //return new ViewHolder(v);
        }


@Override
public int getItemViewType(int position) {

       */
/* switch (messageMeetModel.getChatType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }*//*

        return messageMeetModel.get(position).getChatType();
        }

public static class ChatTypeViewHolder extends RecyclerView.ViewHolder {


    ImageView conversationImage;
    TextView conversationTitleTv, conversationMessageTv, conversationDateTv;
    RelativeLayout chatRelative;
    Session session;
    final TextView tvBadge;
    final View itemView;


    public ChatTypeViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        conversationImage = (ImageView) itemView.findViewById(R.id.conversationImage);
        conversationImage = (ImageView) itemView.findViewById(R.id.conversationImage);
        conversationTitleTv = (TextView) itemView.findViewById(R.id.conversationTitleTv);
        conversationMessageTv = (TextView) itemView.findViewById(R.id.conversationMessageTv);
        conversationDateTv = (TextView) itemView.findViewById(R.id.conversationDateTv);
        chatRelative = (RelativeLayout) itemView.findViewById(R.id.chatRelative);
        tvBadge = (TextView) itemView.findViewById(R.id.tv_badge);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.showChat(context, session.chat);
            }
        });
    }
}

public static class MeetTypeViewHolder extends RecyclerView.ViewHolder {

    private TextView btnJoin, btnAccept;
    private RelativeLayout chatRelative, relative_join, relative_accept;
    ImageView cancelMeet, conversationImage;
    TextView conversationTitleTv, conversationMessageTv, conversationDateTv, conversationDateTv1, tv_badge;

    public MeetTypeViewHolder(View itemView) {
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

    public void bind(final MessageMeetModel item, final MessageDialogMeetAdapter.OnItemClickListener listener, final String pos) {

        chatRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.onItemClick(item, "c", pos);
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.onItemClick(item, "A", pos);
            }
        });

        cancelMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // listener.onItemClick(item, "C", pos);
            }
        });
    }
}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        switch (messageMeetModel.get(position).getChatType()) {
            case 1: {
                final ChatTypeViewHolder theHolder = (ChatTypeViewHolder) holder;
                //Session session = sessionList.get(position + messageMeetModel.getMeetListDTO().getMeetList().size());
                Session session = sessionList.get(position);
                theHolder.session = session;

                final Chat chat = session.chat;

                theHolder.conversationTitleTv.setText(chat.getTopic());
                theHolder.conversationMessageTv.setText(chat.getLastFeedContent());
                String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(chat.getLastFeedTimeStamp());
                theHolder.conversationDateTv.setText(s);

                chat.fetchCover(new ApiCallback<String>() {
                    @Override
                    public void onCompleted(final String avatarPath) {
                        Log.d("", " Chat cover=" + avatarPath);
                        if (!TextUtils.isEmpty(avatarPath)) {
                            theHolder.conversationImage.setImageURI(Uri.fromFile(new File(avatarPath)));
                        } else {
                            theHolder.conversationImage.setImageResource(R.drawable.meet_profile);
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                    }
                });

                if (chat.getUnreadFeedCount() > 0) {
                    theHolder.tvBadge.setText(String.valueOf(chat.getUnreadFeedCount()));
                    theHolder.tvBadge.setVisibility(View.VISIBLE);
                } else {
                    theHolder.tvBadge.setVisibility(View.INVISIBLE);
                }
                break;
            }


            case 0: {

                final MessageMeetModel.MeetList meet = messageMeetModel.get(position).getMeetList();
                final MeetTypeViewHolder holderMessage = (MeetTypeViewHolder) holder;

                holderMessage.conversationTitleTv.setText(meet.getTopic());

                holderMessage.btnJoin.setVisibility(View.GONE);
                holderMessage.relative_accept.setVisibility(View.GONE);


                if (meet.getStatus().equals("SESSION_STARTED")) {
                    holderMessage.relative_join.setVisibility(View.VISIBLE);
                    holderMessage.relative_accept.setVisibility(View.GONE);
                    holderMessage.btnJoin.setVisibility(View.VISIBLE);
                    holderMessage.conversationDateTv1.setVisibility(View.GONE);
                } else if (meet.getStatus().equals("SESSION_SCHEDULED")) {
                    holderMessage.relative_join.setVisibility(View.GONE);
                    holderMessage.relative_accept.setVisibility(View.VISIBLE);
                    holderMessage.conversationDateTv.setVisibility(View.GONE);
                }

                //-------set meetTime--------
                String ScheduleStart = "";

                if (meet.getStatus().equals("SESSION_STARTED")) {
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
                    holderMessage.conversationDateTv.setText(new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(updatedDate));
                }

                holderMessage.btnJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.getInstance().addToSharedPreference(SESSION_KEY, meet.getSession_key());
                        MeetEventActivity.joinMeet(context);
                    }
                });
                break;
            }

        }
    }

    @Override
    public int getItemCount() {

        int size = 0;


     */
/*   if (messageMeetModel.getMeetListDTO().getMeetList().size() > 0) {
            size = messageMeetModel.getMeetListDTO().getMeetList().size();
        } else {*//*

        size = size + sessionList.size();
        //}

        return size;
    }


    public ChatListAdapter(Context context) {
        super();
        this.context = context;
    }


    private void sortData() {

        Collections.sort(sessionList, new Comparator<Session>() {

            @Override
            public int compare(Session arg0, Session arg1) {
                SimpleDateFormat format = new SimpleDateFormat("KK:mm aa yyyy-MM-dd");

                int compareResult = 0;
                try {
                    String s = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg0.chat.getLastFeedTimeStamp());
                    String s1 = new java.text.SimpleDateFormat("KK:mm aa yyyy-MM-dd").format(arg1.chat.getLastFeedTimeStamp());
                    Date arg0Date = format.parse(s);
                    Date arg1Date = format.parse(s1);
                    compareResult = arg1Date.compareTo(arg0Date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return compareResult;
            }
        });
    }

    public void updateChats(ArrayList<MessageMeetModel> messageMeetModel, List<Chat> chatList) {
        sortData();
        this.messageMeetModel = messageMeetModel;
        this.chatList = chatList;
        refreshData();
    }


    */
/* public void updateChats(List<Chat> chats) {
        sortData();

        this.chatList = chats;
        refreshData();
    }*//*



    void refreshData() {

        sessionList.clear();
        if (chatList != null) {
            for (Chat chat : chatList) {
                sessionList.add(new Session(chat));
            }
        }
        sortData();
        notifyDataSetChanged();
    }


    public static Date gmttoLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

}*/
