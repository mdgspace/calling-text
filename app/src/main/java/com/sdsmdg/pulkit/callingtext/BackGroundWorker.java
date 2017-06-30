package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by this pc on 08-08-2016.
 */
public class BackGroundWorker extends AsyncTask<String, Void, String> {
    private Context context;
    private String caller, receiver;
    int n1;
    CallManager cm;
    private String name;
    String number;
    private String type;
    private String time;
    static String value = "";
    static String gifId;
    static String msg;
    private DataBaseHandler dbh;
    private resultInterface mCallback;
    private DatabaseReference callertree = FirebaseDatabase.getInstance().getReference().child("caller");
    private DatabaseReference receivertree = FirebaseDatabase.getInstance().getReference().child("receiver");
    private DatabaseReference dr = FirebaseDatabase.getInstance().getReference();

    public BackGroundWorker(Context context1, int n) {
        mCallback=(CallManager)new CallManager();
        context = context1;
        n1 = n;

    }
    public interface resultInterface
    {
        public void getContent(String s);
    }


    @Override
    protected String doInBackground(String... params) {
        if (n1 == 1) {
            caller = params[0];
            receiver = params[1];
            if(BackgroundService.count<=2) {
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e("in datachange", "in datachange");
                        if (snapshot.child("caller").child(caller) != null) {
                            value = snapshot.child("caller").child(caller).child("receiver").getValue().toString();
                            gifId = snapshot.child("caller").child(caller).child("gifId").getValue().toString();
                            msg = snapshot.child("caller").child(caller).child("message").getValue().toString();
                            Log.e("receiver", receiver);
                            Log.e("caller", value);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                    }
                });
                if (value.equals(receiver)) {
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(BackGroundWorker.value));
                    Cursor phones = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                    while (phones.moveToNext()) {
                        name = phones.getString(phones.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }
                    number = BackGroundWorker.value;
                    type = "incoming";
                    time = String.valueOf(new Date().getTime());
                    CallerDetails cd =new CallerDetails(name,number,msg,type,time);
                    dbh= DataBaseHandler.getInstance(context);
                    dbh.addCaller(cd);
                    mCallback.getContent(msg + " " + gifId);
                }
            }else {
                if (value.equals(caller)) {
                    Log.e("in value", "in value");
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(BackGroundWorker.value));
                    Cursor phones = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                    while (phones.moveToNext()) {
                        name = phones.getString(phones.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }
                    number = BackGroundWorker.value;
                    type = "incoming";
                    time = String.valueOf(new Date().getTime());
                    CallerDetails cd =new CallerDetails(name,number,msg,type,time);
                    dbh= DataBaseHandler.getInstance(context);
                    dbh.addCaller(cd);

                    mCallback.getContent(msg + " " + gifId);
                }
                BackgroundService.count=1;
            }
        } else {
            caller = params[0];
            receiver = params[1];
            msg = params[2];
            gifId = params[3];
            callertree.child(caller).child("receiver").setValue(receiver);
            callertree.child(caller).child("message").setValue(msg);
            callertree.child(caller).child("gifId").setValue(gifId);
            receivertree.child(receiver).child("caller").setValue(caller);
            receivertree.child(receiver).child("message").setValue(msg);
            receivertree.child(receiver).child("gifId").setValue(gifId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
