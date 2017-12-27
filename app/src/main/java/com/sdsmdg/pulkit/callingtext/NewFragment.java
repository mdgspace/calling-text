package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class NewFragment extends Fragment implements View.OnClickListener {
    private final int REQUEST_CODE = 1;
    EditText editText1;
    EditText editText2;
    String yourNumber, yourName;
    String receiver;
    String name;
    GifImageView img;
    public static FrameLayout fl, fl2;
    RelativeLayout rl;
    Boolean press = false;
    android.support.v4.app.FragmentManager fragmentManager;
    TextView t1;
    View view;
    ImageButton call;
   public static int gifNumber1;
    private static final int CONTACTS_LOADER_ID = 1;
    private WindowManager windowManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_fragment, container, false);
        editText1 = (EditText) view.findViewById(R.id.editText2); //number
        editText2 = (EditText) view.findViewById(R.id.editText);//message
        yourNumber = "7248187747";
        t1 = (TextView) view.findViewById(R.id.textView5);
        img = (GifImageView) view.findViewById(R.id.imageView3);
        if (BaseActivity.mNumber != null) {
            Log.i("Number selected ", BaseActivity.mNumber);
            editText1.setText(BaseActivity.mNumber);
        }

        fl = (FrameLayout) view.findViewById(R.id.color);
        fl2 = (FrameLayout) view.findViewById(R.id.bottom);
        rl = (RelativeLayout) view.findViewById(R.id.my_layout);
        img.setOnClickListener(this);


        call = (ImageButton) view.findViewById(R.id.button4);
        call.setOnClickListener(this);
        fl.setAlpha(0);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.button4:
                final Animation myAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounce);

                // Using bounce interpolator with amplitude 0.4 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.4, 20);
                myAnim.setInterpolator(interpolator);

                v.startAnimation(myAnim);
                if (haveNetworkConnection()) {
                    if (editText2.getText().toString() != null && editText1.getText().toString() != null) {

                        BackGroundWorker b = new BackGroundWorker(getActivity(), 2);
                        Log.e("number", editText1.getText().toString());

                        b.execute(yourNumber, editText1.getText().toString(), editText2.getText().toString(), String.valueOf(gifNumber1));
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        BaseActivity.calledByapp = true;
                        callIntent.setData(Uri.parse("tel:" + editText1.getText().toString()));
                        Log.e("receiver", "tel:" + editText1.getText().toString());
                        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(BackGroundWorker.value));
                        Cursor phones = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                        while (phones.moveToNext()) {
                            name = phones.getString(phones.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        }
                        CallerDetails cd = new CallerDetails(name,editText1.getText().toString(),editText2.getText().toString(),"outgoing", String.valueOf(new Date().getTime()),null);
                        DataBaseHandler dbh=DataBaseHandler.getInstance(getContext());
                        dbh.addCaller(cd);
                        startActivity(callIntent);
                    }else {
                        Log.e("in else", "in else");
                        Toast.makeText(getActivity(), "please type your message or number", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getActivity(), "you have no internet connection", Toast.LENGTH_SHORT).show();
                Log.e("call", "call");

                break;

                case R.id.imageView3:
                    BottomSheetDialogFragment bottomSheetDialogFragment = new GifModel();
                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
//                if (press) {
                  //  fl.setAlpha(0.5f);
                   // call.setVisibility(View.INVISIBLE);

                    this.getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .commit();
                    press = !press;

                break;

            default:
                break;

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void setImage(int gifNumber) {
        fl.setAlpha(0);
       gifNumber1 = gifNumber;
        img.setImageResource(BaseActivity.imageIds[gifNumber1-1]);
        call.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.other_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_settings_icon:
                Intent settingsActivityIntent = new Intent(getActivity(), Settings.class);
                startActivity(settingsActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}