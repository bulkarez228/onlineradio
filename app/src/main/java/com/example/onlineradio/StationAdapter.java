package com.example.onlineradio;
import static com.example.onlineradio.MainActivity.adapter;
import static com.example.onlineradio.MainActivity.arr;
import static com.example.onlineradio.MainActivity.img;
import static com.example.onlineradio.MainActivity.lconnecting;
import static com.example.onlineradio.MainActivity.lplayer;
import static com.example.onlineradio.MainActivity.resources;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private ArrayList<Station> stationList = new ArrayList<>();
    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mItemLongClickListener;

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false);
        return new StationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        holder.bind(stationList.get(position));
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public void setItems(ArrayList<Station> arr) {
        stationList=arr;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
        Log.i("LOG", "clcked!");
    }

    class StationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView imageView;
        public TextView name;
        public TextView description;
        public TextView type;
        public CheckBox checkBox;
        public Station station;

        public StationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            type = itemView.findViewById(R.id.type);
            checkBox = itemView.findViewById(R.id.checkBox);


            checkBox.setOnClickListener(view -> {
                station.like = checkBox.isChecked();
                Collections.sort(stationList, Collections.reverseOrder());
                adapter.notifyDataSetChanged();
            });
        }

        public void bind(Station station) {
            this.station=station;
            name.setText(station.name);
            description.setText(station.description);
            description.setSelected(station.played);

            if (station.type.equals("")) {
                type.setVisibility(View.GONE);
            }
            else {
                type.setText(station.type);
            }

            if (station.played)
                name.setTextColor(resources.getColor(R.color.bloody_red));
            else
                name.setTextColor(resources.getColor(R.color.black));

            if (station.img != 0)
                imageView.setImageResource(station.img);
            else
                imageView.setImageResource(R.drawable.album);

            if (station.like) checkBox.setChecked(true);
            else checkBox.setChecked(false);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            mItemLongClickListener.onItemLongClick(view, getAdapterPosition());
            return true;
        }
    }

}

