package com.example.fypapplication;

public class Review {

    private String title;
    private String desc;
    private String rating;
    private String user;

    public Review() {}

    public Review(String title, String desc, String rating, String user){
        this.title = title;
        this.desc = desc;
        this.rating = rating;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
