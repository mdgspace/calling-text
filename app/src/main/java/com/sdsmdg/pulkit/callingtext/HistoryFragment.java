package com.sdsmdg.pulkit.callingtext;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class HistoryFragment extends Fragment {

    RecyclerView recList;
    DataBaseHandler dbh;
    View view;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_history, container, false);
        recList = (RecyclerView) view.findViewById(R.id.history_recycler);
        dbh = DataBaseHandler.getInstance(getContext());
        addToList();
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addToList();
                new HistoryFragment.Task().execute();
            }
        });
        return view;
    }

    private List<CallerDetails> createList() {
        List<CallerDetails> result;
        result = dbh.getAllCallers();
        Log.e("result", result + "");
        return result;
    }


    /**
     * This function adds various calls in the history segment to the list
     */
    public void addToList() {

        recList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(linearLayoutManager);
        CallListAdapter callListAdapter = new CallListAdapter(createList(), getActivity(), recList);
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(callListAdapter);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recList.setAdapter(alphaAdapter);
    }

    private class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }


}
