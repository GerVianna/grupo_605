package com.example.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.CustomListAdapter;
import com.example.models.Event;
import com.example.models.GlobalUser;
import com.example.sensores.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListEvents extends AppCompatActivity {
    ListView list;
    ArrayList<String> list_event_desc;
    ArrayAdapter<String> adapter;
    GlobalUser globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        globalVariable = (GlobalUser) getApplicationContext();
        list = (ListView) findViewById(R.id.listEvents);
        list_event_desc = new ArrayList<String>();
        adapter = new CustomListAdapter(this, R.layout.custom_list, list_event_desc);
        list.setAdapter(adapter);
        loadEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void loadEvent() {
        for (Event item: globalVariable.getList_events()) {
            list_event_desc.add(item.getTypeEvents() + "\n" +item.getDescription() + "\n" + item.getDate());
            adapter.notifyDataSetChanged();
        }
    }

    /*


    private void loadEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(globalVariable.getEmail(), null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        list_events = gson.fromJson(json, type);
        if (list_events == null) {
            list_events = new ArrayList<>();
        } else {
            for (Event item: list_events) {
                list_event_desc.add(item.getTypeEvents() + "\n" +item.getDescription() + "\n" + item.getDate());
                adapter.notifyDataSetChanged();
            }
        }
    }

     */
}
