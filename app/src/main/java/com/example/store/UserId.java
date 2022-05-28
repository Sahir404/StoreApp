package com.example.store;

public class UserId {
    String username;
    String password;

    public UserId(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserId() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
