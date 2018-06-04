package ru.eamosov.n26.api;

/**
 * Transaction
 */
public class Transaction {
    private double amount;
    private long timestamp;

    public Transaction() {

    }

    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "amount=" + amount +
            ", timestamp=" + timestamp +
            '}';
    }
}
