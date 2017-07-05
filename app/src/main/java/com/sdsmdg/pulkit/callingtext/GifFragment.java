package com.sdsmdg.pulkit.callingtext;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class GifFragment extends DialogFragment implements View.OnClickListener {
    ImageButton img1;
    ImageButton img2;
    ImageButton img3;
    ImageButton img4;
    ImageButton img5;
    ImageButton img6;
    ImageButton img7;
    ImageButton img8;
    ImageButton img9;
    ImageButton img10;
    onImageselectionListener mCallback;

    public interface onImageselectionListener {
        public void onImageSelection(String position);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gif_fragment, container, false);
        img1 = (ImageButton) view.findViewById(R.id.imageButton);
        img2 = (ImageButton) view.findViewById(R.id.imageButton2);
        img3 = (ImageButton) view.findViewById(R.id.imageButton3);
        img4 = (ImageButton) view.findViewById(R.id.imageButton4);
        img5 = (ImageButton) view.findViewById(R.id.imageButton5);
        img6 = (ImageButton) view.findViewById(R.id.imageButton6);
        img7 = (ImageButton) view.findViewById(R.id.imageButton7);
        img8 = (ImageButton) view.findViewById(R.id.imageButton8);
        img9 = (ImageButton) view.findViewById(R.id.imageButton9);
        img10 = (ImageButton) view.findViewById(R.id.imageButton10);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        img8.setOnClickListener(this);
        img9.setOnClickListener(this);
        img10.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (onImageselectionListener) activity;
        } catch (ClassCastException e) {
            Log.e("Error", e.getMessage());
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        mCallback.onImageSelection(v.getTag()+"");
        getActivity().onBackPressed();
    }
}