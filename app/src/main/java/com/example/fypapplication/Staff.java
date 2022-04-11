package com.example.fypapplication;

public class Staff {

    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private String password;
    private double wage;

    public Staff(){

    }

    public Staff(String username, String role, String password, double wage, String firstName, String lastName){
        this.username = username;
        this.role = role;
        this.password = password;
        this.wage = wage;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
