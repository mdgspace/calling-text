package com.sdsmdg.pulkit.callingtext;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.GridItemViewHolder> {

    private Context context;
    private List<ArrayList> mItemList;
    private AdapterView.OnItemClickListener onItemClickListener;


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
    public void onBindViewHolder(GridItemViewHolder holder, int position) {

        holder.tvName.setText((CharSequence) mItemList.get(position).get(0));
//        Toast.makeText(context, (CharSequence) mItemList.get(position).get(1),Toast.LENGTH_LONG).show();
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable = TextDrawable.builder().buildRound(( mItemList.get(position).get(0)).toString().substring(0,1), color);
        holder.imageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tvName;
        public FavouriteAdapter favouriteAdapter;
        public ImageView imageView;
        public GridItemViewHolder(View itemView, FavouriteAdapter favouriteAdapter) {
            super(itemView);
            this.favouriteAdapter=favouriteAdapter;
            tvName= (TextView) itemView.findViewById(R.id.nameFav);
            imageView= (ImageView) itemView.findViewById(R.id.imageFav);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,"Clicked",Toast.LENGTH_LONG).show();
        }
    }
}
