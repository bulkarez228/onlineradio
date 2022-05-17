package com.example.onlineradio;

import static com.example.onlineradio.MainActivity.arr;
import static com.example.onlineradio.MainActivity.adapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.Notification;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditStationActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    EditText type;
    FloatingActionButton delete;
    ImageView img;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_station);
        name = findViewById(R.id.stationName);
        description = findViewById(R.id.stationDescription);
        type = findViewById(R.id.stationType);
        delete = findViewById(R.id.delete_btn);
        img = findViewById(R.id.imageAlbum);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        name.setText(arr.get(id).name);
        description.setText(arr.get(id).description);
        type.setText(arr.get(id).type);
        img.setImageResource(arr.get(id).img);

        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    arr.get(id).setName(name.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    arr.get(id).setName(name.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        description.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    arr.get(id).setDescription(description.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    arr.get(id).setDescription(description.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        type.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    arr.get(id).setType(type.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    arr.get(id).setType(type.getText().toString());
                    DataSaver.SaveData(EditStationActivity.this, arr);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             ///AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditStationActivity.this);
             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(EditStationActivity.this, R.style.myDialog));
             //TODO
             alertDialogBuilder
                     .setTitle("Вы действительно хотите удалить?")
                     .setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                             arr.remove(id);
                             adapter.notifyDataSetChanged();
                             DataSaver.SaveData(EditStationActivity.this, arr);
                             finish();
                         }
                     })
                     .setNegativeButton("НЕТ",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
                                     dialog.cancel();
                                 }
                             });
             AlertDialog alertDialog = alertDialogBuilder.create();
             alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
             alertDialog.show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*public void createNotify(){
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification);

// Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .build();
    }*/
}