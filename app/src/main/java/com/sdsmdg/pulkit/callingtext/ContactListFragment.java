package com.sdsmdg.pulkit.callingtext;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public RecyclerView recList;
    public ImageButton button1;
    Button button;
    EditText et1;
    List<ArrayList> result;
    View view;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_contact_list, container, false);
        button1 = (ImageButton) view.findViewById(R.id.imageButton21);
        et1 = (EditText) view.findViewById(R.id.editText3);
        recList = (RecyclerView) view.findViewById(R.id.questionList_recycler);
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et1.getText().toString().equals("")) {
                    addToList();
                } else {
                    searchTtem(et1.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      /*  VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)view.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recList);
        recList.setOnScrollListener(fastScroller.getOnScrollListener());*/
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Log.e("p", createList() + "");
        ContactListAdapter ca = new ContactListAdapter(createList(), getActivity(),
                new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Log.i("OnClick", "inside the onclick of the adapter");
                Intent i = new Intent(getContext(), BaseActivity.class);
                i.putExtra("pagenumber", "2");
                Log.e("OnClick", "internt set !! ");
                startActivity(i);
                Log.e("OnClick", "finisherererer");
                getActivity().finish();


            }
        });
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(ca);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recList.setAdapter(alphaAdapter);

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new Task().execute();
            }
        });
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = ContactsContract.RawContacts.CONTENT_URI;
        Log.e("pul", "in loader");
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("pul", "in loadFinished");
        Log.e("pul", cursor.getCount() + " ");
        cursor.moveToFirst();
        StringBuilder res = new StringBuilder();
        while (!cursor.isAfterLast()) {
            Log.e("pul", "in while");
            res.append("\n" + cursor.getString(21) + "-" + cursor.getString(22));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<ArrayList> createList() {
        result = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            a.add(name);
            a.add(phoneNumber);
            result.add(a);
            a = new ArrayList<>();

        }
        phones.close();
        return result;
    }

    public void addToList() {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ContactListAdapter ca = new ContactListAdapter(createList(), getActivity(), new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Log.i("OnClick", "inside the onclick of the adapter");
                Intent i = new Intent(getContext(), BaseActivity.class);
                i.putExtra("pagenumber", "2");
                Log.e("OnClick", "internt set !! ");
                startActivity(i);
                Log.e("OnClick", "finisherererer");
                getActivity().finish();
            }
        });
        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(ca);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recList.setAdapter(ca);
    }

    public void searchTtem(String s) {
        ArrayList<ArrayList> contactList = new ArrayList<>();
        ArrayList<String> contact = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).get(0).toString().startsWith(s)) {
                contact.add(result.get(i).get(0).toString());
                contact.add(result.get(i).get(1).toString());
                contactList.add(contact);
                contact = new ArrayList<>();
            }
        }
        Log.e("result", contactList + "");
        ContactListAdapter contactListAdapter = new ContactListAdapter(contactList, getActivity(), new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Log.i("OnClick", "inside the onclick of the adapter");
                Intent intent = new Intent(getContext(), BaseActivity.class);
                intent.putExtra("pagenumber", "2");
                startActivity(intent);
                getActivity().finish();

            }
        });
        recList.setAdapter(contactListAdapter);
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
