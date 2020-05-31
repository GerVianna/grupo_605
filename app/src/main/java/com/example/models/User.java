package com.example.models;

public class User {
    private String email;
    private String name;
    private String lastName;
    private int dni;

    public User(String email, String name, String lastName, String commission, String group, int dni ) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
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
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}
