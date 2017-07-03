package com.sdsmdg.pulkit.callingtext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.GridItemViewHolder> {

    private Context context;
    private List<ArrayList> mItemList;
    private String mName, mNumber;


    public FavouriteAdapter(Context context, List<ArrayList> list){
        this.context=context;
        mItemList=list;
    }
    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card_favourite, parent, false);
        return new GridItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(final GridItemViewHolder holder, int position) {

        mName= (String) mItemList.get(position).get(0);
        mNumber= (String) mItemList.get(position).get(1);
        holder.tvName.setText((CharSequence) mItemList.get(position).get(0));

        /*Generates a random color everytime the view is bound to the viewholder, also gives the icon for the first letter of the name*/

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable = TextDrawable.builder().buildRound(( mItemList.get(position).get(0)).toString().substring(0,1), color);
        holder.imageView.setImageDrawable(drawable);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.card_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            /*Removes the contact from favourites list*/
                            case R.id.removeFavCard:
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ContactsContract.Contacts.STARRED,0);
                                context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, ContactsContract.Contacts.DISPLAY_NAME+"=?", new String[]{holder.tvName.getText().toString()});
                                break;
                            }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
/*
* Sets Adapter for favourite fragment in GridLayoutManager to give a better look*/
    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tvName, buttonViewOption;
        public FavouriteAdapter favouriteAdapter;
        public ImageView imageView;
        public GridItemViewHolder(View itemView, FavouriteAdapter favouriteAdapter) {
            super(itemView);
            this.favouriteAdapter=favouriteAdapter;
            tvName= (TextView) itemView.findViewById(R.id.nameFav);
            imageView= (ImageView) itemView.findViewById(R.id.imageFav);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            itemView.setOnClickListener(this);
        }
/*
* Handles onClick event of the favourite card*/
        @Override
        public void onClick(View v) {
            BaseActivity.mname=mName;
            BaseActivity.mnumber=mNumber;
            Intent i = new Intent(context, BaseActivity.class);
            i.putExtra("pagenumber", "2");
            context.startActivity(i);
        }
    }



}
