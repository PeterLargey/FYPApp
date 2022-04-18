package com.example.fypapplication;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Order {

    private String tableNo;
    private String timestamp;
    private String staffMember, total;
    private String note;
    private List<MenuItem> items;
    private String staffName;

    public Order(){}

    public Order(String tableNo, String timestamp, String staffMember, List<MenuItem> items, String total, String note, String staffName){
        this.tableNo = tableNo;
        this.timestamp = timestamp;
        this.staffMember = staffMember;
        this.items = items;
        this.total = total;
        this.note = note;
        this.staffName = staffName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }


    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(String staffMember) {
        this.staffMember = staffMember;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
