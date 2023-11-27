package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.member.application.authcode.AuthCode;
import com.roundtable.roundtable.member.application.authcode.AuthCodeStoreStrategy;
import com.roundtable.roundtable.member.application.dto.EmailDto;
import com.roundtable.roundtable.member.exception.MemberException.EmailDuplicatedException;
import com.roundtable.roundtable.member.domain.MemberRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final AuthCodeStoreStrategy authCodeStoreStrategy;

    public void sendCodeToEmail(final EmailDto emailDto) {
        this.checkDuplicatedEmail(emailDto.email());
        String title = "Round Table 이메일 인증 번호";
        AuthCode authCode = new AuthCode();

        mailService.sendEmail(emailDto.email(), title, authCode.getCode());
        authCodeStoreStrategy.saveAuthCode(authCode);
    }

    private void checkDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException(email);
        }
    }
}
