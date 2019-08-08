package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.service.BankService;
import com.mitrais.utils.Utility;

public class TransactionScreen {
    private Account userAccount;
    private BankService bankService;

    public TransactionScreen(Account userAccount, BankService bankService) {
        this.userAccount = userAccount;
        this.bankService = bankService;
    }

    public void showTransactionScreen() {
        String option = "X";

        do {
            System.out.println("================= Transaction Screen =================\n" +
                    "1. Withdraw\n" +
                    "2. Fund Transfer\n" +
                    "3. Transaction History\n"+
                    "4. Load New Customers From CSV\n" +
                    "5. Exit\n" +
                    "Please choose option[5]:");
            String input = Utility.getScanner().nextLine();
            if(input.isEmpty()) {
                option = "5";
            }
            else if (input.toCharArray().length > 1 || !input.matches("[1-4]+")){
                System.out.println("Incorrect option");
            }
            else {
                option = input;
                System.out.println("selected option");
                switch(option) {
                    case "1":
                        WithdrawScreen withdrawScreen = new WithdrawScreen(userAccount, bankService);
                        withdrawScreen.showWithdraw();
                        option = "X";
                        break;
                    case "2":
                        TransferScreen transferScreen = new TransferScreen(userAccount, bankService);
                        transferScreen.showTransfer();
                        break;
                    case "3":
                        TransactionHistoryScreen transactionHistoryScreen = new TransactionHistoryScreen(userAccount, bankService);
                        transactionHistoryScreen.showHistory();
                        break;
                    case "4" :
                        LoadCsvScreen loadCsvScreen = new LoadCsvScreen(bankService);
                        loadCsvScreen.showLoadMenu();
                        break;
                    case "5" :
                        break;
                    default:
                        break;
                }
            }
        }
        while (!option.equals("5"));
    }
}
