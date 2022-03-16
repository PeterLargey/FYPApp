package com.example.fypapplication;

public class WaitingList {

    private String name;
    private String numberOfGuests;
    private String phoneNumber;
    private String waitTime;

    public WaitingList(){}

    public WaitingList(String name, String numberOfGuests, String phoneNumber, String waitTime){
        this.name = name;
        this.numberOfGuests = numberOfGuests;
        this.phoneNumber = phoneNumber;
        this.waitTime = waitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(String numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }
}
