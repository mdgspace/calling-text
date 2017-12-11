package com.sdsmdg.pulkit.callingtext;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;


public class Splashactivity extends AppCompatActivity {

    int PERMISSIONS_REQUEST_CODE = 1;
    SessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_splashactivity);
        //session class instance
        session = new SessionManager(getApplicationContext());


        Log.e("Splash","INININ");
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {
                            // your code here
                           if(session.isLoggedIn()){
                               Intent i = new Intent(Splashactivity.this, BaseActivity.class);

                               startActivity(i);
                               finish();
                           }
                           else{
                               Intent loginActivityIntent = new Intent(Splashactivity.this, LoginActivity.class);
                               startActivity(loginActivityIntent);
                               finish();
                           }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allGranted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
        }
        if (allGranted) {
            startHomeActivity();
        } else {
            Toast.makeText(this, "Please grant the requested permissions.", Toast.LENGTH_SHORT).show();
            startHomeActivity();
        }
    }

    public void startHomeActivity() {
        if(session.isLoggedIn()){
            Intent i = new Intent(Splashactivity.this, BaseActivity.class);

            startActivity(i);
            finish();
        }
        else{
            Intent loginActivityIntent = new Intent(Splashactivity.this, LoginActivity.class);
            startActivity(loginActivityIntent);
            finish();
        }
    }

    public void requestPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.PROCESS_OUTGOING_CALLS

        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
    }

}
