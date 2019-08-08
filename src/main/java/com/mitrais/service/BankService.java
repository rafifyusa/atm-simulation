package com.mitrais.service;

import com.mitrais.model.Account;
import com.mitrais.repository.BankRepository;

import java.util.*;

public class BankService {

    public BankService() { }

    public Optional<Account> findCredential(String accountNumber, String pin) {
        List<Account> accounts = BankRepository.getInstance().getCustomers();
        Optional<Account> account = accounts.stream()
                .filter( customer -> customer.getAccountNumber().equals(accountNumber))
                .filter( customer -> customer.getPin().equals(pin))
                .findAny();
        return account;
    }

    public Optional<Account> findCustomer(String accountNumber){
        List<Account> accounts = BankRepository.getInstance().getCustomers();
        return accounts.stream()
                .filter(customer -> customer.getAccountNumber().equals(accountNumber))
                .findAny();
    }

    public List<Account> findAllCustomers() {
        return BankRepository.getInstance().getCustomers();
    }

    public void loadCsvData(String fileName) {
        BankRepository.getInstance().loadData(fileName);
    }
}
