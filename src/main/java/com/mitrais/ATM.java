package com.mitrais;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ATM {
    private Bank bank;
    private String name;
    private Account userAccount;
    private boolean idIsValid = false;
    private boolean pinIsValid = false;

    public ATM(Bank bank) { this.bank = bank;}

    public void main() {
        boolean running = true;
        do{
            Optional<Account> user = this.showWelcomeScreen();
            user.ifPresent(this::showTransactionMenu);

        }
        while (running);
    }

    public Optional<Account> showWelcomeScreen() {
        Scanner in = new Scanner(System.in);

        String accountId;
        String accountPin;

        System.out.println("=================================================");
        System.out.println("Welcome to Mitrais Bank");
        System.out.println("Please insert your account detail to continue...");
        System.out.println("=================================================");

        do {
            System.out.println("Enter account number: ");
            accountId = in.nextLine();
            if (accountId.length() != 6){
                System.out.println("Account Number should have 6 digits length");
            }
            else if (!accountId.matches("[0-9]+") ) {
                System.out.println("Account Number should only contains numbers");
            }
            else {
                idIsValid = true;
                break;
            }
        }
        while (!idIsValid);

        System.out.println("=================================================");
        do {
            System.out.println("Enter PIN: ");
            accountPin = in.nextLine();
            if (accountPin.length() != 6){
                System.out.println("PIN should have 6 digits length");
            }
            else if (!accountPin.matches("[0-9]+") ) {
                System.out.println("PIN should only contains numbers");
            }
            else {
                pinIsValid = true;
                break;
            }
        }
        while (!pinIsValid);

        return validateCredential(accountId, accountPin);
    }

    private Optional<Account> validateCredential(String accountId, String accountPin) {
        return bank.findCredential(accountId, accountPin);
    }

    private void showTransactionMenu(Account userAccount) {
        String option = "X";

        System.out.println("=================================================");
        do {
            Scanner in = new Scanner(System.in);
            System.out.println("1. Withdraw");
            System.out.println("2. Fund Transfer");
            System.out.println("3. Exit");
            System.out.println("Please choose option[3]:");
            String input = in.nextLine();
            if(input == null) {
                option = "3";
            }
            else if (input.toCharArray().length > 1 || !input.matches("[1-3]+")){
                System.out.println("Incorrect option");
                System.out.println(" ");
            }
            else {
                option = input;
                System.out.println("selected option");
                switch(option) {
                    case "1":
                        showWithdraw(userAccount);
                        option = "X";
                        break;
                    case "2":
                        showTransfer(userAccount);
                        break;
                    case "3":
                        break;
                    default:
                        break;
                }
            }
        }
        while (!option.equals("3"));
    }

    private void showWithdraw(Account userAccount) {

        System.out.println("=================================================");
        System.out.println("1. $10");
        System.out.println("2. $50");
        System.out.println("3. $100");
        System.out.println("4. Other");
        System.out.println("5. Back");
        System.out.println("Please choose option[5]:");

        Scanner in = new Scanner(System.in);
        String option = in.nextLine();
        if(option.isEmpty()) {
            System.out.println("Withdraw cancelled");
        }
        else if (option.length() > 1 || !option.matches("[1-5]+")){
            System.out.println("Incorrect option");
            System.out.println(" ");
        }
        else {
            switch(option) {
                case "1":
                    this.deductBalance(userAccount,10);
                    option = "x";
                    break;
                case "2":
                    this.deductBalance(userAccount, 50);
                    break;
                case "3":
                    this.deductBalance(userAccount, 100);
                    break;
                case "4":
                    this.showOtherDeductAmt(userAccount);
                    break;
                case "5":
                    break;
                default:
                    break;
            }
        }
    }

    private void showOtherDeductAmt(Account userAccount) {
        System.out.println("==============================");
        System.out.println("Other Withdraw");
        System.out.println("Enter amount to withdraw : ");
        Scanner in = new Scanner(System.in);


        boolean validAmount = false;
        do {
            String amount = in.nextLine();
            boolean isNumeric = amount.chars().allMatch( Character::isDigit );
            System.out.println();
            if (amount.length() > 4 || !isNumeric || ((Integer.parseInt(amount)%10) != 0)) {
                System.out.println("Invalid amount");
                System.out.println("Please try again...");
                System.out.println(" ");
            }
            else if (Integer.parseInt(amount) > 1000){
                System.out.println("Maximum amount to withdraw is $1000");
                System.out.println("Please try again...");
                System.out.println(" ");
            }
            else {
                validAmount = true;
                deductBalance(userAccount, Integer.parseInt(amount));
            }
        }
        while(!validAmount);


    }

    private void deductBalance(Account account, int amount){
        System.out.println("==============in deduct ================");
        Optional<Account> optionalAccount = bank.findCredential(account.getAccountNumber(), account.getPin());
        if(optionalAccount.isPresent()){
            Account acc = optionalAccount.get();
            System.out.println("Current balance : " + acc.getBalance());
            System.out.println("Deduct amount" + amount);
            if (acc.getBalance() >= amount){
                int newBalance = acc.getBalance() - amount;
                acc.setBalance(newBalance);
                List<Account> customers = bank.getCustomers().stream()
                        .filter( c -> !c.getAccountNumber().equals(account.getAccountNumber()))
                        .collect(Collectors.toList());
                customers.add(acc);
                this.showSummaryScreen(acc, amount);
                System.out.println("========================================");
            }
            else{
                System.out.println("Balance insufficient");
            }
        }
    }

    private void showSummaryScreen(Account account, int amount) {
        LocalDateTime transactionTime = LocalDateTime.now();
        System.out.println("Summary");
        System.out.println("Date : " + transactionTime);
        System.out.println("Withdraw : $" + amount);
        System.out.println("Balance : $" + account.getBalance());
        System.out.println(" ");
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.println("Choose option[2]: ");

        Scanner in = new Scanner(System.in);
        String option = in.nextLine();
        if (option.length() > 1 || !option.matches("[1-2]+")) {
            System.out.println("Invalid option");
            System.out.println("Continuing transaction...");
        }
        else if(option.equals("1")){
            System.out.println("Continuing transaction...");
        }
        else {
            showWelcomeScreen();
        }

    }

    private void showTransfer(Account userAccount){}
}
