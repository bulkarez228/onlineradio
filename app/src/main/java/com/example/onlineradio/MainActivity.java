package com.example.onlineradio;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends AppCompatActivity {

    ImageView btn;
    TextView name;
    TextView description;
    RecyclerView lv;
    FloatingActionButton add_btn;
    RelativeLayout lplayer;
    RelativeLayout lconnecting;
    RadioGroup page_selector;
    ImageView img;

    MediaPlayer mediaPlayer;
    EditStationAdapter edit_adapter;
    MediaSessionCompat mediaSession;

    public static Resources resources;
    public static ArrayList<Station> arr;
    public static StationAdapter adapter;
    public static SharedPreferences settings;
    public static final String APP_PREFERENCES = "settings";

    boolean playStatus = false;
    int lastpos=-1;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        resources=getResources();

        btn = findViewById(R.id.button);
        name = findViewById(R.id.textView);
        description = findViewById(R.id.description);
        lv = findViewById(R.id.listview);
        lplayer = findViewById(R.id.player);
        lconnecting = findViewById(R.id.connecting_layout);
        page_selector = findViewById(R.id.page_selector);
        add_btn = findViewById(R.id.add_btn);
        img = findViewById(R.id.imageViewOnPlayer);

        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mediaSession = new MediaSessionCompat(this, "mediaPlayer");

        //settings.getBoolean("firstrun", true)
        if (settings.getBoolean("firstrun", true)) {
            arr = makeStations();
            settings.edit().putBoolean("firstrun", false).apply();
        }
        else{
            arr = DataSaver.getData(this);
        }

        adapter = new StationAdapter();
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(adapter);
        adapter.setItems(arr);
        edit_adapter = new EditStationAdapter(this, arr);


        /*lv.setOnItemClickListener((parent, view, position, id) -> {
            id = position;
            if (lastpos==position){
                OnMedia(view);
                adapter.notifyDataSetChanged();
            }
            else {
                stopPlayer();
                Runnable task = () -> {
                    runOnUiThread(() -> lconnecting.setVisibility(View.VISIBLE));

                    prepare(arr.get(position).url);


                    runOnUiThread(() -> {
                        lconnecting.setVisibility(View.GONE);
                        OnMedia(view);
                    });
                };
                Thread thread = new Thread(task);
                thread.start();

                if(lplayer.getVisibility()== View.GONE){
                    lplayer.setVisibility(View.VISIBLE);
                }
                img.setImageResource(arr.get(position).img);
                if (lastpos!=-1)
                    arr.get(lastpos).played=false;
                arr.get(position).played=true;
                adapter.notifyDataSetChanged();
                lastpos = position;
                name.setText(arr.get(position).name);
                name.setSelected(true);
            }
        });

        lv.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Intent intent2 = new Intent(MainActivity.this, EditStationActivity.class);
            intent2.putExtra("id", i);
            startActivity(intent2);
            return false;
        });
*/
        btn.setOnClickListener(this::OnMedia);

        /*page_selector.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i){
                case R.id.home:
                    add_btn.setVisibility(View.GONE);
                    lv.setAdapter(adapter);
                    break;
                case R.id.settings:
                    add_btn.setVisibility(View.VISIBLE);
                    lv.setAdapter(edit_adapter);
                    break;
                case R.id.search:
                    add_btn.setVisibility(View.GONE);
                    break;
            }
        });*/

        add_btn.setOnClickListener(view -> NewStationDialog());
    }

    Station makeStation(String uri) {
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        String str_name="";
        String str_description="";
        String str_genre="";

        try{
            mmr.setDataSource(uri);
            str_name = mmr.getMetadata().getString("icy-name");
            str_description = mmr.getMetadata().getString("icy-description");
            str_genre = mmr.getMetadata().getString("icy-genre");
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+uri, Toast.LENGTH_LONG).show();
            return null;
        }

        return new Station(uri, str_name, str_description, str_genre);
    }

    ArrayList<Station> makeStations() {
        ArrayList<Station> arr = new ArrayList<>();
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();

        String url="https://dfm.hostingradio.ru/dfm96.aacp";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "DFM", "Dance Radio", "DANCE", R.drawable.dfm);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url="https://pub0301.101.ru:8443/stream/air/mp3/256/99";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Energy", "РАДИО ENERGY", "POP", R.drawable.nrj);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url="https://retro.hostingradio.ru:8014/retro320.mp3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Ретро FM", "Лучшие хиты 80-х, 90-х", "RETRO", R.drawable.retro_fm);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url="https://dorognoe.hostingradio.ru/radio";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Дорожное радио", "Вместе в пути!", "", R.drawable.dorojnoe);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://icecast-newradio.cdnvideo.ru/newradio3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Новое радио", "Всё только начинается!", "POP", R.drawable.new_radio);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://pub0301.101.ru:8443/stream/air/mp3/256/102";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Юмор FM", "Смешное радио", "", R.drawable.humor_fm);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://chanson.hostingradio.ru:8041/chanson256.mp3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио шансон", "Авторская песня/шансон", "CHANSON", R.drawable.shanson);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://rusradio.hostingradio.ru/rusradio96.aacp";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Русское Радио", "Только русские песни", "", R.drawable.russian_radio);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://radiorecord.hostingradio.ru/gold96.aacp";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио рекорд", "Number 1 Dance Radio", "DANCE", R.drawable.radiorecord);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "http://emgregion.hostingradio.ru:8064/moscow.europaplus.mp3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Europa plus", "Number 1 in Russia", "POP", R.drawable.europa_plus);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "http://icecast.vgtrk.cdnvideo.ru/mayakfm_mp3_128kbps";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио маяк", "Федеральная Российская государственная информационно-музыкальная радиостанция", "NEWS", R.drawable.mayak);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://nashe1.hostingradio.ru/nashe-256";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио маяк", "Федеральная Российская государственная информационно-музыкальная радиостанция", "NEWS", R.drawable.mayak);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://radiomv.hostingradio.ru:80/radiomv256.mp3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио маяк", "Федеральная Российская государственная информационно-музыкальная радиостанция", "NEWS", R.drawable.mayak);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://nashe1.hostingradio.ru/nashe-256";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио маяк", "Федеральная Российская государственная информационно-музыкальная радиостанция", "NEWS", R.drawable.mayak);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        url= "https://radiomv.hostingradio.ru:80/radiomv256.mp3";
        try{
            mmr.setDataSource(url);
            Station station = new Station(url, "Радио маяк", "Федеральная Российская государственная информационно-музыкальная радиостанция", "NEWS", R.drawable.mayak);
            arr.add(station);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
            Toast.makeText(this, "Ошибка подключения к радио: "+url, Toast.LENGTH_LONG).show();
        }

        for (int i=0;i<2;i++) {
            Station station = new Station("extra");
            arr.add(station);
        }

        return arr;
    }

    public void prepare (String uri){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(uri));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void OnMedia(View view) {
        if (mediaPlayer != null) {
            if (playStatus) {
                mediaPlayer.pause();
                playStatus = false;
            }
            else {
                try { mediaPlayer.start(); }
                catch (IllegalStateException e) { e.printStackTrace(); }
                playStatus = true;
            }
            if (mediaPlayer.isPlaying()) {
                btn.setImageResource(R.drawable.outline_pause_white_48dp);
            }
            else{
                btn.setImageResource(R.drawable.outline_play_arrow_white_48dp);
            }
        }
    }

    public void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            playStatus=false;
        }
    }

    void NewStationDialog() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.new_station_dialog, null);
        //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        //TODO
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.etUserInput);

        alertDialogBuilder
                .setPositiveButton("OK", (dialog, id) -> {
                    Station station = makeStation(userInput.getText().toString());
                    if (station!=null) {
                        arr.add(station);
                        adapter.notifyDataSetChanged();
                        DataSaver.SaveData(MainActivity.this, arr);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        DataSaver.SaveData(MainActivity.this, arr);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DataSaver.SaveData(MainActivity.this, arr);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        DataSaver.SaveData(MainActivity.this, arr);
        super.onPause();
    }


}