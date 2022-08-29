package com.example.navdrawertest10;

public class ModelAccounts {
    String id, username, password;
    public ModelAccounts(){}

    public ModelAccounts(String id, String password, String username) {
        this.id=id;
        this.username=username;
        this.password=password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
