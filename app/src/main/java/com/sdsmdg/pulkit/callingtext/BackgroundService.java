package com.sdsmdg.pulkit.callingtext;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by pulkit on 4/2/17.
 */

public class BackgroundService extends Service {

    DatabaseReference callertree = FirebaseDatabase.getInstance().getReference().child("caller");
    DatabaseReference receivertree = FirebaseDatabase.getInstance().getReference().child("receiver");
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
    public static int count=0;
    DataBaseHandler dbh;
    String receiver;
    public void onCreate()
    {

//      receiver = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("NUMBER", "8791286328");

        BaseActivity.receiver = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("NUMBER", "7248187747");
        Log.e("Background service","service started"+BaseActivity.receiver);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("receiver").child(BaseActivity.receiver) != null) {
                    count++;
                    Log.e("count",count+"");
                    Log.e("on data change listener", "on data change listener");
                    BackGroundWorker.value = snapshot.child("receiver").child(BaseActivity.receiver).child("caller").getValue().toString();
                    BackGroundWorker.gifId = snapshot.child("receiver").child(BaseActivity.receiver).child("gifId").getValue().toString();
                    BackGroundWorker.msg = snapshot.child("receiver").child(BaseActivity.receiver).child("message").getValue().toString();
                    Log.e("gif",BackGroundWorker.gifId );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Background service","service started");

        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        alarmService.set(
//                AlarmManager.ELAPSED_REALTIME,
//                SystemClock.elapsedRealtime() + 1000,
//                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}