package com.sdsmdg.pulkit.callingtext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class CallManager extends BroadcastReceiver implements BackGroundWorker.resultInterface {
    public static Context context1;
    public static String msg;
    String caller;
    public static String receiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        context1 = context;
        Log.e("pul", "in receive");
        Log.e("pulkit", "in received");
        String receiver = "7248187747";

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Log.e("MY NO.","PHONE NO."+mPhoneNumber);

        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : "+incomingNumber);
                caller = incomingNumber;
            }
        },PhoneStateListener.LISTEN_CALL_STATE);



        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.i("CALLING SOMEONE ? ","YEEAAAHHAHAH");
            if (!BaseActivity.calledByapp) {
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i("inside", "yes" + number);
                if(number.length()>11){
                    number = number.substring(number.length()-11);
                    number = number.substring(1);
                }

                killCall(context);
                // If it is to call (outgoing)
                Intent i = new Intent(context, PopupDialer.class);
                i.putExtra("number", number);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
            BaseActivity.calledByapp = false;
        }

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state != null) {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                caller = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.e("caller", caller.substring(0));
                caller = caller.substring(caller.length() - 11);
                Log.e("caller", caller.substring(1));
                caller = caller.substring(1);
                Log.e("receiving number", caller);
                if (haveNetworkConnection() == true) {
                    BackGroundWorker b = new BackGroundWorker(context, 1);
                    b.execute(caller, receiver);
                }

            }

        }
    }


//        if (intent.getAction() == "android.provider.Telephony.SMS_RECEIVED") {
//            if (haveNetworkConnection() == true) {
//                BackGroundWorker b = new BackGroundWorker(context, 1);
//                b.execute(caller, receiver);
//            }
//        }
//    }

    public boolean killCall(Context context) {
        Log.i("INside", "killcallll");
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("uyfukkyvhggv", "PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context1.getSystemService(context1.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void getContent(String s) {
        Log.e("in content", "in content");
        Intent intentone = new Intent(context1.getApplicationContext(), CustomDialogBox.class);
        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        msg = s;
        context1.startActivity(intentone);
    }
}
