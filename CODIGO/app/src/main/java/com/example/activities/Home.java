package com.example.activities;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.models.Event;
import com.example.models.ResponseEvent;
import com.example.sensores.R;
import com.example.services.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    private Button goAccelerometer;
    private Button goProximity;
    private Button goEvents;
    private String token;
    ArrayList<Event> list_events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        loadEvent();
        goAccelerometer = (Button) findViewById(R.id.buttonAcceler);
        goProximity = (Button) findViewById(R.id.buttonProx);
        goEvents = (Button) findViewById(R.id.buttonTableEvents);
        goAccelerometer.setOnClickListener(eventsButtons);
        goProximity.setOnClickListener(eventsButtons);
        goEvents.setOnClickListener(eventsButtons);
        Event newEvent = new Event();
        newEvent.setDescription("El usuario ingreso al sistema correctamente");
        newEvent.setState("ACTIVO");
        newEvent.setTypeEvents("LOGIN");
        Call<ResponseEvent> call = RetrofitClient
                .getInstance()
                .getService()
                .regEvent(token, "TEST", newEvent.getTypeEvents(), newEvent.getState(), newEvent.getDescription());
        call.enqueue(new Callback<ResponseEvent>() {
                         @Override
                         public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                             if (response.code() != 400 && response.code() != 401) {
                                 Event eventSaved = response.body().getEvent();
                                 list_events.add(eventSaved);
                                 saveEvent();
                             } else
                                 return;
                         }

                        @Override
                        public void onFailure(Call<ResponseEvent> call, Throwable t) {
                             return;
                        }
                     });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Desea cerrar la sesión y volver al login?");

        // add the buttons
        builder.setPositiveButton("Cerrar Sesion", null);
        builder.setNegativeButton("Cancelar", null);

        // create and show the alert dialog
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(Home.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private View.OnClickListener eventsButtons = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("token", token);
            switch(v.getId()) {
                case R.id.buttonAcceler:
                    intent.setClass(Home.this, Accelerometer.class);
                    break;
                case R.id.buttonProx:
                    intent.setClass(Home.this, Proximity.class);
                    break;
                case R.id.buttonTableEvents:
                    intent.setClass(Home.this, ListEvents.class);
                    break;
            }
            startActivity(intent);
        }
    };

    private void saveEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list_events);
        editor.putString("list data", json);
        editor.apply();
    }

    private void loadEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list data", null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        list_events = gson.fromJson(json, type);

        if (list_events == null) {
            list_events = new ArrayList<>();
        }
    }



}
