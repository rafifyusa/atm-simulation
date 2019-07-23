package com.mitrais;
import com.mitrais.model.Account;
import com.mitrais.screen.TransactionScreen;
import com.mitrais.screen.WelcomeScreen;
import com.mitrais.service.BankService;

import java.util.Optional;
import java.util.Scanner;


public class ATM {
    public ATM() {
    }

    public static void main() {
        boolean running = true;
        BankService bankService = new BankService("Mitrais Bank");
        bankService.initialize();
        Scanner in = new Scanner(System.in);

        do{
            WelcomeScreen welcomeScreen = new WelcomeScreen(in, bankService);
            Optional<Account> user = welcomeScreen.showWelcomeScreen();
            if (user.isPresent()) {
                Account userAccount = user.get();

                TransactionScreen transactionScreen = new TransactionScreen(userAccount,in, bankService);
                transactionScreen.showTransactionScreen();
            }
        }
        while (running);
    }
}
