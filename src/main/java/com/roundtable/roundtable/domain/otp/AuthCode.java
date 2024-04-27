package com.roundtable.roundtable.domain.otp;

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
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "authcode")
public class AuthCode {

    public static final int MAX_LENGTH = 6;

    public static final Long CODE_TTL = 180L; //3분

    public static final Long REGISTER_TTL = 360L; //6분

    @Id
    private String email;

    private String code;

    private boolean canRegister;

    @TimeToLive
    private Long ttl; // TTL 필드

    @Builder
    private AuthCode(String email, String code, Long ttl) {
        this.email = email;
        this.code = code;
        this.ttl = ttl;
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
                .ttl(CODE_TTL)
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

    public void settingForRegister() {
       this.canRegister = true;
       this.ttl = REGISTER_TTL;
    }
}
