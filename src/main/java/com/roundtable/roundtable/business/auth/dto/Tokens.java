package com.roundtable.roundtable.business.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Tokens {
    private String accessToken;
    private String refreshToken;

    public static Tokens of(String accessToken, String refreshToken) {
        return new Tokens(accessToken, refreshToken);
    }

    public static Tokens of(String accessToken) {
        return new Tokens(accessToken, null);
    }
}
