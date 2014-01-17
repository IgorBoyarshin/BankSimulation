package com.log;

public class LogPackage {
    private long performedOperations;
    private long createOperations;
    private long creditOperations;
    private long depositOperations;
    private long unprocessedOperations;

    public LogPackage(long performedOperations, long createOperations, long creditOperations, long depositOperations) {
        this.performedOperations = performedOperations;
        this.createOperations = createOperations;
        this.creditOperations = creditOperations;
        this.depositOperations = depositOperations;
        this.unprocessedOperations = -1;
    }

    public LogPackage(long performedOperations, long createOperations, long creditOperations,
                      long depositOperations, long unprocessedOperations) {
        this.performedOperations = performedOperations;
        this.createOperations = createOperations;
        this.creditOperations = creditOperations;
        this.depositOperations = depositOperations;
        this.unprocessedOperations = unprocessedOperations;
    }

    public long getPerformedOperations() {
        return performedOperations;
    }

    public long getCreateOperations() {
        return createOperations;
    }

    public long getCreditOperations() {
        return creditOperations;
    }

    public long getDepositOperations() {
        return depositOperations;
    }

    public long getUnprocessedOperations() {
        return unprocessedOperations;
    }
}
