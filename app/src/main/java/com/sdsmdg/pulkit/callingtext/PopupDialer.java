package com.sdsmdg.pulkit.callingtext;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class PopupDialer extends AppCompatActivity implements GifFragment.onImageselectionListener {

    public static String gifNumber1 = "1";
    public static final String TAG = "POPUP";
    EditText ediTextMessage;
    GifImageView gifImageView;
    GifFragment fragment;
    Button call;
    RelativeLayout mRelativeLayout;
    String number, message, yourNumber;

    @Override
    public void onImageSelection(String position) {
        setImage(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        initVariables();
        setClickListeners();

        Log.i("number and message ", message + ":" + number);
        yourNumber = "7248187747";
    }

    /**
     * sets images to the gif views
     * @param gifNumber The number of the gif
     */
    public void setImage(String gifNumber) {
        Log.e("aj", "aj");
        gifNumber1 = gifNumber;

        call.setVisibility(View.VISIBLE);
        switch (gifNumber) {
            case "1":
                Log.e("in 1", "in 1");
                gifImageView.setImageResource(R.drawable.birthday);
                break;
            case "2":
                gifImageView.setImageResource(R.drawable.confused);
                break;
            case "3":
                gifImageView.setImageResource(R.drawable.funny);
                break;
            case "4":
                gifImageView.setImageResource(R.drawable.embares);
                break;
            case "5":
                gifImageView.setImageResource(R.drawable.angry);
                break;
            case "6":
                gifImageView.setImageResource(R.drawable.machau);
                break;
            case "7":
                gifImageView.setImageResource(R.drawable.sorry);
                break;
            case "8":
                gifImageView.setImageResource(R.drawable.hii);
                break;
            case "9":
                gifImageView.setImageResource(R.drawable.hello);
                break;
            case "10":
                gifImageView.setImageResource(R.drawable.love);
                break;
            case "11":
                gifImageView.setImageResource(R.drawable.compliment);
                break;
            case "12":
                gifImageView.setImageResource(R.drawable.happy);
                break;
            case "13":
                gifImageView.setImageResource(R.drawable.sad);
                break;
            case "14":
                gifImageView.setImageResource(R.drawable.crying);
                break;
            case "15":
                gifImageView.setImageResource(R.drawable.worried);
                break;
            case "16":
                gifImageView.setImageResource(R.drawable.praying);
                break;
            case "17":
                gifImageView.setImageResource(R.drawable.smoking);
                break;
            case "18":
                gifImageView.setImageResource(R.drawable.birthday);
                break;
            case "19":
                gifImageView.setImageResource(R.drawable.birthday);
                break;
            case "20":
                gifImageView.setImageResource(R.drawable.envy);
                break;
            default:
                gifImageView.setImageResource(R.drawable.birthday);
        }
    }


    /**
     * to check whether the phone has an ctive network connection
     *
     * @return true if there is a network connection else false
     */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
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

    /**
     * This method initialises various variables
     */
    private void initVariables() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.popup_rl);
        ediTextMessage = (EditText) findViewById(R.id.message);
        call = (Button) findViewById(R.id.btn_call);
        gifImageView = (GifImageView) findViewById(R.id.gif_image);
        number = getIntent().getExtras().getString("number");
    }


    /**
     * This method sets click listeners for various touch events.
     */
    private void setClickListeners() {

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()) {
                    final ComponentName component = new ComponentName(getBaseContext(), CallManager.class);
                    int status = getBaseContext().getPackageManager().getComponentEnabledSetting(component);
                    if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                        Log.e("POPOp", "receiver is enabled");
                    } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                        Log.e("POPop", "receiver is disabled");
                    } else {
                        Log.e("POPop", "receiver is nooooone");

                    }
                    //to disable
                    getBaseContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    message = ediTextMessage.getText().toString();
                    if (message.isEmpty() && number != null) {
                        BackGroundWorker b = new BackGroundWorker(PopupDialer.this, 2);
                        b.execute(yourNumber, number, message, gifNumber1);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        try {
                            startActivity(callIntent);

                        } catch (SecurityException s) {
                            Log.e("exception", s.toString());
                        }
                    } else {
                        Toast.makeText(PopupDialer.this, "please type your message or number", Toast.LENGTH_SHORT).show();

                    }
                    //to enable
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here
                                    getBaseContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                                }
                            },
                            10000
                    );

                    finish();

                } else {
                    Toast.makeText(PopupDialer.this, "you have no internet connection", Toast.LENGTH_SHORT).show();
                    finish();

                }
                Log.e("call", "call");
            }
        });


        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.setVisibility(View.INVISIBLE);
                GifFragment gifFragment = new GifFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottom2, gifFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

}
