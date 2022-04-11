package com.example.fypapplication;

public class Roster {

    private String firstName;
    private String lastName;
    private String username;
    private String role;
    private String time;
    private String date;

    public Roster(){}

    public Roster(String firstName, String lastName, String username, String role, String time, String date){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.time = time;
        this.date = date;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
