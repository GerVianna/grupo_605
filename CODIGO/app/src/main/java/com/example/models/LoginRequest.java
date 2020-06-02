package com.example.models;

import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("env")
    @Expose
    private String env;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("dni")
    @Expose
    private int dni;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("commission")
    @Expose
    private int commission;
    @SerializedName("group")
    @Expose
    private final int group = 605;

    public LoginRequest(String env, String name, String lastname, int dni, String email, String password, int commission ) {
        this.env = env;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.dni = dni;
        this.commission = commission;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public int getGroup() {
        return group;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getEnv() { return env; }

    public void setEnv(String env) { this.env = env; }

    public int getCommission() { return commission; }

    public void setCommission(int commission) { this.commission = commission; }
}
