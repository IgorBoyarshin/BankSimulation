package com.bank;

import com.user.User;

public class BankRequest {

    public static final int OPERATION_CREATE_ACCOUNT = 0;
    public static final int OPERATION_DEPOSIT = 1;
    public static final int OPERATION_CREDIT = 2;
    //public static final int OPERATION_CREATE_ACCOUNT = 3;

    private User user;
    private int amount;
    private int operation;

    // Sometimes the 'amount' filed is useless
    public BankRequest(User user, int operation, int amount) {
        this.user = user;
        this.amount = amount;
        this.operation = operation;
    }

    public int getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public int getOperation() {
        return operation;
    }
}
