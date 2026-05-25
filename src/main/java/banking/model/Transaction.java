package banking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private final LocalDateTime timestamp;
    private final TransactionType type;
    private final double amount;
    private final String note;
    private final double balanceAfter;

    public Transaction(TransactionType type, double amount, String note, double balanceAfter) {
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}
