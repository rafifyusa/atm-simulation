package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.service.BankService;
import com.mitrais.utils.Utility;

import java.util.Optional;

public class WelcomeScreen {
    private BankService bankService;
    private boolean idIsValid = false;
    private boolean pinIsValid = false;

    public WelcomeScreen(BankService bankService) {
        this.bankService = bankService;
    }

    public Optional<Account> showWelcomeScreen() {
        String accountId;
        String accountPin;

        System.out.println("=================================================");
        System.out.println("             Welcome to Mitrais Bank");
        System.out.println("Please insert your account detail to continue...");
        System.out.println("=================================================");

        do {
            System.out.println("Enter account number: ");
            accountId = Utility.getScanner().nextLine();
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
            accountPin = Utility.getScanner().nextLine();
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
        return this.bankService.findCredential(accountId, accountPin);
    }
}
