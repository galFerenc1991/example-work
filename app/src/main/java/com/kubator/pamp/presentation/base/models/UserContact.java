package com.kubator.pamp.presentation.base.models;

/**
 * Created by
 * Ferenc on 2017.12.01..
 */

public class UserContact {

    private String name;
    private String phoneNumber;

    public UserContact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
