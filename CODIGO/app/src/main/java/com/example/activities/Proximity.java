package com.example.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

public class Proximity extends AppCompatActivity implements SensorEventListener {
    TextView saveEventText;
    ProgressBar savingEvent;
    SensorManager sensorManager;
    Sensor proximitySensor;
    String token = "";
    ArrayList<Event> list_events;
    Toast toast;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);
        saveEventText = (TextView) findViewById(R.id.saveEventText);
        savingEvent = (ProgressBar) findViewById(R.id.savingEvent);
        loadEvent();
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        toast = new Toast(Proximity.this);
        if (proximitySensor == null)
            finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, 2000*1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        toast.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        toast.cancel();
        finish();
    }

    @Override
    public final void onSensorChanged (SensorEvent event) {
        float distance = event.values[0];
        Event newEvent = new Event();
        newEvent.setDescription("Cambio de valor del sensor de proximidad registrado, nuevo valor: " + Float.toString(distance));
        newEvent.setState("ACTIVO");
        newEvent.setTypeEvents("SENSOR EVENT");
        Call<ResponseEvent> call = RetrofitClient
                .getInstance()
                .getService()
                .regEvent(token, "TEST", newEvent.getTypeEvents(), newEvent.getState(), newEvent.getDescription());


        if (distance > 0) {
            Log.i("RED", Float.toString(distance));
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else {
            Log.i("GREEN", Float.toString(distance));
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);

            if (savingEvent.getVisibility() != View.VISIBLE) {
                savingEvent.setVisibility(View.VISIBLE);
                saveEventText.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<ResponseEvent>() {
                    @Override
                    public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                        if (response.code() != 400 && response.code() != 401) {
                            Event eventSaved = response.body().getEvent();
                            list_events.add(eventSaved);
                            saveEvent();
                            Toast.makeText(Proximity.this, "Evento registrado en el servidor", Toast.LENGTH_LONG).show();
                            savingEvent.setVisibility(View.INVISIBLE);
                            saveEventText.setVisibility(View.INVISIBLE);
                        } else
                            Toast.makeText(Proximity.this, "Tiempo de espera terminado, vuelva a intentar nuevamente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseEvent> call, Throwable t) {
                        Toast.makeText(Proximity.this, t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
                    }
                });
            } else {
                return;
            }
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

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
