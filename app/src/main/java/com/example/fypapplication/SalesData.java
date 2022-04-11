package com.example.fypapplication;

public class SalesData {

    private String time;
    private String total;

    public SalesData(){}

    public SalesData(String time, String total){
        this.time = time;
        this.total = total;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
