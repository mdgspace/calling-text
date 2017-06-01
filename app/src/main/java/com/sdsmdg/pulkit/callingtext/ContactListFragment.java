package com.sdsmdg.pulkit.callingtext;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

import static com.sdsmdg.pulkit.callingtext.BaseActivity.getSavedContacts;

public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public RecyclerView recList;
    Button button;
    public ImageButton button1;
    EditText et1;
    static List<ArrayList> result;
    View view;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private ImageView searchButton, settingsButton, backButton;
    AutoCompleteTextView searchBox;
    FrameLayout dimLayout;
    LinearLayout searchLayout;
    ArrayList<PhoneContact> phoneContactsList;
    PhoneSearchSuggestionAdapter adapter;
    Toolbar toolbar;
    ProgressDialog mProgress;
    ScaleInAnimationAdapter alphaAdapter;
    OnContactsLoaded onContactsLoaded;
    ContactListAdapter ca;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mProgress = new ProgressDialog(context);

        try{
            onContactsLoaded = (OnContactsLoaded)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnContactsLoaded");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            phoneContactsList = savedInstanceState.getParcelableArrayList("phoneContactsList");
            for(PhoneContact phoneContact : phoneContactsList){
                ArrayList<String> a =new ArrayList<>();
                a.add(phoneContact.name);
                a.add(phoneContact.phone);
                result.add(a);
                recList.setAdapter(alphaAdapter);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_contact_list, container, false);
        button1 = (ImageButton) view.findViewById(R.id.imageButton21);
        recList = (RecyclerView) view.findViewById(R.id.questionList_recycler);
        dimLayout = (FrameLayout) view.findViewById(R.id.dim_layout);
        searchLayout = (LinearLayout) view.findViewById(R.id.searchbar);
        backButton = (ImageView) view.findViewById(R.id.backbutton);
        searchBox = (AutoCompleteTextView) view.findViewById(R.id.searchbox);

        result = new ArrayList<>();

        if (savedInstanceState != null){
            phoneContactsList = savedInstanceState.getParcelableArrayList("phoneContactsList");
            for(PhoneContact phoneContact : phoneContactsList){
                ArrayList<String> a =new ArrayList<>();
                a.add(phoneContact.name);
                a.add(phoneContact.phone);
                result.add(a);
            }
        }
        else {
            result = ((BaseActivity)getActivity()).getSavedContacts();
            if (result == null || result.size() == 0)
                new CreateContactList().execute();
        }
        mProgress.setMessage("Displaying Contacts...");
        // Back button to request the focus back to the ContactsListFragment
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dimLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
                searchBox.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

            }
        });

        // Dim layout which is present during search operattions
        dimLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dimLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
                searchBox.clearFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);


            }
        });

        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.showDropDown();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

      /*  et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et1.getText().toString().equals(""))
                {
                    addToList();
                }
                else
                {
                    searchTtem(et1.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
      /*  VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)view.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recList);
        recList.setOnScrollListener(fastScroller.getOnScrollListener());*/
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Log.e("p", result + "");

        ca = new ContactListAdapter(result, getActivity(), new ContactListAdapter.OnItemClickListener() {
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
        // Setting the attributes of the search box
        searchBox.setThreshold(1);
        searchBox.setDropDownAnchor(R.id.searchbar);
        adapter = new PhoneSearchSuggestionAdapter(getContext(), R.layout.search_suggestion_item, phoneContactsList);
        searchBox.setAdapter(adapter);

        alphaAdapter = new ScaleInAnimationAdapter(ca);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recList.setAdapter(alphaAdapter);
        alphaAdapter.notifyDataSetChanged();

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*
        Uri CONTENT_URI = ContactsContract.RawContacts.CONTENT_URI;
        Log.e("pul", "in loader");
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, null);
        */
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        /*
        Log.e("pul", "in loadFinished");
        Log.e("pul", cursor.getCount() + " ");
        cursor.moveToFirst();
        StringBuilder res = new StringBuilder();
        while (!cursor.isAfterLast()) {
            Log.e("pul", "in while");
            res.append("\n" + cursor.getString(21) + "-" + cursor.getString(22));
            cursor.moveToNext();
        }
        */
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

/*
    private List<ArrayList> createList() {
        result = new ArrayList<ArrayList>();

        phoneContactsList = new ArrayList<PhoneContact>();
        PhoneContact phoneContact ;
        // Code for contacts retrieval
        // Display the contacts in ascending order
        ContentResolver cr = getContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cur.getCount() > 0) {
            String prev_name = "";            // To keep account of the duplicate names

            String prev_number = "";          // To keep account of the duplicate numbers

            while (cur.moveToNext()) {

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if(name != null){

                    ArrayList<String> a = new ArrayList<String>();
                    int i = 0;
                    a.add(name);

                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        //Query phone here.  Covered next
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);

                        while (pCur.moveToNext()) {
                            // Do something with phones
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if (phone != null) {
                                if (!name.equals(prev_name) || !phone.equals(prev_number)) {
                                    if (i == 0) {
                                        a.add(phone);
                                        result.add(a);
                                        phoneContact = new PhoneContact(name, phone);
                                        phoneContactsList.add(phoneContact);
                                        i = 1;
                                    } else {
                                        if (!(prev_number.equals(phone))) {
                                            a.set(1, phone);
                                            result.add(a);
                                            phoneContact = new PhoneContact(name, phone);
                                            phoneContactsList.add(phoneContact);
                                        }
                                    }
                                    prev_number = phone;
                                }
                            }
                        }
                        pCur.close();
                    }
                    prev_name = name;
                }
            }
        }
        cur.close();

        // Previous Code
/*
        ArrayList<String> a = new ArrayList<String>();
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            a.add(name);
            a.add(phoneNumber);
            result.add(a);
            a = new ArrayList<String>();

        }
        phones.close();
*/
        // Removing duplicate contacts by simple comparison
/*
        int z;
        for(z = 0; z < result.size()-1; z++){
            if(result.get(z) == result.get(z+1)){
                result.remove(z);
            }
        }

        int size = result.size();   // To check the size of the results
        return result;
    }
    */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("phoneContactsList", phoneContactsList);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void addToList() {
        // Log.e("pulkit","pulkit");
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ContactListAdapter ca = new ContactListAdapter(result, getActivity(), new ContactListAdapter.OnItemClickListener() {
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
        for (int i = 0; i < result.size(); i++) {
            if (!((result.get(i)).get(0)).toString().contains(s)) {
                result.remove(i);
            }
        }
        ContactListAdapter ca = new ContactListAdapter(result, getActivity(), new ContactListAdapter.OnItemClickListener() {
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
        recList.setAdapter(ca);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_search_icon:
                dimLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                searchBox.requestFocus();
                searchBox.showDropDown();
                return true;

            case R.id.action_settings_icon:
                Intent settingsActivityIntent = new Intent(getActivity(), Settings.class);
                startActivity(settingsActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CreateContactList extends AsyncTask<Void, Void, List<ArrayList>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.show();
        }

        @Override
        protected List<ArrayList> doInBackground(Void... params) {
            result = createList();
            return result;
        }

        private List<ArrayList> createList() {
            result = new ArrayList<ArrayList>();

            phoneContactsList = new ArrayList<PhoneContact>();
            PhoneContact phoneContact ;
            // Code for contacts retrieval
            // Display the contacts in ascending order
            ContentResolver cr = getContext().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            if (cur.getCount() > 0) {
                String prev_name = "";            // To keep account of the duplicate names

                String prev_number = "";          // To keep account of the duplicate numbers

                while (cur.moveToNext()) {

                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if(name != null){

                        ArrayList<String> a = new ArrayList<String>();
                        int i = 0;
                        a.add(name);

                        if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            //Query phone here.  Covered next
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);

                            while (pCur.moveToNext()) {
                                // Do something with phones
                                String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                if (phone != null) {
                                    if (!name.equals(prev_name) || !phone.equals(prev_number)) {
                                        if (i == 0) {
                                            a.add(phone);
                                            result.add(a);
                                            phoneContact = new PhoneContact(name, phone);
                                            phoneContactsList.add(phoneContact);
                                            i = 1;
                                        } else {
                                            if (!(prev_number.equals(phone))) {
                                                a.set(1, phone);
                                                result.add(a);
                                                phoneContact = new PhoneContact(name, phone);
                                                phoneContactsList.add(phoneContact);
                                            }
                                        }
                                        prev_number = phone;
                                    }
                                }
                            }
                            pCur.close();
                        }
                        prev_name = name;
                    }
                }
            }
            cur.close();

            // Previous Code
/*
        ArrayList<String> a = new ArrayList<String>();
        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            a.add(name);
            a.add(phoneNumber);
            result.add(a);
            a = new ArrayList<String>();

        }
        phones.close();
*/
            // Removing duplicate contacts by simple comparison

            int z;
            for(z = 0; z < result.size()-1; z++){
                if(result.get(z) == result.get(z+1)){
                    result.remove(z);
                }
            }

            int size = result.size();   // To check the size of the results
            return result;
        }

        @Override
        protected void onPostExecute(List<ArrayList> output) {
            super.onPostExecute(output);
            mProgress.hide();
            alphaAdapter.notifyDataSetChanged();
            onContactsLoaded.saveContacts(output);
        }
    }

    public interface OnContactsLoaded {
        public void saveContacts(List<ArrayList> contactsList );
    }


}
