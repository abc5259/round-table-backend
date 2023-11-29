package com.roundtable.roundtable.member.application.authcode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthCode {
    private static final int MAX_LENGTH = 6;
    private final String code;

    public AuthCode() {
        code = createCode();
    }

    public AuthCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private String createCode() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < MAX_LENGTH; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("createCode() exception occur");
            throw new RuntimeException(e);
        }
    }
}
