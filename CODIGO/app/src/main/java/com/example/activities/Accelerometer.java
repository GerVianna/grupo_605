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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Event;
import com.example.models.GlobalUser;
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
    TextView saveEventText;
    ProgressBar savingEvent;
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    GlobalUser globalVariable;
    private Button goRegAccelerometer;
    float sensorValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        globalVariable = (GlobalUser) getApplicationContext();
        saveEventText = (TextView) findViewById(R.id.saveEventText2);
        savingEvent = (ProgressBar) findViewById(R.id.savingEvent2);
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
            Toast.makeText(Accelerometer.this, "Valor del sensor es " + sensorValue, Toast.LENGTH_SHORT).show();
            Event newEvent = new Event();
            newEvent.setDescription("Cambio de valor del sensor acelerometro registrado, nuevo valor: " + Float.toString(sensorValue));
            newEvent.setState("ACTIVO");
            newEvent.setTypeEvents("SENSOR EVENT");
            Call<ResponseEvent> call = RetrofitClient
                    .getInstance()
                    .getService()
                    .regEvent(globalVariable.getToken(), "DEV", newEvent.getTypeEvents(),newEvent.getState(),newEvent.getDescription());
            saveEventText.setVisibility(View.VISIBLE);
            savingEvent.setVisibility(View.VISIBLE);
            goRegAccelerometer.setClickable(false);
            call.enqueue(new Callback<ResponseEvent>(){
                @Override
                public void onResponse(Call<ResponseEvent> call, Response<ResponseEvent> response) {
                    if(response.code()!= 400 && response.code() != 401){
                        Event eventSaved = response.body().getEvent();
                        globalVariable.addEventToList(eventSaved);
                        globalVariable.saveEvent();
                        Toast.makeText(Accelerometer.this, "Evento registrado en el servidor", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Accelerometer.this, "No se pudo registrar el evento, vuelva a intentar nuevamente", Toast.LENGTH_SHORT).show();
                    }
                    saveEventText.setVisibility(View.INVISIBLE);
                    savingEvent.setVisibility(View.INVISIBLE);
                    goRegAccelerometer.setClickable(true);
                }

                @Override
                public void onFailure(Call<ResponseEvent> call, Throwable t) {
                    Toast.makeText(Accelerometer.this, "El servidor no respondió", Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
                    saveEventText.setVisibility(View.INVISIBLE);
                    savingEvent.setVisibility(View.INVISIBLE);
                    goRegAccelerometer.setClickable(true);
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


}
