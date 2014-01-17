package com.user;

public class User {
    private final String name;
    private int accountId;
    private int accountPassword;

    public User(String name) {
        this.name = name;
    }

    public void createAccount(int id, int password) {
        accountId = id;
        accountPassword = password;


    }

    public String getName() {
        return name;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getAccountPassword() {
        return accountPassword;
    }
}
