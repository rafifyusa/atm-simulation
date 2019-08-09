package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.model.Transaction;
import com.mitrais.service.BankService;

import java.util.List;

public class TransactionHistoryScreen {
    private Account userAccount;
    private BankService bankService;

    public TransactionHistoryScreen(Account userAccount, BankService bankService) {
        this.userAccount = userAccount;
        this.bankService = bankService;
    }

     void showHistory() {
        System.out.println("================= History Screen ================\n" +
                "Your 10 most Recent Transactions:");

        List<Transaction> transactionsHistory = bankService.findTenTransactionOfCustomer(userAccount.getAccountNumber());
        transactionsHistory.forEach(System.out::println);
        System.out.println("Current Balance : " + bankService.getCurrentCustomerBalance(userAccount.getAccountNumber()));

        Menu.showMenuWithTwoSelection();
    }
}
