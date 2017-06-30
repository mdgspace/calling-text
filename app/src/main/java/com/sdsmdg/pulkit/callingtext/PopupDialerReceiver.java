//package com.sdsmdg.pulkit.callingtext;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//
//import java.lang.reflect.Method;
//
//*
// * Created by pulkit on 2/2/17.
//
//
//
//
// THis class if for reference !! It has been shifted to Callmanager!
//
//
//public class PopupDialerReceiver extends BroadcastReceiver {
//
//    public static String TAG="POpupdialerreciever";
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
////        Log.e("PopupDialer", "in oncalling");
////        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
////            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
////            Log.i("inside","yes"+number);
//////            setResultData(null);
////            killCall(context);
////        // If it is to call (outgoing)
////            Intent i = new Intent(context, PopupDialer.class);
////            i.putExtra("number",number);
////            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(i);
//////            context.unregisterReceiver(this);
//////            Log.i("unregistered","YEEEESSS");
////        }
//
////
////        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
////            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
////            Log.d(TAG,"PhoneStateReceiver**Call State=" + state);
////
////            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
////                Log.d(TAG,"PhoneStateReceiver**Idle");
////            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
////                // Incoming call
////                String incomingNumber =
////                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
////                Log.d(TAG,"PhoneStateReceiver**Incoming call " + incomingNumber);
////
////                if (!killCall(context)) { // Using the method defined earlier
////                    Log.d(TAG,"PhoneStateReceiver **Unable to kill incoming call");
////                }
////
////            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
////                Log.d(TAG,"PhoneStateReceiver **Offhook");
////            }
////        } else if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
////            // Outgoing call
////            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
////            Log.d(TAG,"PhoneStateReceiver **Outgoing call " + outgoingNumber);
////
////            setResultData(null); // Kills the outgoing call
////
////        } else {
////            Log.d(TAG,"PhoneStateReceiver **unexpected intent.action=" + intent.getAction());
////        }
//
//
//
////        Intent intentone = new Intent(context.getApplicationContext(), PopupDialer.class);
////        intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(intentone);
//    }
//
//
//
//    public boolean killCall(Context context) {
//        Log.i("INside","killcallll");
//        try {
//            // Get the boring old TelephonyManager
//            TelephonyManager telephonyManager =
//                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//
//            // Get the getITelephony() method
//            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
//            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
//
//            // Ignore that the method is supposed to be private
//            methodGetITelephony.setAccessible(true);
//
//            // Invoke getITelephony() to get the ITelephony interface
//            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
//
//            // Get the endCall method from ITelephony
//            Class telephonyInterfaceClass =
//                    Class.forName(telephonyInterface.getClass().getName());
//            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
//
//            // Invoke endCall()
//            methodEndCall.invoke(telephonyInterface);
//
//        } catch (Exception ex) { // Many things can go wrong with reflection calls
//            Log.d(TAG,"PhoneStateReceiver **" + ex.toString());
//            return false;
//        }
//        return true;
//    }
//
//}
