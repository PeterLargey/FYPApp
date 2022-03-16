package com.example.fypapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItem implements Parcelable {

    private String name;
    private String type;
    private String desc;
    private String price;

    public MenuItem(){}

    public MenuItem(String type, String name, String desc, String price){
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    protected MenuItem(Parcel in) {
        name = in.readString();
        type = in.readString();
        desc = in.readString();
        price = in.readString();
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(desc);
        parcel.writeString(price);
    }
}
