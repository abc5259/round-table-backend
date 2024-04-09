package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.domain.otp.AuthCodeRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAuthCodeManager {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final MailProvider mailProvider;

    public void saveAuthCode(AuthCode authCode) {
        authCodeRedisRepository.save(authCode);
    }

    public void saveAuthCodeAndSendMail(AuthCode authCode, String email) {
        saveAuthCode(authCode);
        mailProvider.sendEmail(email, "Round Table 이메일 인증 번호", authCode.getCode());
    }

    public boolean isCorrectAuthCode(AuthCode authCode) {
        AuthCode storedAuthCode = authCodeRedisRepository.findById(authCode.getEmail()).orElse(null);
        return authCode.isSameCode(storedAuthCode);
    }
}
