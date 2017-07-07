package com.sdsmdg.pulkit.callingtext;

import android.app.Activity;
import android.media.Image;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pulkit on 30/6/17.
 */

public class RecentCallsAdapter extends RecyclerView.Adapter<RecentCallsAdapter.ViewHolder> {
/*This class is the adapter for recent calls from a particular number*/
     private  List<CallerDetails> callerDetails;
     private  RecyclerView recyclerView;
     private Activity parentAct;
     private Long timeDiff;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.recent_calls_card, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(itemView);
        return (ViewHolder) viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CallerDetails callerDetails= this.callerDetails.get(position);
        holder.mNumber.setText(callerDetails.getCaller_number());
        holder.mCallDuration.setText(callerDetails.getCall_duration());
        timeDiff = (Long.valueOf(System.currentTimeMillis())-Long.parseLong(callerDetails.getCall_time()))/1000;
        holder.mCallTime.setText(getTime(timeDiff));


        if (callerDetails.getCall_type().equals("outgoing")) {
            holder.callType.setImageResource(R.drawable.call_made);
        } else if (callerDetails.getCall_type().equals("incoming")) {
            holder.callType.setImageResource(R.drawable.call_received);
        }
    }

    @Override
    public int getItemCount() {
        return callerDetails.size();
    }
    public String getTime(Long timeDiff)
    {
        String time;
        Long minutes = timeDiff/60;
        Integer hours = minutes.intValue()/60;
        Integer days = hours/24;
        if(timeDiff<60){
            if(timeDiff==1){
                time = " "+timeDiff.toString() + " second ago";
            } else {
                time = " "+timeDiff.toString() + " seconds ago";
            }
        }else {
            if (minutes < 60) {
                if(minutes==1){
                    time = " "+minutes.toString() + " minute ago";
                } else {
                    time = " "+minutes.toString() + " minutes ago";
                }


            } else {
                if (hours < 25) {
                    if(hours==1){
                        time = " "+hours.toString() + " hour ago";

                    }else {
                        time = " "+hours.toString() + " hours ago";

                    }

                } else {
                    if(days==1){
                        time = " "+days.toString() + " day ago";

                    }else {
                        time = " "+days.toString() + " days ago";

                    }
                }
            }

        }
        return time;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumber, mCallDuration, mCallTime;
        private ImageView callType;

        public ViewHolder(View itemView) {
            super(itemView);
            mNumber= (TextView) itemView.findViewById(R.id.numberTextView);
            mCallDuration= (TextView) itemView.findViewById(R.id.callDuration);
            mCallTime= (TextView) itemView.findViewById(R.id.date_time_textView);
            callType= (ImageView) itemView.findViewById(R.id.callType);
        }
    }

    public RecentCallsAdapter (List<CallerDetails> callerDetailsList, Activity activity, RecyclerView recyclerView){
        callerDetails=callerDetailsList;
        parentAct=activity;
        this.recyclerView=recyclerView;
    }

}
