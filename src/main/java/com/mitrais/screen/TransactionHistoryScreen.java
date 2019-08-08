package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.model.Transaction;
import com.mitrais.service.BankService;

import java.util.List;
import java.util.Optional;

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

        Optional<Account> user = bankService.findCustomer(userAccount.getAccountNumber());
        user.ifPresent(value -> {
            List<Transaction> transactionsHistory = value.getTransactionList();
            transactionsHistory.forEach(System.out::println);
        });
        System.out.println("Current Balance : " + user.get().getBalance());

        Menu.showMenuWithTwoSelection();
    }
}
