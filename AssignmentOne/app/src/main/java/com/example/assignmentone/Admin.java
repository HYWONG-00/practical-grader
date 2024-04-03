package com.example.assignmentone;

import java.io.Serializable;

public class Admin extends User implements Serializable {

    public Admin(String username, int pinNum) {
        super(username, pinNum);

    }
}
