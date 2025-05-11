package com.example.bookshop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
    public boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(\\+84|0)\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
