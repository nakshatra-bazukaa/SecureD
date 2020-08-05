package com.bazukaa.secured.util;

import java.util.Random;

public class PasswordGenerator {

    public static char[] generatePassword(boolean haveBlockAlpha, boolean haveNonBlockAlpha, boolean haveSpecialChar, boolean haveNumeric, int len){
        char[] generatedPassword = new char[len];
        String block_alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String non_block_alpha = "abcdefghijklmnopqrstuvwxyz";
        String special_char = "!@#$%^&*_=+-/.?<>)(";
        String numbers = "1234567890";
        String allowed_values = "";

        if(haveBlockAlpha)
            allowed_values += block_alpha;
        if(haveNonBlockAlpha)
            allowed_values += non_block_alpha;
        if(haveSpecialChar)
            allowed_values += special_char;
        if(haveNumeric)
            allowed_values += numbers;

        Random random = new Random();
        for(int i = 0; i < len; i++)
            generatedPassword[i] = allowed_values.charAt(random.nextInt(allowed_values.length()));

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
