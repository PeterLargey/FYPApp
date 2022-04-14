package com.example.fypapplication;

public class Roster {

    private String fullName;
    private String username;
    private String role;
    private String time;
    private String date;

    public Roster(){}

    public Roster(String fullName, String username, String role, String time, String date){
        this.fullName = fullName;
        this.username = username;
        this.role = role;
        this.time = time;
        this.date = date;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
