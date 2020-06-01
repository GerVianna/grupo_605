package com.example.activities;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Event;
import com.example.models.LoginRequest;
import com.example.models.ResponseEvent;
import com.example.models.ResponseLogin;
import com.example.sensores.R;
import com.example.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class Proximity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor proximitySensor;
    SensorEventListener sensorEventListener;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null)
            finish();

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, proximitySensor, 2000*1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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


        call.enqueue(new Callback<ResponseEvent>() {
            @Override
            public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                if(response.code() != 400 && response.code() != 401) {
                    String env = response.body().getEnv();
                    String state = response.body().getState();
                    Event eventSaved = response.body().getEvent();
                    Log.i("event", env);
                    Log.i("event", state);
                    Log.i("event", eventSaved.getDescription());
                    Log.i("event", eventSaved.getState());
                    Log.i("event", eventSaved.getTypeEvents());
                }else   Toast.makeText(Proximity.this, response.errorBody().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseEvent> call, Throwable t) {
                Toast.makeText(Proximity.this ,t.toString(),Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
            }
        });

        if (distance > 0) {
            Log.i("RED", Float.toString(distance));
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        } else {
            Log.i("GREEN", Float.toString(distance));
            getWindow().getDecorView().setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }


}
