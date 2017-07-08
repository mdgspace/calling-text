package com.sdsmdg.pulkit.callingtext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class    FavouriteFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private View view;
    private List<ArrayList>  favList;
    private FloatingActionButton mFavFab;
    private EditText editText;
    private String nameAddFav;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_favourites,container,false);
        mFavFab=(FloatingActionButton) view.findViewById(R.id.favoriteFab);
        editText = (EditText)view.findViewById(R.id.favAddName);
        mWaveSwipeRefreshLayout= (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getVisibility() == View.VISIBLE)
                {
                    /* Shows the editText if its not already visible so that the user may add any contact to favourite contact*/

                    /* This has to be further enhanced by the implementation of search or predictive hints as the user types the name*/
                    nameAddFav=editText.getText().toString();
                    mFavFab.setImageResource(R.drawable.ic_plus);
                    editText.setVisibility(View.INVISIBLE);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Contacts.STARRED,1);
                    getContext().getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, ContactsContract.Contacts.DISPLAY_NAME+"=?", new String[]{nameAddFav});

                }
                else {
                    editText.setText("");
                    mFavFab.setImageResource(R.drawable.red_tick);
                    editText.setVisibility(View.VISIBLE);
                }

            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.favRecyclerView);
        addToList();

        /*For refreshing the fragment views*/

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addToList();
                new FavouriteFragment.Task().execute();
            }
        });
               return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class Task extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    /* This method generates the list of favourite contacts, this list changes with changes in default contact apps*/

    public List<ArrayList> createFavouriteList(){
        favList= new ArrayList<>();
        ArrayList<String> arrayList=new ArrayList<>();
        Cursor favPhones= getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "starred=?", new String[]{"1"}, null);
        while (favPhones.moveToNext()){
            String name = favPhones.getString(favPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = favPhones.getString(favPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            arrayList.add(name);
            arrayList.add(number);
            favList.add(arrayList);
            arrayList =new ArrayList<>();
        }
        favPhones.close();
        return favList;
    }

    /* Method for setting adapter*/

    public void addToList(){
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new FavouriteAdapter(getContext(), createFavouriteList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
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

