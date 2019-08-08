package com.mitrais;
import com.mitrais.model.Account;
import com.mitrais.screen.TransactionScreen;
import com.mitrais.screen.WelcomeScreen;
import com.mitrais.service.BankService;

import java.util.Optional;

public class ATM {
    public ATM() {
    }

    public static void main() {
        boolean running = true;
        BankService bankService = new BankService();

        do{
            WelcomeScreen welcomeScreen = new WelcomeScreen(bankService);
            Optional<Account> user = welcomeScreen.showWelcomeScreen();
            if (user.isPresent()) {
                Account userAccount = user.get();

                TransactionScreen transactionScreen = new TransactionScreen(userAccount,bankService);
                transactionScreen.showTransactionScreen();
            }
        }
        while (running);
    }
}
