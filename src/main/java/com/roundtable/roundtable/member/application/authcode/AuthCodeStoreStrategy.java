package com.roundtable.roundtable.member.application.authcode;

public interface AuthCodeStoreStrategy {
    String AUTH_CODE_STORE_KEY = "authcode";
    void saveAuthCode(AuthCode authCode);
    boolean isCorrectAuthCode(AuthCode authCode);
}
