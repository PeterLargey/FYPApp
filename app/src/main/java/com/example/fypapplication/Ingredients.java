package com.example.fypapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    private String name;
    private String amount;

    public Ingredients(){}

    public Ingredients(String name, String amount){
        this.name = name;
        this.amount = amount;
    }

    protected Ingredients(Parcel in) {
        name = in.readString();
        amount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
