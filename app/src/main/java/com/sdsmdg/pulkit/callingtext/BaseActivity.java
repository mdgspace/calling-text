package com.sdsmdg.pulkit.callingtext;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements ActionBar.TabListener, GifModel.onImageselectionListener, ContactListFragment.OnContactsLoaded {

    int PERMISSIONS_REQUEST_CODE = 1;

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    public android.support.v7.app.ActionBar actionBar;
    public static String mName, mNumber, TAG;
    public static Boolean calledByapp = false;
    public static List<ArrayList> savedContacts;
    public static ArrayList<PhoneContact> savedPhoneContacts;
    //session manager class
    SessionManager session;
    public static TabLayout tabLayout;
    public static int[] imageIds;
    public static String receiver = "7248187747";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //to initialise all the global variables
        initVariables();

        TAG="Splash";
        Log.e(TAG, "INININ");
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions();
        } else {
            session.checkLogIn();
        }

        setListenersAndAdapters();
        setImageIds();

        //to check if permission for call is available
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // To retain the contact list when the device is rotated
        if (savedInstanceState != null) {
            savedPhoneContacts = savedInstanceState.getParcelableArrayList("phoneContactsList");
            for (PhoneContact phoneContact : savedPhoneContacts) {
                ArrayList<String> a = new ArrayList<>();
                a.add(phoneContact.name);
                a.add(phoneContact.phone);
                savedContacts.add(a);
            }
        }

        TelephonyManager telephoneManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        String mPhoneNumber = telephoneManager.getLine1Number();
        Log.e("MY BA NO.", "PHONE NO." + mPhoneNumber);

        int pg_number = 0;

        if (getIntent().getExtras() != null) {
            try {
                pg_number = Integer.parseInt(getIntent().getExtras().getString("pagenumber"));

            } catch (NumberFormatException num) {
                Log.i("EXCEpTION", num.toString());
            }
        }
        viewPager.setCurrentItem(pg_number);

        tabLayout.setupWithViewPager(viewPager);
        startService(new Intent(this, BackgroundService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("lololo", "took permissions");

        boolean allGranted = true;

        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Log.d("lololo", String.valueOf(grantResult));
                    allGranted = false;
                    break;
                }
            }
        }
        if (allGranted) {
            session.checkLogIn();//check login session
        } else {
            Toast.makeText(this, "Please grant the requested permissions.", Toast.LENGTH_SHORT).show();
            session.checkLogIn();//check login session
        }
    }

    private void call(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        try {
            //to check if permission for call is available
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BaseActivity.this, "yourActivity is not found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onImageSelection(String position) {
        NewFragment newFragment = null;
        Log.e("in Imageselection", "in !!!!");
        int n = getSupportFragmentManager().getFragments().toArray().length;
        for (int i = n-1; i >= 0; i--){
            String classname = "" + getSupportFragmentManager().getFragments().get(i).getClass();
            if (classname.contains("NewFragment")){
                newFragment = (NewFragment) getSupportFragmentManager().getFragments().get(i);
                break;
            }
        }

        if (newFragment != null) {
            Log.e("in null", "in null");
            newFragment.setImage(Integer.parseInt(position));
        }
    }



    @Override
    public void saveContacts(List<ArrayList> contactsList, ArrayList<PhoneContact> phoneContacts) {
        savedContacts = contactsList;
        savedPhoneContacts = phoneContacts;
    }

   //this function asks for certain permissions from the user
    public void requestPermissions() {
        String[] permissions = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                android.Manifest.permission.PROCESS_OUTGOING_CALLS
        };
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("phoneContactsList", savedPhoneContacts);
    }

    /**
     * This function sets up adapters and click listeners
     */
    private void setListenersAndAdapters(){
        viewPager.setAdapter(mAdapter);
    }

    /**
     * This function initializes various new variables
     */
    private void initVariables(){
        //session class instance
        session = new SessionManager(this);
        viewPager = (ViewPager) findViewById(R.id.pager);

        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    /**
     * This function sets up the image ids.
     */
    private void setImageIds(){
        imageIds=new int[19];
        imageIds[0] = R.drawable.birthday;
        imageIds[1] = R.drawable.confused;
        imageIds[2] = R.drawable.funny;
        imageIds[3] = R.drawable.embares;
        imageIds[4] = R.drawable.angry;
        imageIds[5] = R.drawable.machau;
        imageIds[6] = R.drawable.sorry;
        imageIds[7] = R.drawable.hii;
        imageIds[8] = R.drawable.hello;
        imageIds[9] = R.drawable.love;
        imageIds[10] = R.drawable.compliment;
        imageIds[11] = R.drawable.happy;
        imageIds[12] = R.drawable.sad;
        imageIds[13] = R.drawable.crying;
        imageIds[14] = R.drawable.worried;
        imageIds[15] = R.drawable.praying;
        imageIds[16] = R.drawable.smoking;
        imageIds[17] = R.drawable.birthday;
        imageIds[18] = R.drawable.envy;
    }

    public static String getmName() {
        return mName;
    }

    public static void setmName(String mName) {
        BaseActivity.mName = mName;
    }

    public static String getmNumber() {
        return mNumber;
    }

    public static void setmNumber(String mNumber) {
        BaseActivity.mNumber = mNumber;
    }
  
    public static List<ArrayList> getSavedContacts() {
        return savedContacts;
    }

    public static ArrayList<PhoneContact> getSavedPhoneContacts(){
        return savedPhoneContacts;
    }


}
