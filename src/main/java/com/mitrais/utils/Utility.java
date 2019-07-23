package com.mitrais.utils;

public class Utility {
    public static boolean isNumeric (String string) {
        return string.chars().allMatch( Character::isDigit );
    }
}
