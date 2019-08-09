package com.mitrais.service;

import com.mitrais.model.Account;
import com.mitrais.model.Transaction;
import com.mitrais.repository.BankRepository;

import java.util.*;
import java.util.stream.Collectors;

public class BankService {
    private BankRepository bankRepository = BankRepository.getInstance();
    public BankService() { }

    public Optional<Account> findCredential(String accountNumber, String pin) {
        List<Account> accounts = bankRepository.getCustomers();
        Optional<Account> account = accounts.stream()
                .filter( customer -> customer.getAccountNumber().equals(accountNumber))
                .filter( customer -> customer.getPin().equals(pin))
                .findAny();
        return account;
    }

    public Optional<Account> findCustomer(String accountNumber){
        List<Account> accounts = bankRepository.getCustomers();
        return accounts.stream()
                .filter(customer -> customer.getAccountNumber().equals(accountNumber))
                .findAny();
    }

    public List<Account> findAllCustomers() {
        return bankRepository.getCustomers();
    }

    public void loadCsvData(String fileName) {
        bankRepository.loadData(fileName);
    }

    public List<Transaction> findTenTransactionOfCustomer(String accountNumber) {
        Optional<Account> account = findCustomer(accountNumber);
        List<Transaction> transactionsHistory = new ArrayList<>();
        account.ifPresent(value -> transactionsHistory.addAll(value.getTransactionList().stream()
                .limit(10).collect(Collectors.toList())));
        return transactionsHistory;
    }

    public int getCurrentCustomerBalance(String accountNumber) {
        Optional<Account> customer = findCustomer(accountNumber);
        if (customer.isPresent()) {
            return customer.get().getBalance();
        }
        else {
            return 0;
        }
    }
}
