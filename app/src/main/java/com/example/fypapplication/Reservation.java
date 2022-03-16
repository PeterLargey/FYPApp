package com.example.fypapplication;

public class Reservation {

    private String name;
    private String numberOfGuests;
    private String time;
    private String date;

    public Reservation(){}

    public Reservation(String name, String numberOfGuests, String time, String date){
        this.name = name;
        this.numberOfGuests = numberOfGuests;
        this.time = time;
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(String numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
