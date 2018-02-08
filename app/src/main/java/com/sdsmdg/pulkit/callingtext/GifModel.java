package com.sdsmdg.pulkit.callingtext;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class GifModel extends BottomSheetDialogFragment implements View.OnClickListener {

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
    ImageButton img11;
    ImageButton img12;
    ImageButton img13;
    ImageButton img14;
    ImageButton img15;
    ImageButton img16;
    ImageButton img17;
    ImageButton img18;
    ImageButton img19;
    ImageButton img20;

    GifModel.onImageselectionListener mCallback;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.gif_fragment, null);
       dialog.setContentView(contentView);
        //((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public interface onImageselectionListener {
        public void onImageSelection(String position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
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
        img11 = (ImageButton) view.findViewById(R.id.imageButton11);
        img12 = (ImageButton) view.findViewById(R.id.imageButton12);
        img13 = (ImageButton) view.findViewById(R.id.imageButton13);
        img14 = (ImageButton) view.findViewById(R.id.imageButton14);
        img15 = (ImageButton) view.findViewById(R.id.imageButton15);
        img16 = (ImageButton) view.findViewById(R.id.imageButton16);
        img17 = (ImageButton) view.findViewById(R.id.imageButton17);
        img18 = (ImageButton) view.findViewById(R.id.imageButton18);
        img19 = (ImageButton) view.findViewById(R.id.imageButton19);
        img20 = (ImageButton) view.findViewById(R.id.imageButton20);

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
        img11.setOnClickListener(this);
        img12.setOnClickListener(this);
        img13.setOnClickListener(this);
        img14.setOnClickListener(this);
        img15.setOnClickListener(this);
        img16.setOnClickListener(this);
        img17.setOnClickListener(this);
        img18.setOnClickListener(this);
        img19.setOnClickListener(this);
        img20.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (GifModel.onImageselectionListener) activity;
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
