package com.example.activities;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.models.LoginRequest;
import com.example.sensores.R;
import com.example.services.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText dni;
    private EditText commission;
    private EditText name;
    private EditText lastName;
    private Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        email = (EditText) findViewById(R.id.emailReg);
        password = (EditText) findViewById(R.id.passwordReg);
        dni = (EditText) findViewById(R.id.dniReg);
        commission = (EditText) findViewById(R.id.commissionReg);
        name = (EditText) findViewById(R.id.nameReg);
        lastName = (EditText) findViewById(R.id.lastNameReg);
        reg = (Button) findViewById(R.id.regButton);
        reg.setOnClickListener(eventsButtons);
    }

    private View.OnClickListener eventsButtons = new View.OnClickListener() {
        public void onClick (View v) {
            String emailField = email.getText().toString().trim();
            String passwordField = password.getText().toString().trim();
            String dniField =  dni.getText().toString().trim();
            String commissionField = commission.getText().toString().trim();
            String nameField = name.getText().toString().trim();
            String lastNameField = lastName.getText().toString().trim();
            switch (v.getId()) {
                case R.id.regButton:
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

                    if (dniField.isEmpty()) {
                        dni.setError("Dni es requerido");
                        dni.requestFocus();
                        return;
                    }

                    if (commissionField.isEmpty()) {
                        commission.setError("La comision del alumno es requerida");
                        commission.requestFocus();
                        return;
                    }

                    if (nameField.isEmpty()) {
                        name.setError("El nombre es requerido");
                        name.requestFocus();
                        return;
                    }

                    if (lastNameField.isEmpty()) {
                        lastName.setError("El apellido es requerido");
                        lastName.requestFocus();
                        return;
                    }

                    LoginRequest req = new LoginRequest("DEV", nameField,  lastNameField, Integer.parseInt(dniField), emailField, passwordField, Integer.parseInt(commissionField));

                    Call<LoginRequest> call = RetrofitClient
                            .getInstance()
                            .getService()
                            .regUser(req.getEnv(), req.getName(), req.getLastName(), req.getDni(), req.getEmail(), req.getPassword(), req.getCommission(), req.getGroup());


                    call.enqueue(new Callback<LoginRequest>() {
                        @Override
                        public void onResponse(Call<LoginRequest> call, Response<LoginRequest> response) {
                            Log.i("error", Integer.toString(response.code()));
                            if (response.code() == 400) {
                                Log.i("Error", "Fallo todo");
                            } else {
                                Log.i("success", response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginRequest> call, Throwable t) {
                            Log.i("Error", "hola");
                        }
                    });
            }
        }
    };
}
