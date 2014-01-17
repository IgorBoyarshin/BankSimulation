package com.bank;

import com.Main;
import com.log.Log;
import com.log.LogPackage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/*

    make the 'requests' volatile!!!

 */

public class Bank extends Thread {
    private int balance;
    private Vector<Account> accounts;
    private volatile Vector<BankRequest> requests;

    private volatile boolean terminated;

    // Analytics
    private final String className = "Bank";
    private long performedOperations = 0;
    private long createOperations = 0;
    private long creditOperations = 0;
    private long depositOperations = 0;
    private long smthWentWrong = 0;

    private volatile boolean requestsEnded = false;

    public Log log;// = new Log(className);

    public int getBalance() {
        return balance;
    }

    public void reportRequestsEnded() {
        requestsEnded = true;
        log("Received requestsEnded. Unprocessed left: " + requests.size()
                + ".   Performed already: " + performedOperations);
    }

    public void makeRequest(BankRequest bankRequest) {
        requests.add(bankRequest);
    }

    public void terminate() {
        terminated = true;
        log("Received requestsEnded. Unprocessed left: " + requests.size()
                + ".   Performed already: " + performedOperations);
        // Ought not to be here, but still...
    }

    public Bank(int balance) {
        log = new Log(Main.getStartTimeLog() + "_" + className);
        this.balance = balance;
        log.start();
        terminated = false;

        log("Starting the system");

        accounts = new Vector<Account>();
        requests = new Vector<BankRequest>();
    }

    private void log(String string) {
        String str = "[" + className + "] " + string;
        log.log(str);
    }

    private void log(String name, long amount) {
        String string = "[" + className + "] " + name + ": " + amount;
        log.log(string);
    }

    private boolean isRequestValidForTransactions(BankRequest bankRequest) {
        if (bankRequest.getUser().getAccountId() >= accounts.size()) {
            smthWentWrong++;
            return false;
        }
        return true;
    }

    private int processRequest(BankRequest bankRequest) {

        /*
        if (bankRequest.getUser().getAccountId() >= accounts.size()) {
            return; // Can not handle the bankRequest
        }
        */

        /*
        com.bank.Account account = accounts.get(0);
        int iterator = 0;
        int size = accounts.size();
        int id = bankRequest.getUser().getAccountId();

        while ((iterator + 1 < size) && (account.getId() != id)) {
            ++iterator;
            account = accounts.get(iterator);
        }

        if (id != account.getId()) {
            return; // Can not handle the bankRequest
        }
        */

        performedOperations++;


        // WRONG!!!!!!
        int res = 0;

        // WRONG!!!!!!
        switch (bankRequest.getOperation()) {
            case BankRequest.OPERATION_CREATE_ACCOUNT: {
                createOperations++;

                //System.out.println("Type: CREATE_ACCOUNT.");
                accounts.add(new Account(accounts.size(), 1234));
                res = accounts.size() - 1;
                break;
            }
            case BankRequest.OPERATION_CREDIT: {
                //System.out.println("Type: CREDIT.");
                creditOperations++;

                if (isRequestValidForTransactions(bankRequest)) {
                    int amount = bankRequest.getAmount(); // WRONG!!!!!!
                    int id = bankRequest.getUser().getAccountId();

                    accounts.get(id).credit(amount);
                }
                break;
            }
            case BankRequest.OPERATION_DEPOSIT: {
                //System.out.println("Type: DEPOSIT.");
                depositOperations++;

                if (isRequestValidForTransactions(bankRequest)) {
                    int amount = bankRequest.getAmount(); // WRONG!!!!!!
                    int id = bankRequest.getUser().getAccountId();

                    accounts.get(id).deposit(amount);
                }
                break;
            }
        }
        return res;
    }

    @Override
    public void run() {
        System.out.println("Bank thread started");

        String hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        String seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
        String millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
        String curTime = hours + "_" + minutes + "_" + seconds + "_" + millis;
        log("Started at: " + curTime);

        log("Starting the main loop");

        while ((!terminated) || (requests.size() != 0)) {

            if (requests.size() != 0) {
                //System.out.print("[com.bank.Bank]: Processing a request. Name:" + requests.get(0).getUser().getName() + ". ");
                processRequest(requests.get(0));
                requests.remove(0);
            }
        }

        hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
        millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
        curTime = hours + "_" + minutes + "_" + seconds + "_" + millis;
        log("Terminated at: " + curTime);

        //log.setField(4, requests.size());

        //log.listAllFields();

        LogPackage logPackage = new LogPackage(performedOperations, createOperations, creditOperations,
                depositOperations, requests.size());
        Main.bankAnalytics = logPackage;


        log.nextLine();
        log("Analytics:");
        log("Performed operations", performedOperations);
        log("Create operations", createOperations);
        log("Credit operations", creditOperations);
        log("Deposit operations", depositOperations);
        log("Unprocessed", requests.size());
        log("Something went wrong", smthWentWrong);
        log.nextLine();

        log("Terminating");

        /*
        System.out.println("[Bank] Performed operations: " + performedOperations);
        System.out.println("[Bank] Create operations: " + createOperations);
        System.out.println("[Bank] Credit operations: " + creditOperations);
        System.out.println("[Bank] Deposit operations: " + depositOperations);
        System.out.println("[Bank] Unprocessed left: " + requests.size());
        System.out.println("[Bank] Something went wrong: " + smthWentWrong);
        System.out.println("[Bank]: Terminating");
        */

        log.terminate();

        System.out.println("Bank thread terminated");
    }
}