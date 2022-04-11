package com.example.fypapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MenuItem implements Parcelable {

    private String name;
    private String type;
    private String desc;
    private String price;
    private List<Ingredients> ingredients;
    private String costPerUnit;

    public MenuItem(){}

    public MenuItem(String type, String name, String desc, String price, List<Ingredients> ingredients, String costPerUnit){
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.ingredients = ingredients;
        this.costPerUnit = costPerUnit;
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

    public String getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(String costPerUnit) {
        this.costPerUnit = costPerUnit;
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

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }
}
