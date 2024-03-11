package com.roundtable.roundtable.global.util;

import java.security.SecureRandom;

public class RandomStringGenerator {

    private static final String DEFAULT_SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generateRandomString(int length) {
        return generateRandomString(length, DEFAULT_SALT_CHARS);
    }

    private static String generateRandomString(int length, String saltChars) {
        SecureRandom random = new SecureRandom();
        StringBuilder salt = new StringBuilder();
        int saltCharsLength = saltChars.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(saltCharsLength);
            salt.append(saltChars.charAt(index));
        }
        return salt.toString();
    }
}
