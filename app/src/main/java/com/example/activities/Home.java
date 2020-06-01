package com.example.activities;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.LoginRequest;
import com.example.models.ResponseLogin;
import com.example.sensores.R;
import com.example.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class Home extends AppCompatActivity {

    private Button goAccelerometer;
    private Button goProximity;
    private Button goEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            switch(v.getId()) {
                case R.id.buttonAcceler:
                    intent.setClass(Home.this, Accelerometer.class);
                    break;
                case R.id.buttonProx:
                    intent.setClass(Home.this, Proximity.class);
                    break;
                case R.id.buttonTableEvents:
                    intent.setClass(Home.this, TableEvents.class);
                    break;
            }
            startActivity(intent);
        }
    };



}
