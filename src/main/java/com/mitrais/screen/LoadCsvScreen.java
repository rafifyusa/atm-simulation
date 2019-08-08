package com.mitrais.screen;

import com.mitrais.service.BankService;
import com.mitrais.utils.Utility;

public class LoadCsvScreen {
    private BankService bankService;

    public LoadCsvScreen(BankService bankService) {
        this.bankService = bankService;
    }

    public void showLoadMenu () {
        System.out.println("Please insert the file name :");
        String fileName = Utility.getScanner().nextLine();
        if (fileName.isEmpty()) {
            System.out.println("Cancelling loading csv file...");
        }
        else {
            bankService.loadCsvData(fileName);
        }
    }
}
