package com.mitrais.screen;

import com.mitrais.model.Account;

import java.time.LocalDateTime;

public class WithdrawSummaryScreen {

    public static void showSummaryScreen(Account account, int amount) {
        LocalDateTime transactionTime = LocalDateTime.now();
        System.out.println("Summary\n" +
                "Date : " + transactionTime + "\n" +
                "Withdraw : $" + amount + "\n" +
                "Balance : $" + account.getBalance()
        );
        Menu.showMenuWithTwoSelection();
    }
}
