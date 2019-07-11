package com.mitrais;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Bank bank = new Bank("Mit Bank");
        bank.initialize();
        ATM atm = new ATM(bank);
        atm.main();

    }
}
