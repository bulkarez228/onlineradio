package com.example.onlineradio;
import static com.example.onlineradio.MainActivity.resources;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditStationAdapter extends ArrayAdapter<Station> {

    public EditStationAdapter(Context context, ArrayList<Station> arr) {
        super(context, R.layout.edit_adapter, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Station station = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_adapter, null);
        }
        if (station == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_adapter, null);
        }
        else {
            // Заполняем адаптер
            ((TextView) convertView.findViewById(R.id.name_edit)).setText(station.name);
            ((TextView) convertView.findViewById(R.id.description_edit)).setText(station.description);

            if (station.type.equals("")){
                ((TextView) convertView.findViewById(R.id.type_edit)).setText(R.string.no_type);
            }
            else{
                ((TextView) convertView.findViewById(R.id.type_edit)).setText(station.type);
            }


            if (station.played)
                ((TextView) convertView.findViewById(R.id.name_edit)).setTextColor(resources.getColor(R.color.bloody_red));
            else
                ((TextView) convertView.findViewById(R.id.name_edit)).setTextColor(resources.getColor(R.color.black));

            ((ImageView) convertView.findViewById(R.id.imageView_edit)).setImageResource(R.drawable.album);

            ((ImageView) convertView.findViewById(R.id.imageView_trash)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }
        return convertView;
    }
}
