package com.roundtable.roundtable.member.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncoder {

    private static final String ALGORITHM = "SHA-256";

    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(password.getBytes());
            return Base64.getEncoder().encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException("암호화 알고리즘이 존재하지 않습니다.");
        }
    }


    public static boolean matches(String password, String encodedPassword) {
        return encode(password).equals(encodedPassword);
    }
}
