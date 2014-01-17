package com;

import com.bank.Bank;
import com.log.Log;
import com.log.LogPackage;
import com.user.UserHandler;
import org.omg.DynamicAny._DynEnumStub;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

    private static final String className = "Main";
    private static Log log;// = new Log(className);
    public static Bank bank;// = new Bank(0);
    public static UserHandler userHandler;// = new UserHandler(bank);
    public static volatile LogPackage bankAnalytics;
    public static volatile LogPackage uhAnalytics;
    private static volatile String startTimeLog;

    private static String description;
    private static final int timeRunning = 2000;

    public static String getStartTimeLog() {
        return startTimeLog;
    }

    private static void log(String string) {
        //String str = "[" + className + "] " + string;
        String str = string;
        log.log(str);
    }

    private static void log(String name, long amount) {
        //String string = "[" + className + "] " + name + ": " + amount;
        String string = name + ": " + amount;
        log.log(string);
    }

    private static void analise() {
        log.nextLine();
        log("Analytics:");

        LogPackage bank = bankAnalytics;
        LogPackage uh = uhAnalytics;
        long diff = uh.getPerformedOperations() - bank.getPerformedOperations();
        log("          Bank        UserHandler");
        log("Processed:    " + bank.getPerformedOperations() + "        " + uh.getPerformedOperations()
                + "   Diff: " + diff);
        log("Created:      " + bank.getCreateOperations() + "        " + uh.getCreateOperations());
        log("Credit:       " + bank.getCreditOperations() + "        " + uh.getCreditOperations());
        log("Deposit:      " + bank.getDepositOperations() + "        " + uh.getDepositOperations());
        log("Unprocessed:  " + bank.getUnprocessedOperations());
        log.nextLine();
        log.flush();

        long unP = uh.getPerformedOperations() - bank.getPerformedOperations();
        if (unP == bank.getUnprocessedOperations()) {
            log("Unprocessed matches");
        } else {
            log("Unprocessed doesn't match. Difference: " + (unP - bank.getUnprocessedOperations()));
        }

        long depBigger = uh.getDepositOperations() - uh.getCreditOperations();
        if (depBigger >= 0) {
            log("Deposit(UH) is bigger or equals by: " + depBigger);
        } else {
            log("Deposit(UH) is smaller by: " + (-depBigger));
        }

        //log("Left space: " + (Long.MAX_VALUE - uh.getPerformedOperations()));

        log.nextLine();
        log.flush();
    }

    public static void main(String[] args) {

        description = "Description: " + "Testing some crazy hardcore!!!!!" +
                "Testing overprocessing by Bank. " +
                "Works until requests == 0.";

        System.out.println("Main thread started");

        String hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        String seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
        startTimeLog = hours + "_" + minutes + "_" + seconds;

        log = new Log(startTimeLog + "_" + className);

        bank = new Bank(0);
        userHandler = new UserHandler(bank);

        log.start();
        bank.start();
        userHandler.start();



        log("Running time", timeRunning);

        log(description);

        // The system will run for 'timeRunning' millis
        System.out.println("Left: " + (timeRunning) + " millis");
        try {
            Thread.sleep((int) timeRunning / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep((int) timeRunning / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Left: " + (timeRunning - (timeRunning/4)) + " millis");
        try {
            Thread.sleep((int) timeRunning / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Left: " + (timeRunning - (timeRunning/2)) + " millis");
        try {
            Thread.sleep((int) timeRunning / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Left: " + (timeRunning - (timeRunning/4*3)) + " millis");
        try {
            Thread.sleep((int) timeRunning / 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        userHandler.terminate();
        bank.terminate();

        while (((bankAnalytics == null) || (uhAnalytics == null))) {
            // Wait for them to terminate
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        analise();

        /*
        String s = "Bank unprocessed: " + bankAnalytics.getUnprocessedOperations();
        String s1 = "UserHandler performed: " + uhAnalytics.getPerformedOperations();
        String s2 = "Bank performed: " + bankAnalytics.getPerformedOperations();
        long r = uhAnalytics.getPerformedOperations() - bankAnalytics.getPerformedOperations() -
                bankAnalytics.getUnprocessedOperations();
        //log.nextLine();
        log("Analytics:");
        log(s);
        log(s1);
        log(s2);
        log("Should be zero: ", r);
        //log.nextLine();
        */

        log.terminate();

        System.out.println("Main thread terminated");
    }
}