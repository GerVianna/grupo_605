package com.example.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.Event;
import com.example.models.LoginRequest;
import com.example.models.ResponseLogin;
import com.example.sensores.R;
import com.example.services.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button goReg;
    ArrayList<Event> list_events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadEvent();
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        login = (Button) findViewById(R.id.loginButton);
        goReg = (Button) findViewById(R.id.goRegButton);
        login.setOnClickListener(eventsButtons);
        goReg.setOnClickListener(eventsButtons);
    }

    private View.OnClickListener eventsButtons = new View.OnClickListener() {
        public void onClick (View v) {
            String emailField = email.getText().toString().trim();
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


                    call.enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                            if(response.code() != 400) {
                                response.body();
                                String env = response.body().getEnv();
                                String state = response.body().getState();
                                String token = response.body().getToken();
                                Event event = new Event();
                                event.setTypeEvents("LOGIN");
                                event.setState("ACTIVO");
                                event.setDescription("Usuario logueado en el sistema");
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.putExtra("token", token);
                                MainActivity.this.startActivity(intent);
                            }else   Toast.makeText(MainActivity.this, "No fue posible realizar el login, intente nuevamente",Toast.LENGTH_SHORT).show();
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


    private void startRegActivity() {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
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
