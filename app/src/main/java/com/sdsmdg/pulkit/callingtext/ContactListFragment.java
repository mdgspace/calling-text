package com.sdsmdg.pulkit.callingtext;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class ContactListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public RecyclerView recList;
    public ImageButton button1;
    Button button;
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
    int mShortDurationTime;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_contact_list, container, false);

        // init views
        button1 = (ImageButton) view.findViewById(R.id.imageButton21);
        recList = (RecyclerView) view.findViewById(R.id.questionList_recycler);
        dimLayout = (FrameLayout) view.findViewById(R.id.dim_layout);
        searchLayout = (LinearLayout) view.findViewById(R.id.searchbar);
        backButton = (ImageView) view.findViewById(R.id.backbutton);
        searchBox = (AutoCompleteTextView) view.findViewById(R.id.searchbox);
        result = new ArrayList<>();
        // Initially hide the content view.
        recList.setVisibility(View.GONE);
        // Retrieve and cache the system's default "short" animation time.
        mShortDurationTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        // To retain the contact list when the fragments are swiped or device is rotated
        result = new ArrayList<>();
        result = ((BaseActivity)getActivity()).getSavedContacts();
        phoneContactsList = ((BaseActivity)getActivity()).getSavedPhoneContacts();
        if (result == null || result.size() == 0)
            new CreateContactList().execute();

        // set the message for progress bar
        mProgress.setMessage("Displaying Contacts...");

        // Back button to request the focus back to the ContactsListFragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDimLayout();
            }
        });

        // Dim layout which is present during search operations
        dimLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDimLayout();
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
      /*
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
        */
      /*  VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller)view.findViewById(R.id.fast_scroller);
        fastScroller.setRecyclerView(recList);
        recList.setOnScrollListener(fastScroller.getOnScrollListener());
        */
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getBaseContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Log.d("p", result + "");

        // Setting the attributes of the search box
        searchBox.setThreshold(1);
        searchBox.setDropDownAnchor(R.id.searchbar);
        adapter = new PhoneSearchSuggestionAdapter(getContext(), R.layout.search_suggestion_item, phoneContactsList);
        searchBox.setAdapter(adapter);

        if (result != null && result.size() != 0) {
            setContactListAdapter();
        }

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

  // Previous Code
  /*
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
*/
    public void addToList() {
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
                Log.i("OnClick", "internt set !! ");
                startActivity(i);
                Log.i("OnClick", "finisherererer");
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
        Log.i("result", contactList + "");
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
        recList.setAdapter(ca);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.contact_list_main_menu, menu);
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

        List<ArrayList> contactsList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.show();
        }

        @Override
        protected List<ArrayList> doInBackground(Void... params) {
            contactsList = createList();
            return contactsList;
        }

        private List<ArrayList> createList() {
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
                                            contactsList.add(a);
                                            phoneContact = new PhoneContact(name, phone);
                                            phoneContactsList.add(phoneContact);
                                            i = 1;
                                        } else {
                                            if (!(prev_number.equals(phone))) {
                                                a.set(1, phone);
                                                contactsList.add(a);
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

            // Removing duplicate contacts by simple comparison
            int z;
            for(z = 0; z < contactsList.size()-1; z++){
                if(contactsList.get(z) == contactsList.get(z+1)){
                    contactsList.remove(z);
                }
            }

            int size = contactsList.size();   // To check the size of the contactsLists
            return contactsList;
        }

        @Override
        protected void onPostExecute(List<ArrayList> output) {
            super.onPostExecute(output);
            result = output;
            setContactListAdapter();
            onContactsLoaded.saveContacts(output, phoneContactsList);
        }
    }

    public interface OnContactsLoaded {
        public void saveContacts(List<ArrayList> contactsList, ArrayList<PhoneContact> phoneContacts);
    }

    public void setContactListAdapter(){

        ca = new ContactListAdapter(result, getActivity(), new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick() {
                Log.i("OnClick", "inside the onclick of the adapter");
                Intent i = new Intent(getContext(), BaseActivity.class);
                i.putExtra("pagenumber", "2");
                Log.d("OnClick", "internt set !! ");
                startActivity(i);
                Log.d("OnClick", "finisherererer");
                getActivity().finish();
            }
        });

        alphaAdapter = new ScaleInAnimationAdapter(ca);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        alphaAdapter.setDuration(1000);
        alphaAdapter.setFirstOnly(false);
        recList.setAdapter(alphaAdapter);
        crossFade();
    }

    private void crossFade() {

        // Set the recycler view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        recList.setAlpha(0);
        recList.setVisibility(View.VISIBLE);
        // dismiss the progress bar
        mProgress.dismiss();
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        recList.animate().alpha(1).setDuration(mShortDurationTime);
    }

    private void removeDimLayout() {
        dimLayout.setVisibility(View.GONE);
        searchLayout.setVisibility(View.GONE);
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
