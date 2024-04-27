package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.domain.otp.AuthCodeRedisRepository;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.exception.AuthenticationException.EmailNotVerifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailAuthCodeManager {

    public static final Long CODE_TTL = 180L; //3분

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
        boolean isSameCode = storedAuthCode != null && authCode.isSameCode(storedAuthCode);


        if(isSameCode) {
            updateForRegisterMember(storedAuthCode);
        }

        return isSameCode;
    }

    private void updateForRegisterMember(AuthCode storedAuthCode) {
        storedAuthCode.settingForRegister();
        authCodeRedisRepository.save(storedAuthCode);
    }

    public boolean canRegister(String email) {
        AuthCode authCode = authCodeRedisRepository.findById(email).orElseThrow(EmailNotVerifiedException::new);
        return authCode.isCanRegister();
    }

    public void validateRegister(String email) {
        AuthCode authCode = authCodeRedisRepository.findById(email).orElseThrow(EmailNotVerifiedException::new);
        if(!authCode.isCanRegister()) {
            throw new EmailNotVerifiedException();
        }
    }
}
