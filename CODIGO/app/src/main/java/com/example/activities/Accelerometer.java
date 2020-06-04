package com.example.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Accelerometer extends AppCompatActivity implements SensorEventListener{
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    String token = "";
    ArrayList<Event> list_events;
    private Button goRegAccelerometer;
    float sensorValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        loadEvent();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null)
            finish();
        goRegAccelerometer = (Button) findViewById(R.id.btnRegEventA);
        goRegAccelerometer.setOnClickListener(eventsButtons);
    }

    private View.OnClickListener eventsButtons = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            token=intent.getStringExtra("token");
            Toast.makeText(Accelerometer.this, "Valor del sensor es " + sensorValue, Toast.LENGTH_SHORT).show();
            Event newEvent = new Event();
            newEvent.setDescription("Cambio de valor del sensor acelerometro registrado, nuevo valor: " + Float.toString(sensorValue));
            newEvent.setState("ACTIVO");
            newEvent.setTypeEvents("SENSOR EVENT");
            Call<ResponseEvent> call = RetrofitClient
                    .getInstance()
                    .getService()
                    .regEvent(token, "DEV", newEvent.getTypeEvents(),newEvent.getState(),newEvent.getDescription());
            call.enqueue(new Callback<ResponseEvent>(){
                @Override
                public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                    if(response.code()!= 400 && response.code() != 401){
                        Event eventSaved = response.body().getEvent();
                        list_events.add(eventSaved);
                        saveEvent();
                        Toast.makeText(Accelerometer.this, "Evento registrado en el servidor", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(Accelerometer.this, "No se pudo registrar el evento, vuelva a intentar nuevamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseEvent> call, Throwable t) {
                    Toast.makeText(Accelerometer.this, t.toString(), Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accelerometerSensor,2000*1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);
        sensorValue = sensorEvent.values[0];
        if (sensorValue < -2) {
            getWindow().getDecorView().setBackgroundColor(Color.RED);
            Log.d("X<-2", String.valueOf(sensorValue));
        }
        if (sensorValue > 2) {
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            Log.d("X<2", String.valueOf(sensorValue));
        }
        if(sensorValue>-2 && sensorValue<2){
            getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void saveEvent(){
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list_events);
        editor.putString("list data", json);
        editor.apply();
    }

    private void loadEvent(){
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list data", null);
        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
        list_events = gson.fromJson(json, type);

        if(list_events == null){
            list_events = new ArrayList<>();
        }
    }

}
