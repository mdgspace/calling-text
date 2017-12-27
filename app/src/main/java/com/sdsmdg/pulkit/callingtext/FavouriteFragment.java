package com.sdsmdg.pulkit.callingtext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class    FavouriteFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private View view;
    private List<FavContact> favList;
    private FloatingActionButton mFavFab;
    private EditText editText;
    private String nameAddFav;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    AutoCompleteTextView searchBox;
    FrameLayout dimLayoutfav;
    LinearLayout searchLayoutfav;
    private ImageView backButtonfav;
    int color = Color.parseColor("#000080");

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_favourites,container,false);
        favList=createFavouriteList(); //to store all the contacts marked as favourites.
        FavAdapter adapter = new FavAdapter(getContext(), favList);

        searchBox = (AutoCompleteTextView) view.findViewById(R.id.searchboxfav);
        searchBox.setAdapter(adapter);
        searchBox.setThreshold(1); //to show the search suggestions when the user has entered 1 character in the search bar

        mFavFab=(FloatingActionButton) view.findViewById(R.id.favoriteFab);
        dimLayoutfav = (FrameLayout) view.findViewById(R.id.dim_layoutfav);
        searchLayoutfav = (LinearLayout) view.findViewById(R.id.searchbarfav);
        backButtonfav = (ImageView) view.findViewById(R.id.backbuttonfav);
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
                    editText.requestFocus();
                    mFavFab.setImageResource(R.drawable.tick1);
                    editText.setVisibility(View.VISIBLE);
                }

            }
        });
        mFavFab.setRippleColor(color);

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

        // Back button to request the focus back to the ContactsListFragment
        backButtonfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDimLayout();
            }
        });

        // Dim layout which is present during search operations
        dimLayoutfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDimLayout();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    private void removeDimLayout() {
        dimLayoutfav.setVisibility(View.GONE);
        searchLayoutfav.setVisibility(View.GONE);
        searchBox.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
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

    public List<FavContact> createFavouriteList(){
        favList= new ArrayList<FavContact>();
       // ArrayList<String> arrayList=new ArrayList<>();
        FavContact contact = new FavContact();
        Cursor favPhones= getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "starred=?", new String[]{"1"}, null);

        while (favPhones.moveToNext()){
            String name = favPhones.getString(favPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = favPhones.getString(favPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact.setName(name);
            contact.setNumber(number);
            favList.add(contact);
            contact =new FavContact();
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
        inflater.inflate(R.menu.contact_list_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_settings_icon:
                Intent settingsActivityIntent = new Intent(getActivity(), Settings.class);
                startActivity(settingsActivityIntent);
                return true;

            case R.id.action_search_icon:
                dimLayoutfav.setVisibility(View.VISIBLE);
                searchLayoutfav.setVisibility(View.VISIBLE);
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                searchBox.requestFocus();
                searchBox.showDropDown();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}

