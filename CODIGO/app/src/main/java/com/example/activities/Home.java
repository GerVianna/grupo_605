package com.example.activities;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sensores.R;

public class Home extends AppCompatActivity {

    private Button goAccelerometer;
    private Button goProximity;
    private Button goEvents;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Log.i("token", token);
        goAccelerometer = (Button) findViewById(R.id.buttonAcceler);
        goProximity = (Button) findViewById(R.id.buttonProx);
        goEvents = (Button) findViewById(R.id.buttonTableEvents);
        goAccelerometer.setOnClickListener(eventsButtons);
        goProximity.setOnClickListener(eventsButtons);
        goEvents.setOnClickListener(eventsButtons);
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



}
