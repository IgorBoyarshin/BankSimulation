package com.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

public class Log extends Thread {
    private volatile Vector<String> strings;

    private final String fileName;
    private volatile boolean terminated;
    private long startedTimeMillis;

    // File logging
    private File file;
    private FileWriter fstream;
    private BufferedWriter out;

    public Log(String fileN) {
        fileName = fileN;
        strings = new Vector<String>();
        terminated = false;


        //String millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
        //fileName = hours + "_" + minutes + "_" + seconds + "_" + fileN;

        try {
            String workDir = System.getProperty("user.dir");
            //System.out.println(workDir);
            File file = new File(workDir + "\\log\\" + fileName + ".txt");



            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            fstream = new FileWriter(file.getAbsoluteFile());
            out = new BufferedWriter(fstream);

            // File is ready

            String hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
            String minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
            String seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
            String millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
            String startTimeLog = hours + " " + minutes + " " + seconds + " " + millis;

            startedTimeMillis = System.currentTimeMillis();
            out.write("[Log started at " + startTimeLog + "]");
            out.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Not sure if its safe for multiThreading
    public void log(String string) {
        long pastMillis = System.currentTimeMillis() - startedTimeMillis;
        strings.add("{" + pastMillis + "} " + string);
    }

    public void nextLine() {
        try {
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println("Log thread started");
        while (!terminated || (strings.size() != 0)) {
            if (strings.size() != 0) {
                try {
                    out.write(strings.get(0));
                    out.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                strings.remove(0);
            }
        }

        try {
            String hours = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
            String minutes = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
            String seconds = new SimpleDateFormat("ss").format(Calendar.getInstance().getTime());
            String millis = new SimpleDateFormat("SSS").format(Calendar.getInstance().getTime());
            String startTimeLog = hours + ":" + minutes + ":" + seconds + ":" + millis;

            out.write("[Log ended at " + startTimeLog + "]");

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Log thread terminated");
    }

    public void terminate() {
        //System.out.println("[" + fileName + "] Log is gonna terminate now");
        terminated = true;
    }

    /*
    public void makeRequest(LogRequest logRequest) {
        requests.add(logRequest);
    }

    private void processRequest(LogRequest logRequest) {
        switch(logRequest.getOperation()) {
            case LogRequest.OPERATION_CREATE_USER: {
                createNewLogUser(logRequest.getName)
                break;
            }
            case
        }
    }

    // Returns id for that user( for later access )
    private int createNewLogUser(String name) {
        users.add(new LogUser(name));
        return (users.size() - 1);
    }

    // Returns id for that field( for later access )
    private int createNewField(int idUser, String fieldName, long fieldValue) {
        users.get(idUser).addField(fieldName, fieldValue);
        return (users.get(idUser).getFieldsSize() - 1);
    }

    // Not cool coding!!!!!
    private void logFieldOfUser(int idUser, int idField) {
        LogUser user = users.get(idUser);

        try {
            out.write(
                    "[" + user.getUserName() + "] " + user.getFieldName(idField) + ": " + user.getFieldValue(idField));
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Not cool coding!!!!!!
    private void logAllFieldsOfUser(int idUser) {
        LogUser user = users.get(idUser);
        String name = user.getUserName();

        try {
            for (int idField = 0; idField < user.getFieldsSize(); idField++) {
                out.write(
                        "[" + name + "] " +
                                user.getFieldName(idField) + ": " +
                                user.getFieldValue(idField));
                out.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    public int addField(String name, long startValue) {
        LogItem logItem = new LogItem(name, startValue);
        fields.
    }

    public void incField(int id) {
        if (id < maxNumOfFields) {
            fields[id]++;
        }
    }

    public void setField(int id, int amount) {
        if (id < maxNumOfFields) {
            fields[id] = amount;
        }
    }

    public void listAllFields() {
        for (int i = 0; i < curNumOfFields; i++) {
            //System.out.println("[" + name + "] " + names[i] + ": " + fields[i]);

            try {
                out.write("[" + name + "] " + names[i] + ": " + fields[i]);
                out.newLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
