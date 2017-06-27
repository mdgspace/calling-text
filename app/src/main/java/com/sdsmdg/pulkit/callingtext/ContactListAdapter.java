package com.sdsmdg.pulkit.callingtext;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ListViewHolder> {
    private List<ArrayList> contactList;
    private Activity parentAct;
    private ContactListAdapter.ListViewHolder h1;
    private int lastPosition = -1;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        public void onItemClick();

    }



    ContactListAdapter(List<ArrayList> contactList1, Activity activity, OnItemClickListener listener) {
        Log.e("pil", "in adapter");
        contactList = contactList1;
        parentAct = activity;
        this.listener = listener;
    }


    @Override
    public ContactListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("pil", "in adapter");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.ListViewHolder holder, final int position) {
        Log.e("pil", "in adapter");
        h1 = holder;
        holder.name.setText((String) contactList.get(position).get(0));
        holder.phoneNumber.setText((String) contactList.get(position).get(1));
        holder.textsmsLogo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                h1.textsmsLogo.startAnimation(
                        AnimationUtils.loadAnimation(v.getContext(), R.anim.rotation));
//                Intent intent = new Intent(v.getContext(), NewFragment.class);
//                intent.putExtra("name",(String)contactList.get(position).get(0));
//                intent.putExtra("number",(String)contactList.get(position).get(1));
//                v.getContext().startActivity(intent);


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick();
                BaseActivity.setMname(holder.name.getText().toString());
                BaseActivity.setMnumber(holder.phoneNumber.getText().toString());

                Log.i("CLICK ", "OVER HERE BITCH!"+BaseActivity.getMname()+" "+BaseActivity.getMnumber());

            }
        });

        for (int i = 0; i < getItemCount(); i++) {
            //animate(itemView, i);
        }
    }




    private void animate(final View view, final int position) {
        view.animate();
        view.setTranslationY(100);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(300).setStartDelay(position * 100);

    }

    @Override
    public int getItemCount() {
        Log.d("itemCount", contactList.size() + "");
        return contactList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name;
        protected TextView phoneNumber;
        protected ImageButton textsmsLogo;



        public ListViewHolder(final View vi) {
            super(vi);
            Log.e("pil", "in adapter");
            name = (TextView) vi.findViewById(R.id.textView6);
            phoneNumber = (TextView) vi.findViewById(R.id.textView7);
            textsmsLogo = (ImageButton) vi.findViewById(R.id.imageButton21);
            vi.setOnClickListener(this);
            textsmsLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }

        @Override
        public void onClick(View v) {
            Animation animation = AnimationUtils.loadAnimation(parentAct, R.anim.swipe);
            v.startAnimation(animation);
        }
    }

    public List<ArrayList> getContactList() {
        return contactList;
    }

}
