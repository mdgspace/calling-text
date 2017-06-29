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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;


public class BaseActivity extends FragmentActivity implements ActionBar.TabListener, GifFragment.onImageselectionListener {


    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    Button btn_settings;
    public static String mname, mnumber;
    public static Boolean calledByapp = false;
    private TabLayout tabLayout;
    public static HashMap<String,Integer> imageIds;
    SessionManager session;  //session manager class
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
            newFragment.setImage(position);
        }
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
        imageIds=new HashMap<>();
        imageIds.put("1",R.drawable.birthday);
        imageIds.put("2",R.drawable.confused);
        imageIds.put("3",R.drawable.funny);
        imageIds.put("4",R.drawable.embares);
        imageIds.put("5",R.drawable.angry);
        imageIds.put("6",R.drawable.machau);
        imageIds.put("7",R.drawable.sorry);
        imageIds.put("8",R.drawable.hii);
        imageIds.put("9",R.drawable.hello);
        imageIds.put("10",R.drawable.love);
        imageIds.put("11",R.drawable.compliment);
        imageIds.put("12",R.drawable.happy);
        imageIds.put("13",R.drawable.sad);
        imageIds.put("14",R.drawable.crying);
    }


    public static String getMname() {
        return mname;
    }

    public static void setMname(String mname) {
        BaseActivity.mname = mname;
    }

    public static String getMnumber() {
        return mnumber;
    }

    public static void setMnumber(String mnumber) {
        BaseActivity.mnumber = mnumber;
    }

}
