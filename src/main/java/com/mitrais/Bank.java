package com.mitrais;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bank {
    private String name;
    private List<Account> customers;

    public Bank(String name) {
        this.name = name;
    }

    public void initialize() {
        Account johnAccount = new Account("John Doe", "332211", 100, "112233");
        Account janeAccount = new Account("Jane Doe", "932012", 30, "112244");

        List<Account> accounts = new ArrayList<>();
        accounts.add(johnAccount);
        accounts.add(janeAccount);

        this.customers = accounts;
    }

    public Optional<Account> findCredential(String accountNumber, String pin) {
        List<Account> accounts = this.customers;
        Optional<Account> account = accounts.stream()
                .filter( customer -> customer.getAccountNumber().equals(accountNumber))
                .filter( customer -> customer.getPin().equals(pin))
                .findAny();

        return account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Account> customers) {
        this.customers = customers;
    }
}
