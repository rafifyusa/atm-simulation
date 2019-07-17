package com.mitrais;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        System.out.println("             Welcome to Mitrais Bank");
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

        do {
            Scanner in = new Scanner(System.in);
            System.out.println("================= Transaction Screen =================\n" +
                    "1. Withdraw\n" +
                    "2. Fund Transfer\n" +
                    "3. Transaction History\n"+
                    "4. Exit\n" +
                    "Please choose option[4]:");
            String input = in.nextLine();
            if(input.isEmpty()) {
                option = "4";
            }
            else if (input.toCharArray().length > 1 || !input.matches("[1-4]+")){
                System.out.println("Incorrect option");
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
                        showHistory(userAccount);
                        break;
                    case "4" :
                        break;
                    default:
                        break;
                }
            }
        }
        while (!option.equals("4"));
    }

    private void showHistory(Account userAccount) {
        System.out.println("================= History Screen ================\n" +
                "Your 10 most Recent Transactions:");

        Optional<Account> user = bank.findAccount(userAccount.getAccountNumber());
        user.ifPresent(value -> {
            List<Transaction> transactionsHistory = value.getTransactionList();
            transactionsHistory.forEach(System.out::println);
        });
        System.out.println("Current Balance : " + user.get().getBalance());

        showMenuWithTwoSelection();
    }

    private void showMenuWithTwoSelection () {
        System.out.println("\n1. Transaction\n" +
                "2. Exit\n" +
                "Please choose option [2]");
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
            main();
        }
    }

    private void showWithdraw(Account userAccount) {
        System.out.println("================ Withdraw Screen =================\n" +
                "1. $10\n" +
                "2. $50\n" +
                "3. $100\n" +
                "4. Other\n" +
                "5. Back\n" +
                "Please choose option[5]:");

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
        System.out.println("============== Deducting Balance ================");
        Optional<Account> optionalAccount = bank.findCredential(account.getAccountNumber(), account.getPin());
        if(optionalAccount.isPresent()){
            Account acc = optionalAccount.get();
            System.out.println("Current balance : " + acc.getBalance());
            System.out.println("Deduct amount" + amount);
            if (acc.getBalance() >= amount){
                int newBalance = acc.getBalance() - amount;
                acc.setBalance(newBalance);
                Transaction withdrawTransaction = createWithdrawTransaction(amount);
                acc.getTransactionList().add(withdrawTransaction);

                List<Account> customers = bank.getCustomers().stream()
                        .filter( c -> !c.getAccountNumber().equals(account.getAccountNumber()))
                        .collect(Collectors.toList());
                customers.add(acc);
                this.showSummaryScreen(acc, amount);
            }
            else{
                System.out.println("Balance insufficient");
            }
        }
    }

    private Transaction createWithdrawTransaction(int amount) {
        return new Transaction(LocalDateTime.now(), Transaction.Type.WITHDRAW, amount);
    }

    private void showSummaryScreen(Account account, int amount) {
        LocalDateTime transactionTime = LocalDateTime.now();
        System.out.println("Summary\n" +
                "Date : " + transactionTime + "\n" +
                "Withdraw : $" + amount + "\n" +
                "Balance : $" + account.getBalance()
                );
       showMenuWithTwoSelection();
    }

    private void showTransfer(Account userAccount){
        Scanner in = new Scanner(System.in);

        System.out.println("================ Transfer Screen =================\n" +
                "Please enter destination account and\n " +
                "press enter to continue or \n" +
                "press cancel (Esc) to go back to Transaction:"
        );
        String destinationAcc = in.nextLine();
        System.out.println("................................................\n" +
                "Please enter transfer amount and\n" +
                "press enter to continue or \n" +
                "press cancel (Esc) to go back to Transaction:"
        );
        String transferAmt = in.nextLine();
        System.out.println("................................................\n" +
                "Please enter reference number (Optional) and\n" +
                "press enter to continue or \n" +
                "press cancel (Esc) to go back to Transaction:"
                );
        String referenceNum = in.nextLine();

            System.out.println(
                    "Transfer Confirmation\n" +
                    "Destination Account : " +destinationAcc+ "\n"+
                    "Transfer Amount     : " + transferAmt + "\n" +
                    "Reference Number    : " + referenceNum +"\n" +
                    "\n" +
                    "1. Confirm Trx\n" +
                    "2. Cancel Trx\n" +
                    "Choose option[2]:");

            String option;
            boolean transactionIsValid;
            do {
                option = in.nextLine();
                if (option.isEmpty()) {
                    System.out.println("Transaction cancelled");
                    option = "2";
                }
                else if (option.length() > 1 || !option.matches("[1-2]+")) {
                    System.out.println("Incorrect option");
                }
                else {
                    switch(option) {
                        case "1":
                            userAccount = bank.findAccount(userAccount.getAccountNumber()).get();
                            transactionIsValid = validateTransfer(userAccount, destinationAcc, transferAmt, referenceNum);
                            if (transactionIsValid) {executeTransaction(userAccount, destinationAcc, transferAmt, referenceNum);}
                            break;
                        case "2":
                            break;
                    }
                }
            }
            while (!option.equals("2"));

    }

    private void executeTransaction(Account userAccount, String destinationAcc, String transferAmt, String referenceNum) {
        Optional<Account> optionalAccount = bank.findCredential(userAccount.getAccountNumber(), userAccount.getPin());
        Optional<Account> optionalDestinationAccount = bank.findAccount(destinationAcc);

        if (optionalAccount.isPresent() && optionalDestinationAccount.isPresent()) {
            Account newAccDetails = optionalAccount.get();
            newAccDetails.setBalance(newAccDetails.getBalance() - Integer.parseInt(transferAmt));

            Account newDestinationAccDetails = optionalDestinationAccount.get();
            newDestinationAccDetails.setBalance(newDestinationAccDetails.getBalance() + Integer.parseInt(transferAmt));

            List<Account> customers = bank.getCustomers().stream()
                    .filter( c -> !c.getAccountNumber().equals(newAccDetails.getAccountNumber())
                    && !c.getAccountNumber().equals(newDestinationAccDetails.getAccountNumber()))
                    .collect(Collectors.toList());

            List<Transaction> transactions = createTransferTransaction(newAccDetails, newDestinationAccDetails, Integer.parseInt(transferAmt));

            if (newAccDetails.getTransactionList() == null) {newAccDetails.setTransactionList(new ArrayList<>());}
            if (newDestinationAccDetails.getTransactionList() == null) {newDestinationAccDetails.setTransactionList(new ArrayList<>());}
            newAccDetails.getTransactionList().add(transactions.get(0));
            newDestinationAccDetails.getTransactionList().add(transactions.get(1));


            customers.add(newAccDetails);
            customers.add(newDestinationAccDetails);

            newAccDetails.getTransactionList().forEach(System.out::println);
            showTransferSummary(newAccDetails, destinationAcc, transferAmt, referenceNum);
        }
    }

    private void showTransferSummary(Account userAccount, String destinationAcc, String transferAmt, String referenceNumber) {
        System.out.println("Fund Transfer Summary\n" +
                "Destination Account : " + destinationAcc +"\n" +
                "Transfer Amount     : " + transferAmt +"\n" +
                "Reference Number    : "+ referenceNumber + "\n" +
                "Balance             : " + userAccount.getBalance() + "\n" +
                "\n" +
                "1. Transaction\n" +
                "2. Exit\n" +
                "Choose option[2]:");
        Scanner in = new Scanner(System.in);
        String option = in.nextLine();

        if (option.isEmpty() || option.equals("2")) {
            main();
        }
        else if (option.equals("1")) {
            showTransactionMenu(userAccount);
        }
        else {
            System.out.println("invalid option, returning to transaction page...");
        }
    }

    private boolean validateTransfer(Account userAccount, String destinationAcc, String transferAmt, String referenceNum) {
        boolean validDestinationAcc = false;
        boolean validTransferAmt = false;
        boolean validReferenceNum = false;

        Optional<Account> destinationAccount = bank.findAccount(destinationAcc);
        if (!isNumeric(destinationAcc) || !destinationAccount.isPresent()) {
            System.out.println("Invalid Account");
        }
        else {
            validDestinationAcc = true;
        }

        if (transferAmt.length() > 4 || !isNumeric(transferAmt)) {
            System.out.println("Invalid amount");
            System.out.println("Please try again...");
            System.out.println(" ");
        }
        else if (Integer.parseInt(transferAmt) > 1000){
            System.out.println("Maximum amount to transfer is $1000");
        }
        else if (Integer.parseInt(transferAmt) < 1){
            System.out.println("Minimum amount to transfer is $1");
        }
        else if (Integer.parseInt(transferAmt) > userAccount.getBalance()) {
            System.out.println("Insufficient balance $" + transferAmt);
        }
        else {
            validTransferAmt = true;
        }
        if (!referenceNum.isEmpty() && !isNumeric(referenceNum)) {
            System.out.println("Invalid Reference Number");
        }
        else {
            validReferenceNum = true;
        }

        return (validDestinationAcc && validReferenceNum && validTransferAmt);
    }

    private boolean isNumeric (String string) {
        return string.chars().allMatch( Character::isDigit );
    }

    private List<Transaction> createTransferTransaction(Account userAccount, Account receiverAccount, int amount) {
        LocalDateTime time = LocalDateTime.now();
        Transaction userTransaction = new Transaction(time, Transaction.Type.TRANSFER, amount, receiverAccount, userAccount);
        Transaction receiverTransaction = new Transaction(time, Transaction.Type.TRANSFER, amount, receiverAccount, userAccount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(userTransaction);
        transactions.add(receiverTransaction);

        return transactions;
    }
}
