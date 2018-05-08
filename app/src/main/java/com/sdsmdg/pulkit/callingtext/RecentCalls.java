package com.sdsmdg.pulkit.callingtext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class RecentCalls extends AppCompatActivity {

    private DataBaseHandler dataBaseHandler;
    private RecyclerView recyclerView;
    private View view;
    private String number= "9876543210";
    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_calls);
        nameView= (TextView) findViewById(R.id.callerName);
        number=(String) getIntent().getExtras().get("Number");
        Toast.makeText(getApplicationContext(),number,Toast.LENGTH_LONG).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_caller_details);
        dataBaseHandler = DataBaseHandler.getInstance(getApplicationContext());
        addToList();
       // nameView.setText(dataBaseHandler.getName(number));
        //nameView is currently not working
    }

    /*This method generates a list that has calls from a particular number*/
    private List<CallerDetails> createListByNumber() {
        List<CallerDetails> result;
        result=dataBaseHandler.getAllCallsByNumber(number);
        return result;
    }
    /*THis method sets the adapter for recycler view and also adds animation to the card*/
    public void addToList(){

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecentCallsAdapter recentCallsAdapter = new RecentCallsAdapter(createListByNumber(),getParent(),recyclerView);
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(recentCallsAdapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recyclerView.setAdapter(alphaAdapter);
    }

}
