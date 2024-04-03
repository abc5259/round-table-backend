package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.business.member.MemberMaker;
import com.roundtable.roundtable.business.member.RegisterMember;
import com.roundtable.roundtable.business.member.LoginManager;
import com.roundtable.roundtable.business.member.LoginMember;
import com.roundtable.roundtable.business.member.MemberReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MailProvider mailProvider;
    private final MemberReader memberReader;
    private final MemberMaker memberMaker;
    private final EmailAuthCodeManager emailAuthCodeManager;
    private final LoginManager loginManager;


    @Transactional(readOnly = true)
    public void sendCodeToEmail(final String email) {
        memberReader.checkDuplicateEmail(email);

        AuthCode authCode = AuthCode.createCode();
        emailAuthCodeManager.saveAuthCode(email, authCode);
        mailProvider.sendEmail(email, "Round Table 이메일 인증 번호", authCode.getCode());
    }

    public boolean isCorrectAuthCode(String email, AuthCode authCode) {
        return emailAuthCodeManager.isCorrectAuthCode(email, authCode);
    }

    public Long register(final RegisterMember registerMember) {
        memberReader.checkDuplicateEmail(registerMember.email());
        Long memberId = memberMaker.register(registerMember);
        return memberId;
    }

    @Transactional(readOnly = true)
    public Token login(final LoginMember loginMember) {
        return loginManager.login(loginMember);
    }
}
