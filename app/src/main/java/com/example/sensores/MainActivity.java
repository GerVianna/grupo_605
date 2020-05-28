package com.example.sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(eventsButtons);
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

                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getService()
                            .loginUser(emailField, passwordField);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
            }
        }
    };

    private void loginUser (String email, String password) {

    }
}
