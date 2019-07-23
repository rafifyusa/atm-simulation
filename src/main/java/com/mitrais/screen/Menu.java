package com.mitrais.screen;

import com.mitrais.ATM;

import java.util.Scanner;

public class Menu {

    public static void showMenuWithTwoSelection () {
        System.out.println("\n1. Transaction\n" +
                "2. Exit\n" +
                "Please choose option [2]");
        Scanner in = new Scanner(System.in);
        String option = in.nextLine();
        if (option.length() > 1 || !option.matches("[1-2]+")) {
            System.out.println("Invalid option");
            System.out.println("Continuing transaction...");
        }
        else if(option.equals("1")){
            System.out.println("Continuing transaction...");
        }
        else {
            ATM.main();
        }
    }
}
