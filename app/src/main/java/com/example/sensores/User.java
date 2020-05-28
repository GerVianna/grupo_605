package com.example.sensores;

public class User {
    private String email;
    private String name;
    private String lastName;
    private String commission;
    private String group;
    private String password;
    private int dni;

    public User(String email, String name, String lastName, String commission, String group, String password, int dni ) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.commission = commission;
        this.group = group;
        this.password = password;
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}
