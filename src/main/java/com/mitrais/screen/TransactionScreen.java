package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.service.BankService;

import java.util.Scanner;

public class TransactionScreen {
    private Account userAccount;
    private Scanner in;
    private BankService bankService;

    public TransactionScreen(Account userAccount, Scanner in, BankService bankService) {
        this.userAccount = userAccount;
        this.in = in;
        this.bankService = bankService;
    }

    public void showTransactionScreen() {
        String option = "X";

        do {
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
                        WithdrawScreen withdrawScreen = new WithdrawScreen(userAccount, in, bankService);
                        withdrawScreen.showWithdraw();
                        option = "X";
                        break;
                    case "2":
                        TransferScreen transferScreen = new TransferScreen(userAccount, in, bankService);
                        transferScreen.showTransfer();
                        break;
                    case "3":
                        TransactionHistoryScreen transactionHistoryScreen = new TransactionHistoryScreen(userAccount, bankService);
                        transactionHistoryScreen.showHistory();
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
}
