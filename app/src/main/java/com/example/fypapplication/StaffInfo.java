package com.example.fypapplication;

public class StaffInfo {

    private String name;
    private String wage;

    public StaffInfo(){}

    public StaffInfo(String name, String wage){
        this.name = name;
        this.wage = wage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }
}
