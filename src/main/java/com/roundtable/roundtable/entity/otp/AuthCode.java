package com.roundtable.roundtable.entity.otp;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "authcode", timeToLive = 60 * 3)
public class AuthCode {

    private static final int MAX_LENGTH = 6;

    @Id
    private String email;

    private String code;

    @Builder
    private AuthCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static AuthCode of(String email, String code) {
        return AuthCode.builder()
                .email(email)
                .code(code)
                .build();
    }

    public static AuthCode of(String code) {
        return AuthCode.builder()
                .code(code)
                .build();
    }

    public static AuthCode create(String email) {
        return AuthCode.builder()
                .email(email)
                .code(createCode())
                .build();
    }

    private static String createCode() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < MAX_LENGTH; i++) {
                code.append(random.nextInt(10));
            }
            return code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSameCode(AuthCode authCode) {
        if(Objects.isNull(authCode) || authCode.code == null) {
            return false;
        }

        return this.code.equals(authCode.code);
    }
}
