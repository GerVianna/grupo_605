package com.example.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.models.Event;
import com.example.models.GlobalUser;
import com.example.models.LoginRequest;
import com.example.models.ResponseLogin;
import com.example.receivers.NetworkState;
import com.example.sensores.R;
import com.example.services.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver networkState;
    private EditText email;
    private EditText password;
    private Button login;
    private Button goReg;
    private ProgressBar progressBar;
    GlobalUser globalVariable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalVariable = (GlobalUser) getApplicationContext();
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        login = (Button) findViewById(R.id.loginButton);
        goReg = (Button) findViewById(R.id.goRegButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        login.setOnClickListener(eventsButtons);
        goReg.setOnClickListener(eventsButtons);
    }

    private View.OnClickListener eventsButtons = new View.OnClickListener() {
        public void onClick (View v) {
            final String emailField = email.getText().toString().trim();
            String passwordField = password.getText().toString().trim();
            switch (v.getId()) {
                case R.id.loginButton:
                    if (emailField.isEmpty()) {
                        email.setError("El email es requerido");
                        email.requestFocus();
                        return;
                    }

                    if (passwordField.isEmpty()) {
                        password.setError("Contraseña es requerida");
                        password.requestFocus();
                        return;
                    }

                    if (passwordField.length() < 8) {
                        password.setError("La contraseña debe ser mínimo 8 caracteres");
                        password.requestFocus();
                        return;
                    }

                    LoginRequest req = new LoginRequest("DEV", "", "", 0, emailField, passwordField, 0);
                    Call<ResponseLogin> call = RetrofitClient
                            .getInstance()
                            .getService()
                            .loginUser(req.getEnv(), req.getName(), req.getLastName(), req.getDni(), req.getEmail(), req.getPassword(), req.getCommission(), req.getGroup());

                    progressBar.setVisibility(View.VISIBLE);
                    call.enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if(response.code() != 400) {
                                String token = response.body().getToken();
                                globalVariable.setEmail(emailField);
                                globalVariable.setToken(token);
                                globalVariable.loadEvent();
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                progressBar.setVisibility(View.INVISIBLE);
                                MainActivity.this.startActivity(intent);
                                finish();
                            }else  {
                                password.setError("Correo electrónico o contraseña incorrecto/s");
                                password.requestFocus();
                            };
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            Toast.makeText(MainActivity.this ,t.toString(),Toast.LENGTH_SHORT).show(); // ALL NETWORK ERROR HERE
                        }
                    });
                    break;
                case R.id.goRegButton:
                    startRegActivity();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkState = new NetworkState();
        IntentFilter intent = new IntentFilter();
        intent.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver((BroadcastReceiver) networkState, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterNetworkChanges();
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkState);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void startRegActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        unregisterNetworkChanges();
    }


}
