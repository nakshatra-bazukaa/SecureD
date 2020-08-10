package com.bazukaa.secured.util;

import java.util.Random;

public class PasswordGenerator {

    public static char[] generatePassword(boolean haveBlockAlpha, boolean haveNonBlockAlpha, boolean haveSpecialChar, boolean haveNumeric, int len){
        char[] generatedPassword = new char[len];
        String blockAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String nonBlockAlpha = "abcdefghijklmnopqrstuvwxyz";
        String specialChar = "!@#$%^&*_=+-/.?<>)(";
        String numbers = "1234567890";
        String allowedValues = "";

        if(haveBlockAlpha)
            allowedValues += blockAlpha;
        if(haveNonBlockAlpha)
            allowedValues += nonBlockAlpha;
        if(haveSpecialChar)
            allowedValues += specialChar;
        if(haveNumeric)
            allowedValues += numbers;

        Random random = new Random();
        for(int i = 0; i < len; i++)
            generatedPassword[i] = allowedValues.charAt(random.nextInt(allowedValues.length()));

        return generatedPassword;
    }

    public static char[] generateCustomPassword(String allowedValues, int len){
        char[] generatedPassword = new char[len];

        Random random = new Random();
        for(int i = 0; i < len; i++)
            generatedPassword[i] = allowedValues.charAt(random.nextInt(allowedValues.length()));

        return generatedPassword;
    }
}
