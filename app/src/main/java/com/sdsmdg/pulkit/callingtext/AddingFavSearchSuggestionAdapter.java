
package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddingFavSearchSuggestionAdapter extends ArrayAdapter<PhoneContact> {
    private ArrayList<PhoneContact> suggestions;
    private int layoutId;
    int orangeColor = getContext().getResources().getColor(R.color.dark_orange);
    private LayoutInflater mInflater;
    private String queryString;
    private ArrayList<PhoneContact> phoneContacts;
    private final ForegroundColorSpan fcs = new ForegroundColorSpan(orangeColor);

    public AddingFavSearchSuggestionAdapter(Context context, int resource, ArrayList<PhoneContact> phoneContacts) {
        super(context, resource);
        this.suggestions = new ArrayList<>();
        this.phoneContacts = phoneContacts;
        this.layoutId = resource;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.list_item_name_add_fav);
            holder.phone = (TextView) convertView.findViewById(R.id.list_item_phone_add_fav);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        PhoneContact contact = getItem(position);
        if (contact != null){
            SpannableStringBuilder sb = new SpannableStringBuilder(contact.name);
            String phone = contact.phone;
            if(!queryString.equals("")){
                int matchPosition = contact.name.toLowerCase().indexOf(queryString.toLowerCase());
                if (matchPosition != -1){
                    sb.setSpan(fcs, matchPosition, matchPosition + queryString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
            holder.name.setText(sb);
            holder.phone.setText(phone);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView name, phone;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ( (PhoneContact)resultValue ).name;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            suggestions.clear();
            if (constraint == null){
                queryString = "";
            }
            else{
                queryString = constraint.toString();
                for (PhoneContact phoneContact : phoneContacts){
                    if (phoneContact.name.toLowerCase().contains(queryString))
                        suggestions.add(phoneContact);
                }
                FilterResults results = new FilterResults();
                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<PhoneContact> filterList = (ArrayList<PhoneContact>)filterResults.values;
            if (filterResults != null && filterResults.count > 0){
                clear();
                for (PhoneContact phoneContact : filterList){
                    add(phoneContact);
                    notifyDataSetChanged();
                }
            }
        }
    };
} 
