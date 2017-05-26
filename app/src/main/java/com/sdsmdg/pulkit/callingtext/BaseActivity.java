package com.sdsmdg.pulkit.callingtext;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements ActionBar.TabListener,GifFragment.onImageselectionListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    FragmentManager fragmentManager;
    GifFragment fragment;
    Button btn_settings;
    public static String mname,mnumber;
    public static Boolean calledByapp = false;

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

    public static String receiver="7248187747";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        /*
        btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(BaseActivity.this,Settings.class);
                startActivity(o);
            }
        });
        */

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Log.e("MY BA NO.","PHONE NO."+mPhoneNumber);
        viewPager = (ViewPager) findViewById(R.id.pager);
//        actionBar = getActionBar();
/*
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
*/
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        int pg_number = 0;
        viewPager.setAdapter(mAdapter);
//        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("NUMBER", "7248187747")!=null){
//            receiver = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("NUMBER", "7248187747");
//        }

        if(getIntent().getExtras()!= null){
            try {
                pg_number = Integer.parseInt(getIntent().getExtras().getString("pagenumber"));

            }catch (NumberFormatException num){
                Log.i("EXCEpTION",num.toString());
            }
        }
        viewPager.setCurrentItem(pg_number);

       /* actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);*/

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        startService(new Intent(this, BackgroundService.class));
        }
    private void call(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        try{
            startActivity(callIntent);}
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(BaseActivity.this,"yourActivity is not found",Toast.LENGTH_SHORT).show();}
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
        Log.e("in Imageselection","in !!!!");
        Log.e("in null",getSupportFragmentManager().getFragments().get(0).getTag());
        NewFragment newFragment = (NewFragment)
                getSupportFragmentManager().getFragments().get(2
                );

        if (newFragment != null) {
          Log.e("in null","in null");
            newFragment.setImage(position);
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }
    */
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.action_settings_icon:

                // Direct the user to the Settings Activity class
                Intent settingsActivityIntent = new Intent(BaseActivity.this,Settings.class);
                startActivity(settingsActivityIntent);
                break;

            case R.id.action_search_icon:


                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }
    */
}
