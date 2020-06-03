package com.example.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Event;
import com.example.sensores.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListEvents extends AppCompatActivity {
    ListView list;
    ArrayList<Event> list_events;
    ArrayList<String> list_event_desc;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        list = (ListView) findViewById(R.id.listEvents);
        list_event_desc = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_event_desc);
        list.setAdapter(adapter);
        loadEvent();
    }

    private void loadEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list data", null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        list_events = gson.fromJson(json, type);

        if (list_events == null) {
            list_events = new ArrayList<>();
        } else {
            for (Event item: list_events) {
                list_event_desc.add(item.getTypeEvents() + "\n" +item.getDescription() + "\n" + item.getDate().toString());
                Log.i("item desc", item.getDescription() + "\n" + item.getTypeEvents());
                adapter.notifyDataSetChanged();
            }
        }
    }
}
