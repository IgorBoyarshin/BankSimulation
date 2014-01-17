package com.user;

import com.Main;
import com.bank.Bank;
import com.bank.BankRequest;
import com.log.Log;
import com.log.LogPackage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

public class UserHandler extends Thread {
    private Vector<User> users;
    public Bank bank;
    private Random random;

    // Analytics
    private static final String className = "UserHandler";
    private Log log;// = new Log(className);
    private long performedOperations = 0;
    private long createOperations = 0;
    private long creditOperations = 0;
    private long depositOperations = 0;

    private final long maxUsers = 10000;

    private volatile boolean terminated;

    public void terminate() {
        terminated = true;
    }

    public UserHandler(Bank bank) {
        log = new Log(Main.getStartTimeLog() + "_" + className);
        this.bank = bank;
        log.start();
        log("Starting the system");
        random = new Random();
        terminated = false;
        users = new Vector<User>();

//        users.add(new com.user.User("Stephan"));
//        users.add(new com.user.User("Johnson"));
//        users.add(new com.user.User("Alex"));
//
//        for (int i = 0; i < users.size(); i++) {
//            com.bank.BankRequest request = new com.bank.BankRequest(users.get(i), com.bank.BankRequest.OPERATION_CREATE_ACCOUNT, 0);
//            bank.makeRequest(request);
//        }
    }

    private void log(String string) {
        String str = "[" + className + "] " + string;
        log.log(str);
    }

    private void log(String name, long amount) {
        String string = "[" + className + "] " + name + ": " + amount;
        log.log(string);
    }

    @Override
    public void run() {
        System.out.println("UserHandler thread started");

        String hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        String seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
        String millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
        String curTime = hours + "_" + minutes + "_" + seconds + "_" + millis;
        log("Started at: " + curTime);

        log("Starting the main loop");
        while (!terminated) {

            performedOperations++;
            //System.out.println("[com.user.UserHandler]: Gonna send some.");
            int num = random.nextInt(users.size() + 1);
            switch (num) {
                case 0: {
                    if (users.size() < maxUsers) {
                        createOperations++;
                        User user = new User("Peter");
                        users.add(user);

                        BankRequest bankRequest = new BankRequest(user, BankRequest.OPERATION_CREATE_ACCOUNT, 0);
                        bank.makeRequest(bankRequest);
                    }
                    break;
                }
                default: {
                    User user = users.get(num - 1);

                    BankRequest bankRequest;
                    if (num % 2 == 0) {
                        creditOperations++;
                        bankRequest = new BankRequest(user, BankRequest.OPERATION_CREDIT, (num * 100) % 64);
                    } else {
                        depositOperations++;
                        bankRequest = new BankRequest(user, BankRequest.OPERATION_DEPOSIT, (num * 100) % 64);
                    }

                    bank.makeRequest(bankRequest);
                    break;
                }
            }
        }

        hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
        millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
        curTime = hours + "_" + minutes + "_" + seconds + "_" + millis;
        log("Terminated at: " + curTime);

        // Telling the bank that we are done sending requests
        //bank.reportRequestsEnded();

        // Creating a LopPackage for Main
        LogPackage logPackage = new LogPackage(performedOperations, createOperations, creditOperations,
                depositOperations);
        Main.uhAnalytics = logPackage;

        log.nextLine();
        log("Analytics:");
        log("Performed operations", performedOperations);
        log("Create operations", createOperations);
        log("Credit operations", creditOperations);
        log("Deposit operations", depositOperations);
        log.nextLine();
        log("Terminating");

        /*
        System.out.println("[com.user.UserHandler] Performed operations: " + performedOperations);
        System.out.println("[com.user.UserHandler] Create operations: " + createOperations);
        System.out.println("[com.user.UserHandler] Credit operations: " + creditOperations);
        System.out.println("[com.user.UserHandler] Deposit operations: " + depositOperations);
        System.out.println("[com.user.UserHandler] Users size: " + users.size());
        System.out.println("[com.user.UserHandler]: Terminated");
        */

        log.terminate();
        System.out.println("UserHandler thread terminated");
    }
}