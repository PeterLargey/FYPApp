package com.example.fypapplication;

public class Roster {

    private String name;
    private String role;
    private String time;
    private String date;

    public Roster(){}

    public Roster(String name, String role, String time, String date){
        this.name = name;
        this.role = role;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
