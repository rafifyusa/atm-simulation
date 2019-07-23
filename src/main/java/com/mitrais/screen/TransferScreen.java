package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.model.Transaction;
import com.mitrais.service.BankService;
import com.mitrais.utils.Utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TransferScreen {
    private Account userAccount;
    private Scanner in;
    private BankService bankService;

    public TransferScreen(Account userAccount, Scanner in, BankService bankService) {
        this.userAccount = userAccount;
        this.in = in;
        this.bankService = bankService;
    }

    void showTransfer(){
        boolean validDestinationAcc = false;
        boolean validTransferAmt = false;
        boolean validReferenceNum = false;
        String destinationAcc = "";
        String transferAmt = "";
        String referenceNum = "";

        while (!validDestinationAcc) {
            System.out.println("================ Transfer Screen =================\n" +
                    "Please enter destination account and\n" +
                    "press enter to continue or \n" +
                    "press cancel (Esc) to go back to Transaction:"
            );
            destinationAcc = in.nextLine();
            validDestinationAcc = validateDestinationAcc(destinationAcc);
        }

        while(!validTransferAmt){
            System.out.println("................................................\n" +
                    "Please enter transfer amount and\n" +
                    "press enter to continue or \n" +
                    "press cancel (Esc) to go back to Transaction:"
            );
            transferAmt = in.nextLine();
            validTransferAmt = validateTransferAmt(transferAmt, userAccount);
        }

        while (!validReferenceNum) {
            System.out.println("................................................\n" +
                    "Please enter reference number (Optional) and\n" +
                    "press enter to continue or \n" +
                    "press cancel (Esc) to go back to Transaction:"
            );
            referenceNum = in.nextLine();
            validReferenceNum = validateReferenceNum(referenceNum);
        }

        System.out.println(
                "Transfer Confirmation\n" +
                        "Destination Account : " +destinationAcc+ "\n"+
                        "Transfer Amount     : " + transferAmt + "\n" +
                        "Reference Number    : " + referenceNum +"\n" +
                        "\n" +
                        "1. Confirm Trx\n" +
                        "2. Cancel Trx\n" +
                        "Choose option[2]:");

        boolean validOption = false;
        String option;
        do {
            option = in.nextLine();
            if (option.isEmpty()) {
                System.out.println("Transaction cancelled");
                option = "2";
                validOption = true;
            }
            else if (option.length() > 1 || !option.matches("[1-2]+")) {
                System.out.println("Incorrect option");
            }
            else {
                switch(option) {
                    case "1":
                        Optional<Account> freshUserData = bankService.findAccount(userAccount.getAccountNumber());
                        if (freshUserData.isPresent()){
                            userAccount = freshUserData.get();
                            executeTransaction(userAccount, destinationAcc, transferAmt, referenceNum);
                        }
                        validOption = true;
                        break;
                    case "2":
                        validOption = true;
                        break;
                }
            }
        }
        while (!validOption);
    }

    private boolean validateDestinationAcc (String destinationAcc) {
        Optional<Account> destinationAccount = bankService.findAccount(destinationAcc);
        if (!Utility.isNumeric(destinationAcc) || !destinationAccount.isPresent()) {
            System.out.println("Invalid Account");
            return false;
        }
        else {
            return true;
        }
    }
    private boolean validateTransferAmt(String transferAmt, Account userAccount) {
        if (transferAmt.length() > 4 || !Utility.isNumeric(transferAmt)) {
            System.out.println("Invalid amount");
            System.out.println("Please try again...");
            return false;
        }
        else if (Integer.parseInt(transferAmt) > 1000){
            System.out.println("Maximum amount to transfer is $1000");
            return false;
        }
        else if (Integer.parseInt(transferAmt) < 1){
            System.out.println("Minimum amount to transfer is $1");
            return false;
        }
        else if (Integer.parseInt(transferAmt) > userAccount.getBalance()) {
            System.out.println("Insufficient balance $" + transferAmt);
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validateReferenceNum(String referenceNum) {
        if (!referenceNum.isEmpty() && !Utility.isNumeric(referenceNum)) {
            System.out.println("Invalid Reference Number");
            return false;
        }
        else {
            return true;
        }
    }

    private void executeTransaction(Account userAccount, String destinationAcc, String transferAmt, String referenceNum) {
        Optional<Account> optionalAccount = bankService.findCredential(userAccount.getAccountNumber(), userAccount.getPin());
        Optional<Account> optionalDestinationAccount = bankService.findAccount(destinationAcc);

        if (optionalAccount.isPresent() && optionalDestinationAccount.isPresent()) {
            Account newAccDetails = optionalAccount.get();
            newAccDetails.setBalance(newAccDetails.getBalance() - Integer.parseInt(transferAmt));

            Account newDestinationAccDetails = optionalDestinationAccount.get();
            newDestinationAccDetails.setBalance(newDestinationAccDetails.getBalance() + Integer.parseInt(transferAmt));

            List<Account> customers = bankService.getCustomers().stream()
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
            TransferSummaryScreen.showTransferSummary(newAccDetails, destinationAcc, transferAmt, referenceNum);
        }
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
