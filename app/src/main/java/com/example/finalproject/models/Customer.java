package com.example.finalproject.models;

public class Customer {
    private String userName;

    private String phone;
    public Customer(){

    }

    public Customer(String userName, String phone) {
        this.phone = phone;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone =phone;
    }

}
