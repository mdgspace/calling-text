package com.sdsmdg.pulkit.callingtext;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by pulkit on 4/2/17.
 */

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.ListViewHolder> {

    private List<CallerDetails> callerList;
    private Activity parentAct;
    private int mExpandedPosition = -1;
    private RecyclerView recyclerView;
    private Long timeDiff;

    public CallListAdapter(List<CallerDetails> historyList, Activity activity, RecyclerView recyclerView1) {

        this.callerList = historyList;
        this.recyclerView = recyclerView1;
        parentAct = activity;
    }

    @Override
    public CallListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.history_card, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CallListAdapter.ListViewHolder holder, final int position) {

        CallerDetails callerDetails = callerList.get(position);
        holder.vNumber.setText((CharSequence) callerDetails.getCaller_number());
        holder.vMsg.setText((CharSequence) callerDetails.getCaller_msg());
        timeDiff = (Long.valueOf(System.currentTimeMillis())-Long.parseLong((String) callerDetails.getCall_time()))/1000;
        holder.time.setText(getTime(timeDiff));

        final boolean isExpanded = position==mExpandedPosition;

        if (callerDetails.getCall_type().equals("outgoing")) {

            holder.type.setImageResource(R.drawable.call_made);
        } else if (callerDetails.getCall_type().equals("incoming")) {
            holder.type.setImageResource(R.drawable.call_received);
        }

    }

    public String getTime(Long timeDiff) {
        String time;
        Long minutes = timeDiff / 60;
        Integer hours = minutes.intValue() / 60;
        Integer days = hours / 24;
        if (timeDiff < 60) {
            if (timeDiff == 1) {
                time = " " + timeDiff.toString() + " second ago";
            } else {
                time = " " + timeDiff.toString() + " seconds ago";
            }
        } else {
            if (minutes < 60) {
                if (minutes == 1) {
                    time = " " + minutes.toString() + " minute ago";
                } else {
                    time = " " + minutes.toString() + " minutes ago";
                }


            } else {
                if (hours < 25) {
                    if (hours == 1) {
                        time = " " + hours.toString() + " hour ago";

                    } else {
                        time = " " + hours.toString() + " hours ago";

                    }

                } else {
                    if (days == 1) {
                        time = " " + days.toString() + " day ago";

                    } else {
                        time = " " + days.toString() + " days ago";

                    }
                }
            }

        }
        return time;

    }

    @Override
    public int getItemCount() {
        return callerList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView vName;
        protected TextView time;
        protected ImageView call;
        TextView vNumber;
        TextView vMsg;
        ImageView type;

        ListViewHolder(View vi) {
            super(vi);
            vNumber = (TextView) vi.findViewById(R.id.number);
            vMsg = (TextView) vi.findViewById(R.id.message);
            type = (ImageView) vi.findViewById(R.id.call_type);
            time = (TextView) vi.findViewById(R.id.time);
            call = (ImageView) vi.findViewById(R.id.call);
            vi.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Animation animation = AnimationUtils.loadAnimation(parentAct, R.anim.swipe);
            v.startAnimation(animation);
        }
    }

}
