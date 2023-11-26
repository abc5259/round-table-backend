package com.roundtable.roundtable.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;

    public void sendCodeToEmail(String toEmail) {
//        this.checkDuplicatedEmail(toEmail);
        String title = "Round Table 이메일 인증 번호";
//        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, "authCode");
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
//        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
//                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }
}
