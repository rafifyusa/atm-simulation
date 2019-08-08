package com.mitrais.utils;

import java.util.Scanner;

public class Utility {
    public static boolean isNumeric (String string) {
        return string.chars().allMatch( Character::isDigit );
    }

    public static Scanner getScanner() {
        return new Scanner (System.in);
    }

}
