package com.sdsmdg.pulkit.callingtext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private View view;
    private List<ArrayList> result, favList;
    private FloatingActionButton mFavFab;
    private DataBaseHandler dataBaseHandler;
    private EditText editText;
    private String numberAddFav;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_favourites,container,false);
        mFavFab=(FloatingActionButton) view.findViewById(R.id.favoriteFab);
        editText = (EditText)view.findViewById(R.id.favAddNumber);
        mFavFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getVisibility() == View.VISIBLE)
                {
                    numberAddFav=editText.getText().toString();
                    mFavFab.setImageResource(R.drawable.ic_plus);
                    editText.setVisibility(View.INVISIBLE);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ContactsContract.Contacts.STARRED,1);
                    getContext().getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, ContactsContract.Contacts.Data.DATA1+"=?", new String[]{numberAddFav+""});

                }
                else {
                    editText.setText("");
                    mFavFab.setImageResource(R.drawable.red_tick);
                    editText.setVisibility(View.VISIBLE);
                }

            }
        });

        dataBaseHandler=DataBaseHandler.getInstance(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.favRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new FavouriteAdapter(getContext(), createFavouriteList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
               return view;
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

}

