package com.mitrais.screen;

import com.mitrais.ATM;
import com.mitrais.model.Account;

import java.util.Scanner;

public class TransferSummaryScreen {

    public static void showTransferSummary(Account userAccount, String destinationAcc, String transferAmt, String referenceNumber) {
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
            ATM.main();
        }
        else if (option.equals("1")) {
            System.out.println("Continuing Transaction...");
        }
        else {
            System.out.println("invalid option, returning to transaction page...");
        }
    }
}
