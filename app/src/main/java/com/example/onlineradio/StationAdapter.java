package com.example.onlineradio;
import static com.example.onlineradio.MainActivity.adapter;
import static com.example.onlineradio.MainActivity.arr;
import static com.example.onlineradio.MainActivity.resources;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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

    private List<Station> stationList = new ArrayList<>();

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

    public void setItems(ArrayList<Station> stationList) {
        stationList.addAll(stationList);
        notifyDataSetChanged();
    }

    class StationViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком
        public ImageView imageView;
        public TextView name;
        public TextView description;
        public TextView type;
        public CheckBox checkBox;

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public StationViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            type = itemView.findViewById(R.id.type);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(Station station) {
            if (type.equals("extra")){
            /*((TextView) convertView.findViewById(R.id.name)).setVisibility(View.GONE);
            ((TextView) convertView.findViewById(R.id.description)).setVisibility(View.GONE);
            ((TextView) convertView.findViewById(R.id.type)).setVisibility(View.GONE);
            ((ImageView) convertView.findViewById(R.id.imageView)).setVisibility(View.GONE);
            ((CheckBox) convertView.findViewById(R.id.checkBox)).setVisibility(View.GONE);
            convertView.setClickable(false);*/
            }
            else {
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

                checkBox.setOnClickListener(view -> {
                    station.like = checkBox.isChecked();
                    Collections.sort(arr, Collections.reverseOrder());
                    adapter.notifyDataSetChanged();
                });

            }
        }
    }


    /*@SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Station station = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter, null);
        }
        if (station == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter, null);
        }
        else {
            if (station.type.equals("extra")){
                ((TextView) convertView.findViewById(R.id.name)).setVisibility(View.GONE);
                ((TextView) convertView.findViewById(R.id.description)).setVisibility(View.GONE);
                ((TextView) convertView.findViewById(R.id.type)).setVisibility(View.GONE);
                ((ImageView) convertView.findViewById(R.id.imageView)).setVisibility(View.GONE);
                ((CheckBox) convertView.findViewById(R.id.checkBox)).setVisibility(View.GONE);
                convertView.setClickable(false);
            }
            else {
                ((TextView) convertView.findViewById(R.id.name)).setText(station.name);
                ((TextView) convertView.findViewById(R.id.description)).setText(station.description);
                ((TextView) convertView.findViewById(R.id.description)).setSelected(station.played);

                if (station.type.equals(""))
                    ((TextView) convertView.findViewById(R.id.type)).setVisibility(View.GONE);
                else
                    ((TextView) convertView.findViewById(R.id.type)).setText(station.type);

                if (station.played)
                    ((TextView) convertView.findViewById(R.id.name)).setTextColor(resources.getColor(R.color.bloody_red));
                else
                    ((TextView) convertView.findViewById(R.id.name)).setTextColor(resources.getColor(R.color.black));

                if (station.img != 0)
                    ((ImageView) convertView.findViewById(R.id.imageView)).setImageResource(station.img);
                else
                    ((ImageView) convertView.findViewById(R.id.imageView)).setImageResource(R.drawable.album);

                CheckBox ch = (CheckBox) convertView.findViewById(R.id.checkBox);

                if (station.like) ch.setChecked(true);
                else ch.setChecked(false);

                ch.setOnClickListener(view -> {
                    station.like = ch.isChecked();
                    Collections.sort(arr, Collections.reverseOrder());
                    adapter.notifyDataSetChanged();
                });

            }

        }
        return convertView;*/
    }

