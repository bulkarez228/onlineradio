package com.example.onlineradio;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataSaver{
    private static final String TAG = "Data saver";
    private static final String fileName = "stations";

    public static ArrayList<Station> getData(Context context){
        ArrayList<Station> stations=new ArrayList<>();
        try {
            if (new File(context.getFilesDir(), "stations").exists()) {
                FileInputStream fileInputStream = context.openFileInput("stations");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                String jsonString = stringBuilder.toString();

                stations = new ObjectMapper().readValue(jsonString, new TypeReference<ArrayList<Station>>(){});
            }
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return stations;
    }

    public static void SaveData(Context context, ArrayList<Station> stations) {
        try {
            if (! new File(context.getFilesDir(), "stations").exists()) new File(context.getFilesDir(), "stations").createNewFile();

            String jsonString = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(stations);
            FileOutputStream fileOutputStream;
            fileOutputStream = context.openFileOutput("stations", Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            Log.d(TAG, "Data saved!");
            fileOutputStream.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
