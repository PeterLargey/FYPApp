package com.example.fypapplication;

public class InventoryItem {

    private String name;
    private String desc;
    private String units;
    private String expiry;

    public InventoryItem(){}

    public InventoryItem(String name, String desc, String units, String expiry){
        this.name = name;
        this.desc = desc;
        this.units = units;
        this.expiry = expiry;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
