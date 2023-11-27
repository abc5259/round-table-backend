package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.member.application.authcode.AuthCode;
import com.roundtable.roundtable.member.application.authcode.AuthCodeStoreStrategy;
import com.roundtable.roundtable.member.application.dto.EmailDto;
import com.roundtable.roundtable.member.exception.MemberException.EmailDuplicatedException;
import com.roundtable.roundtable.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final AuthCodeStoreStrategy authCodeStoreStrategy;

    @Transactional(readOnly = true)
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
