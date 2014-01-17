package com.bank;

public class Account {
    private final int id;
    private final int password;
    private int balance;

    public Account(final int id, final int password) {
        this.id = id;
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public boolean credit(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean deposit(int amount) {
        if (amount >= 0) {
            balance += amount;
            return true;
        }

        return false;
    }
}
