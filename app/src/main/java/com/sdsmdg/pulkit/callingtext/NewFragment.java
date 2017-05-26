package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class NewFragment extends Fragment implements View.OnClickListener {
    private final int REQUEST_CODE = 1;
    EditText editText1;
    EditText editText2;
    //    public EditText editName;
    String yourNumber, yourName;
    String receiver;
    String name;
    GifImageView img;
    public static FrameLayout fl, fl2;
    RelativeLayout rl;
    Boolean press = false;
    android.support.v4.app.FragmentManager fragmentManager;
    TextView t1;
    GifFragment fragment;
    View view;
    Button call;
    public static String gifNumber1;
    private static final int CONTACTS_LOADER_ID = 1;
    private WindowManager windowManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        View view = inflater.inflate(R.layout.new_fragment, container, false);
        editText1 = (EditText) view.findViewById(R.id.editText2); //number
        editText2 = (EditText) view.findViewById(R.id.editText);//message
//        editName = (EditText) view.findViewById(R.id.editText3); //name
        yourNumber = "7253046197";
        t1 = (TextView) view.findViewById(R.id.textView5);
        img = (GifImageView) view.findViewById(R.id.imageView3);
        if (BaseActivity.mnumber != null) {
            Log.i("Number selected ", BaseActivity.mnumber);
            editText1.setText(BaseActivity.mnumber);
        }

        fl = (FrameLayout) view.findViewById(R.id.color);
        fl2 = (FrameLayout) view.findViewById(R.id.bottom);
        rl = (RelativeLayout) view.findViewById(R.id.my_layout);
        img.setOnClickListener(this);
        call = (Button) view.findViewById(R.id.button4);
        call.setOnClickListener(this);
        fl.setAlpha(0);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.button4:
                if (haveNetworkConnection()) {
                    if (editText2.getText().toString() != null && editText1.getText().toString() != null) {

                        BackGroundWorker b = new BackGroundWorker(getActivity(), 2);
                        Log.e("number", editText1.getText().toString());
                        b.execute(yourNumber, editText1.getText().toString(), editText2.getText().toString(), gifNumber1);
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        BaseActivity.calledByapp = true;
                        callIntent.setData(Uri.parse("tel:" + editText1.getText().toString()));
                        Log.e("receiver", "tel:" + editText1.getText().toString());
                        /*
                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(BackGroundWorker.value));
                        Cursor phones = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                        while (phones.moveToNext()) {
                            name = phones.getString(phones.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        }
                        CallerDetails cd = new CallerDetails(name,editText1.getText().toString(),editText2.getText().toString(),"outgoing", String.valueOf(new Date().getTime()));
                        DataBaseHandler dbh=DataBaseHandler.getInstance(getContext());
                        dbh.addCaller(cd);
*/
                        // Find the current status of the user being called
                        String callerStatus = searchCallerStatus(editText1.getText().toString());

                        // Show a dialog to the user to ask him if he wants to make the call
                        if(callerStatus != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(callerStatus).setTitle("Receiver's Present Status")
                                    .setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(callIntent);
                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else {
                            startActivity(callIntent);
                        }

                    } else {
                        Log.e("in else", "in else");
                        Toast.makeText(getActivity(), "please type your message or number", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getActivity(), "you have no internet connection", Toast.LENGTH_SHORT).show();
                Log.e("call", "call");

                break;
            case R.id.imageView3:
                GifFragment gifFragment = new GifFragment();
//                if (press) {
                    fl.setAlpha(0.5f);
                    call.setVisibility(View.INVISIBLE);
                    this.getFragmentManager().beginTransaction()
                            .replace(R.id.bottom, gifFragment, null)
                            .addToBackStack(null)
                            .commit();
                    press = !press;
//                } else {
//                    fl.setAlpha(0);
//                    this.getFragmentManager().beginTransaction()
//                            .detach(gifFragment)
//                            .addToBackStack(null)
//                            .commit();
//                    press = !press;
//                }


                break;
            default:
                break;

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String searchCallerStatus(final String callerNumber) {

        final String[] callerStatus = new String[1];
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("users_status");
        DatabaseReference usersStatusReference = dr.child(callerNumber);
        usersStatusReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callerStatus[0] = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        return callerStatus[0];
    }

    public void setImage(String gifNumber) {
        fl.setAlpha(0);
        Log.e("pul", "pul");
        gifNumber1 = gifNumber;

        call.setVisibility(View.VISIBLE);
        switch (gifNumber) {
            case "1":
                Log.e("in 1", "in 1");
                img.setImageResource(R.drawable.birthday);
                break;
            case "2":
                img.setImageResource(R.drawable.confused);
                break;
            case "3":
                img.setImageResource(R.drawable.funny);
                break;
            case "4":
                img.setImageResource(R.drawable.embares);
                break;
            case "5":
                img.setImageResource(R.drawable.angry);
                break;
            case "6":
                img.setImageResource(R.drawable.machau);
                break;
            case "7":
                img.setImageResource(R.drawable.sorry);
                break;
            case "8":
                img.setImageResource(R.drawable.hii);
                break;
            case "9":
                img.setImageResource(R.drawable.hello);
                break;
            case "10":
                img.setImageResource(R.drawable.love);
                break;
            case "11":
                img.setImageResource(R.drawable.compliment);
                break;
            case "12":
                img.setImageResource(R.drawable.happy);
                break;
            case "13":
                img.setImageResource(R.drawable.sad);
                break;
            case "14":
                img.setImageResource(R.drawable.crying);
                break;
            case "15":
                img.setImageResource(R.drawable.worried);
                break;
            case "16":
                img.setImageResource(R.drawable.praying);
                break;
            case "17":
                img.setImageResource(R.drawable.smoking);
                break;
            case "18":
                img.setImageResource(R.drawable.birthday);
                break;
            case "19":
                img.setImageResource(R.drawable.birthday);
                break;
            case "20":
                img.setImageResource(R.drawable.envy);
                break;
            default:
                img.setImageResource(R.drawable.birthday);
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_icon) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        return true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("NewFragment", "Detached");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("NewFragment", "Attached");
//        editName.setText(BaseActivity.getMname());


    }
}