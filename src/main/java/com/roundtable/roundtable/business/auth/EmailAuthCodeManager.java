package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.entity.otp.AuthCode;
import com.roundtable.roundtable.entity.otp.AuthCodeRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAuthCodeManager {

    private final AuthCodeRedisRepository authCodeRedisRepository;

    public void saveAuthCode(AuthCode authCode) {
        authCodeRedisRepository.save(authCode);
    }

    public boolean isCorrectAuthCode(AuthCode authCode) {
        AuthCode storedAuthCode = authCodeRedisRepository.findById(authCode.getEmail()).orElse(null);
        return authCode.isSameCode(storedAuthCode);
    }
}
