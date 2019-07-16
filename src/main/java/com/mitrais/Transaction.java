package com.mitrais;

import java.time.LocalDateTime;

public class Transaction {
    public enum Type {WITHDRAW, TRANSFER}

    private LocalDateTime transactionDateTime;
    private Type type;
    private int amount;
    private Account to;

    public Transaction(LocalDateTime transactionDateTime, Type type, int amount, Account to) {
        this.transactionDateTime = transactionDateTime;
        this.type = type;
        this.amount = amount;
        this.to = to;
    }

    public Transaction(LocalDateTime transactionDateTime, Type type, int amount) {
        this.transactionDateTime = transactionDateTime;
        this.type = type;
        this.amount = amount;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }
}
