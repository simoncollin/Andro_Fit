package com.cnamge.fipinfo.androfit.model;

import com.orm.SugarRecord;

public class User extends SugarRecord<User> {
    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }
}
