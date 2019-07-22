package com.mitrais;

import com.mitrais.model.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Bank {
    private String name;
    private List<Account> customers;

    public Bank(String name) {
        this.name = name;
    }

    public void initialize() {
        /*Account johnAccount = new Account("John Doe", "332211", 100, "112233");
        Account janeAccount = new Account("Jane Doe", "932012", 30, "112244");

        List<Account> accounts = new ArrayList<>();
        accounts.add(johnAccount);
        accounts.add(janeAccount);

        this.customers = accounts;*/

        List<Account> accounts = readAccountsFromCSV("customers.csv");

        List<Account> filtered = findDuplicates(accounts);
        // let's print all the person read from CSV file
        for (Account a : filtered) {
            System.out.println(a);
        }
        this.customers = filtered;
    }

    public Optional<Account> findCredential(String accountNumber, String pin) {
        List<Account> accounts = this.customers;
        Optional<Account> account = accounts.stream()
                .filter( customer -> customer.getAccountNumber().equals(accountNumber))
                .filter( customer -> customer.getPin().equals(pin))
                .findAny();
        return account;
    }

    public Optional<Account> findAccount (String accountNumber){
        List<Account> accounts = this.customers;
        return accounts.stream()
                .filter(customer -> customer.getAccountNumber().equals(accountNumber))
                .findAny();
    }
    private static List<Account> readAccountsFromCSV(String fileName) {
        List<Account> accounts = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                Account account = createAccount(attributes);

                // adding book into ArrayList
                accounts.add(account);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return accounts;
    }

    private static Account createAccount(String[] metadata) {
        String name = metadata[0];
        String pin = metadata[1].replaceAll("\\s+", "");
        int balance = Integer.parseInt(metadata[2].replaceAll("\\s+", ""));
        String accountNumber = metadata[3].replaceAll("\\s+", "");

        // create and return book of this metadata
        return new Account(name, pin, balance, accountNumber);
    }

    private static List<Account> findDuplicates(List<Account> listContainingDuplicates)
    {
        final Set<String> accountNumbers = new HashSet<>();
        listContainingDuplicates.removeIf(account -> !accountNumbers.add(account.getAccountNumber()));
        return listContainingDuplicates;
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
