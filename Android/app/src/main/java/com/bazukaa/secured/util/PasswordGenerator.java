package com.bazukaa.secured.util;

import java.util.Random;

public class PasswordGenerator {

    private static String blockAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String nonBlockAlpha = "abcdefghijklmnopqrstuvwxyz";
    private static String specialChar = "!@#$%^&*_=+-/.?<>)(";
    private static String numbers = "1234567890";
    public static String generateCustomPassword(boolean haveBlockAlpha, boolean haveNonBlockAlpha, boolean haveSpecialChar, boolean haveNumeric, int len){
        char[] generatedPassword = new char[len];

        String allowedValues = "";
        int st = 0;

        Random random = new Random();
        if(haveBlockAlpha){
            allowedValues += blockAlpha;
            generatedPassword[st++] = blockAlpha.charAt(random.nextInt(blockAlpha.length()));
        }
        if(haveNonBlockAlpha){
            allowedValues += nonBlockAlpha;
            generatedPassword[st++] = nonBlockAlpha.charAt(random.nextInt(nonBlockAlpha.length()));
        }
        if(haveSpecialChar){
            allowedValues += specialChar;
            generatedPassword[st++] = specialChar.charAt(random.nextInt(specialChar.length()));
        }
        if(haveNumeric) {
            allowedValues += numbers;
            generatedPassword[st++] = numbers.charAt(random.nextInt(numbers.length()));
        }
        for(int i = st; i < len; i++)
            generatedPassword[i] = allowedValues.charAt(random.nextInt(allowedValues.length()));

        String finalGeneratedPassword = new String(generatedPassword);
        return finalGeneratedPassword;
    }

    public static String generateDefaultPassword(int len){
        char[] generatedPassword = new char[len];

        String allowedValues = blockAlpha + nonBlockAlpha + specialChar + numbers;

        Random random = new Random();
        for(int i = 0; i < len; i++)
            generatedPassword[i] = allowedValues.charAt(random.nextInt(allowedValues.length()));

        String finalGeneratedPassword = new String(generatedPassword);
        return finalGeneratedPassword;    }
}
