package com.sdsmdg.pulkit.callingtext;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class PopupDialer extends AppCompatActivity implements GifFragment.onImageselectionListener{

    EditText et_message;
    GifImageView g;
    GifFragment fragment;
    Button call;
    RelativeLayout rl;
    public static String gifNumber1 = "1";
    String number, message, yourNumber;
    public String TAG = "POPUP";

    @Override
    public void onImageSelection(String position) {
            setImage(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        rl = (RelativeLayout)findViewById(R.id.popup_rl);
        et_message = (EditText)findViewById(R.id.message);
        call = (Button) findViewById(R.id.btn_call);
        g = (GifImageView) findViewById(R.id.gif_image);
        number = getIntent().getExtras().getString("number");
        Log.i("number and message ", message + ":" + number);
        yourNumber = "7248187747";


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("CALLL CLICK ", yourNumber + "" + number + "" + message);
                if (haveNetworkConnection() == true) {
                    final  ComponentName component = new ComponentName(getBaseContext(), CallManager.class);
                    int status = getBaseContext().getPackageManager().getComponentEnabledSetting(component);
                    Log.e("INTSTATUS", status + "nknks");
                    if (status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                        Log.e("POPOp", "receiver is enabled");
                    } else if (status == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
                        Log.e("POPop", "receiver is disabled");
                    } else {
                        Log.e("POPop", "receiver is nooooone");

                    }
                    //to disable
                    getBaseContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    Log.e("POPop", "disable receiver");
                    message = et_message.getText().toString();
                    if (message != null && number != null) {
                        Log.i("REACHED HERe", "YEAAA !!");
                        BackGroundWorker b = new BackGroundWorker(PopupDialer.this, 2);
                        Log.e("number", number+"message:"+message);
                        b.execute(yourNumber, number, message, gifNumber1);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        Log.e("receiver", "tel:" + number);
                        try {
                            Log.i("Reached Here!","YES inside try");
                            startActivity(callIntent);

                        } catch (SecurityException s) {
                            Log.e("exception", s.toString());

                        }
                    } else {
                        Log.e("in else", "in else");
                        Toast.makeText(PopupDialer.this, "please type your message or number", Toast.LENGTH_SHORT).show();

                    }
                    //to enable
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    // your code here
                                    getBaseContext().getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                                    Log.e("POPop", "enabled receiver");
                                }
                            },
                            10000
                    );

                    finish();
                    Log.e("FINSIHSSS", "FINNNSHS HIIm");

                } else {
                    Toast.makeText(PopupDialer.this, "you have no internet connection", Toast.LENGTH_SHORT).show();
                    finish();
                    Log.e("FINSIHSSS", "FINNNSIISHS HIIm");

                }


                Log.e("call", "call");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                params.width =320;
//                params.height =500;
//                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                rl.setLayoutParams(params);

                call.setVisibility(View.INVISIBLE);
                GifFragment gifFragment = new GifFragment();
//                FragmentManager fm = getFragmentManager();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.bottom2, gifFragment)
                        .addToBackStack(null)
                        .commit();
//                gifFragment.show(getSupportFragmentManager(),TAG);


//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.add(gifFragment, null);
//                ft.commit();

            }
        });


    }

    public void setImage(String gifNumber) {
        Log.e("aj", "aj");
        gifNumber1 = gifNumber;

        call.setVisibility(View.VISIBLE);
        switch (gifNumber) {
            case "1":
                Log.e("in 1", "in 1");
                g.setImageResource(R.drawable.birthday);
                break;
            case "2":
                g.setImageResource(R.drawable.confused);
                break;
            case "3":
                g.setImageResource(R.drawable.funny);
                break;
            case "4":
                g.setImageResource(R.drawable.embares);
                break;
            case "5":
                g.setImageResource(R.drawable.angry);
                break;
            case "6":
                g.setImageResource(R.drawable.machau);
                break;
            case "7":
                g.setImageResource(R.drawable.sorry);
                break;
            case "8":
                g.setImageResource(R.drawable.hii);
                break;
            case "9":
                g.setImageResource(R.drawable.hello);
                break;
            case "10":
                g.setImageResource(R.drawable.love);
                break;
            case "11":
                g.setImageResource(R.drawable.compliment);
                break;
            case "12":
                g.setImageResource(R.drawable.happy);
                break;
            case "13":
                g.setImageResource(R.drawable.sad);
                break;
            case "14":
                g.setImageResource(R.drawable.crying);
                break;
            case "15":
                g.setImageResource(R.drawable.worried);
                break;
            case "16":
                g.setImageResource(R.drawable.praying);
                break;
            case "17":
                g.setImageResource(R.drawable.smoking);
                break;
            case "18":
                g.setImageResource(R.drawable.birthday);
                break;
            case "19":
                g.setImageResource(R.drawable.birthday);
                break;
            case "20":
                g.setImageResource(R.drawable.envy);
                break;
            default:
                g.setImageResource(R.drawable.birthday);
        }
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
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

}
