package com.sdsmdg.pulkit.callingtext;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class CustomDialogBox extends AppCompatActivity {
    TextView textView;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
        ShapeDrawable rectShapeDrawable = new ShapeDrawable();
        Paint paint = rectShapeDrawable.getPaint();
        paint.setColor(Color.parseColor("#388FF5"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        layout.setBackgroundDrawable(rectShapeDrawable);
       textView = (TextView)findViewById(R.id.textView5);
       gifImageView =(GifImageView)findViewById(R.id.imageView2);
       int id=Integer.parseInt(CallManager.msg.substring(CallManager.msg.lastIndexOf(" ")+1));
        gifImageView.setImageResource(BaseActivity.imageIds.get(String.valueOf(id)));
      textView.setText((CallManager.msg).substring(0,CallManager.msg.lastIndexOf(" ")));
    }
    public void ok(View v)
    {
        finish();
    }
}
