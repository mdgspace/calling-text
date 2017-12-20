package com.sdsmdg.pulkit.callingtext;

import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

public class FavNamesFilter extends Filter {

    FavAdapter adapter;
    List<FavContact> originalList;
    List<FavContact> filteredList;

    public FavNamesFilter(FavAdapter adapter, List<FavContact> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList; //contains list of contacts added by the user as favourites
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // filtering logic
            for (final FavContact name : originalList) {
                if (name.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(name);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults filterResults) {
        adapter.filteredNames.clear();
        adapter.filteredNames.addAll((List) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}