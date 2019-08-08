package com.mitrais.screen;

import com.mitrais.model.Account;
import com.mitrais.model.Transaction;
import com.mitrais.service.BankService;
import com.mitrais.utils.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class WithdrawScreen {
    private Account userAccount;
    private BankService bankService;

    WithdrawScreen(Account userAccount, BankService bankService) {
        this.userAccount = userAccount;
        this.bankService = bankService;
    }

    void showWithdraw() {
        System.out.println("================ Withdraw Screen =================\n" +
                "1. $10\n" +
                "2. $50\n" +
                "3. $100\n" +
                "4. Other\n" +
                "5. Back\n" +
                "Please choose option[5]:");

        String option = Utility.getScanner().nextLine();
        if(option.isEmpty()) {
            System.out.println("Withdraw cancelled");
        }
        else if (option.length() > 1 || !option.matches("[1-5]+")){
            System.out.println("Incorrect option");
            System.out.println(" ");
        }
        else {
            switch(option) {
                case "1":
                    this.deductBalance(userAccount,10);
                    break;
                case "2":
                    this.deductBalance(userAccount, 50);
                    break;
                case "3":
                    this.deductBalance(userAccount, 100);
                    break;
                case "4":
                    this.showOtherDeductAmt(userAccount);
                    break;
                case "5":
                    break;
                default:
                    break;
            }
        }
    }

    private void showOtherDeductAmt(Account userAccount) {
        System.out.println("==============================");
        System.out.println("Other Withdraw");
        System.out.println("Enter amount to withdraw : ");

        boolean validAmount = false;
        do {
            String amount = Utility.getScanner().nextLine();

            if (amount.length() > 4 || !Utility.isNumeric(amount) || ((Integer.parseInt(amount)%10) != 0)) {
                System.out.println("Invalid amount");
                System.out.println("Please try again...");
            }
            else if (Integer.parseInt(amount) > 1000){
                System.out.println("Maximum amount to withdraw is $1000");
                System.out.println("Please try again...");
            }
            else {
                validAmount = true;
                deductBalance(userAccount, Integer.parseInt(amount));
            }
        }
        while(!validAmount);
    }

    private void deductBalance(Account account, int amount){
        System.out.println("============== Deducting Balance ================");
        Optional<Account> optionalAccount = bankService.findCredential(account.getAccountNumber(), account.getPin());
        if(optionalAccount.isPresent()){
            Account acc = optionalAccount.get();
            System.out.println("Current balance : " + acc.getBalance());
            System.out.println("Deduct amount" + amount);
            if (acc.getBalance() >= amount){
                int newBalance = acc.getBalance() - amount;
                acc.setBalance(newBalance);
                Transaction withdrawTransaction = createWithdrawTransaction(amount);
                acc.getTransactionList().add(withdrawTransaction);

                List<Account> customers = bankService.findAllCustomers().stream()
                        .filter( c -> !c.getAccountNumber().equals(account.getAccountNumber()))
                        .collect(Collectors.toList());
                customers.add(acc);
                WithdrawSummaryScreen.showSummaryScreen(acc, amount);
            }
            else{
                System.out.println("Balance insufficient");
            }
        }
    }

    private Transaction createWithdrawTransaction(int amount) {
        return new Transaction(LocalDateTime.now(), Transaction.Type.WITHDRAW, amount);
    }
}
