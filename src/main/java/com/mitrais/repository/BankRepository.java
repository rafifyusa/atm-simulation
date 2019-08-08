package com.mitrais.repository;

import com.mitrais.model.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BankRepository {
    private List<Account> customers;

    private BankRepository() { initialize();}

    private void initialize() {
        Account johnAccount = new Account("John Doe", "332211", 100, "112233");
        Account janeAccount = new Account("Jane Doe", "442211", 30, "112244");

        List<Account> accounts = new ArrayList<>();
        accounts.add(johnAccount);
        accounts.add(janeAccount);

        this.customers = accounts;
    }

    public void loadData(String fileName) {
        List<Account> accounts = readAccountsFromCSV(fileName);
        accounts.addAll(this.customers);

        List<Account> filtered = findDuplicates(accounts);

        //printing all non-duplicate accounts if the csv file contain new customers
        if (!this.customers.equals(filtered)){
            for (Account a : filtered) {
                System.out.println("Successfully added :" + a);
            }
        }
        this.customers = filtered;
    }

    private static List<Account> readAccountsFromCSV(String fileName) {
        List<Account> accounts = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            String line = br.readLine();
            // loop until all lines are read
            while (line != null) {
                String[] attributes = line.split(",");

                Account account = createAccount(attributes);
                accounts.add(account);
                line = br.readLine();
            }

        } catch (IOException ioe) {
            System.out.println("Your file name is invalid, going back to transaction menu...");
        }
        return accounts;
    }

    private static Account createAccount(String[] metadata) {
        String name = metadata[0];
        String pin = metadata[1].replaceAll("\\s+", "");
        int balance = Integer.parseInt(metadata[2].replaceAll("\\s+", ""));
        String accountNumber = metadata[3].replaceAll("\\s+", "");

        return new Account(name, pin, balance, accountNumber);
    }

    private static List<Account> findDuplicates(List<Account> listContainingDuplicates)
    {
        final Set<String> accountNumbers = new HashSet<>();
        listContainingDuplicates.removeIf(account -> !accountNumbers.add(account.getAccountNumber()));
        return listContainingDuplicates;
    }

    private static class SingletonBankRepository
    {
        private static final BankRepository INSTANCE = new BankRepository();
    }

    public static BankRepository getInstance() {
        return SingletonBankRepository.INSTANCE;
    }

    public List<Account> getCustomers() {
        return customers;
    }
}
