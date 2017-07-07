package com.sdsmdg.pulkit.callingtext;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class BaseActivity extends AppCompatActivity implements ActionBar.TabListener,GifFragment.onImageselectionListener, ContactListFragment.OnContactsLoaded {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    public android.support.v7.app.ActionBar actionBar;
    FragmentManager fragmentManager;
    GifFragment fragment;
    public static String mName, mNumber;
    public static Boolean calledByapp = false;
    public static List<ArrayList> savedContacts;
    public static ArrayList<PhoneContact> savedPhoneContacts;
    //session manager class
    SessionManager session;
    private TabLayout tabLayout;
    public static int[] imageIds;
    public static String receiver = "7248187747";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //to initialise all the global variables
        initVariables();
        session.checkLogIn();//check login session
        setListenersAndAdapters();
        setImageIds();

        // To retain the contact list when the device is rotated
        if (savedInstanceState != null){
            savedPhoneContacts = savedInstanceState.getParcelableArrayList("phoneContactsList");
            for(PhoneContact phoneContact : savedPhoneContacts){
                ArrayList<String> a =new ArrayList<>();
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

    private void call(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        try {
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
        Log.e("in Imageselection", "in !!!!");
        Log.e("in null", getSupportFragmentManager().getFragments().get(0).getTag());
        NewFragment newFragment = (NewFragment)
                getSupportFragmentManager().getFragments().get(2
                );

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("phoneContactsList", savedPhoneContacts);
    }

    /**
     * This function sets up adapters and click listeners
     */
    private void setListenersAndAdapters(){
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(BaseActivity.this, Settings.class);
                startActivity(settingsIntent);
            }
        });

        viewPager.setAdapter(mAdapter);

    }

    /**
     * This function initializes various new variables
     */
    private void initVariables(){
        //session class instance
        session = new SessionManager(getApplicationContext());
        btn_settings = (Button) findViewById(R.id.btn_settings);
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
