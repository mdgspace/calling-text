package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private View view;
    private FloatingActionButton mFavFab;
    private DataBaseHandler dataBaseHandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_favourites,container,false);
        mFavFab=(FloatingActionButton) view.findViewById(R.id.favoriteFab);
        dataBaseHandler=DataBaseHandler.getInstance(getContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.favRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new FavouriteAdapter(getContext(), createList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        return view;
    }
    private List<CallerDetails> createList() {
        List<CallerDetails> result;
        result=dataBaseHandler.getAllCallers();
        return result;
    }
}

