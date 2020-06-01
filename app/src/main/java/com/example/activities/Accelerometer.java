package com.example.activities;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sensores.R;

public class Accelerometer extends AppCompatActivity {
SensorManager sensorManager;
Sensor sensor;
SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null)
            finish();

        sensorEventListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                if (x < -5) {
                    soundOne();
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    Log.d("X<-5", String.valueOf(x));
                }
                if (x > 5) {
                    soundTwo();
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                    Log.d("X<5", String.valueOf(x));
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        start();
    }
    private void soundOne(){
        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.water1);
        mediaPlayer.start();
    }
    private void soundTwo(){
        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.water2);
        mediaPlayer.start();
    }

 private void start(){
        sensorManager.registerListener(sensorEventListener,sensor,2000*1000);
 }
  private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
  }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
}
