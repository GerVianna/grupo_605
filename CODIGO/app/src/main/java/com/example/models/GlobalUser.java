package com.example.models;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GlobalUser extends Application {
    private String token;
    private String email;
    private ArrayList<Event> list_events;


    public ArrayList<Event> getList_events() {
        return list_events;
    }

    public void setList_events(ArrayList<Event> list_events) {
        this.list_events = list_events;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addEventToList(Event event) {
        this.list_events.add(event);
    }

    public void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list_events);
        editor.putString(email, json);
        editor.apply();
    }

    public void loadEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(email, null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        list_events = gson.fromJson(json, type);
        if (list_events == null) {
            list_events = new ArrayList<>();
        }
    }
}
