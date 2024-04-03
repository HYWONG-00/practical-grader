package com.example.assignmentone;

import java.io.Serializable;

public abstract class User implements Serializable {
    private String username;
    private int pinNum;

    public User(String username,int pinNum){
        this.username = username;
        this.pinNum = pinNum;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){return this.username;}

    public void setPinNum(int pinNum){this.pinNum = pinNum;}

    public int getPinNum(){return this.pinNum;}
}

