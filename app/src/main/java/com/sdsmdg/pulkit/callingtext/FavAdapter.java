package com.sdsmdg.pulkit.callingtext;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class FavAdapter extends ArrayAdapter<FavContact> {

    private final List<FavContact> names ;
    List<FavContact> filteredNames = new ArrayList<>();

    public FavAdapter( Context context, List<FavContact> names) {
        super(context,0, names);
        this.names = names; //stores names of all the contacts marked as favourites
    }

    @Override
    public int getCount() {
        return filteredNames.size();
    }

    @Override
    public Filter getFilter() {
        return new FavNamesFilter(this, names);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // To get the data item from filtered list.
        FavContact name = filteredNames.get(position);

        // To inflate the custom row layout for suggestion items.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.fav_suggestion_item, parent, false);

        //Initializing the view objects for name and number
        TextView tvName = (TextView) convertView.findViewById(R.id.list_item_favname);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.list_item_favphone);

       //To display the name and number in the text fields of a row
        tvName.setText(name.getName());
        tvNumber.setText(name.getNumber());
        return convertView;

    }
}