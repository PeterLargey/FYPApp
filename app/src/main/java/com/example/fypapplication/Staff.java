package com.example.fypapplication;

public class Staff {

    private String fullName;
    private String username;
    private String role;
    private String password;
    private String phoneNum;
    private String wage;

    public Staff(){

    }

    public Staff(String username, String role, String password, String wage, String fullName, String phoneNum){
        this.username = username;
        this.role = role;
        this.password = password;
        this.wage = wage;
        this.fullName = fullName;
        this.phoneNum = phoneNum;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWage() {
        return wage;
    }

    public void setWage(String wage) {
        this.wage = wage;
    }


}
