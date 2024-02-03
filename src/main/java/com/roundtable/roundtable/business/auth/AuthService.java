package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.implement.auth.authcode.AuthCode;
import com.roundtable.roundtable.implement.auth.authcode.AuthCodeStoreStrategy;
import com.roundtable.roundtable.implement.member.LoginManager;
import com.roundtable.roundtable.implement.member.LoginMember;
import com.roundtable.roundtable.implement.auth.MailProvider;
import com.roundtable.roundtable.implement.auth.Token;
import com.roundtable.roundtable.implement.member.MemberMaker;
import com.roundtable.roundtable.implement.member.MemberReader;
import com.roundtable.roundtable.implement.member.RegisterMember;
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
    private final AuthCodeStoreStrategy authCodeStoreStrategy;
    private final LoginManager loginManager;


    @Transactional(readOnly = true)
    public void sendCodeToEmail(final String email) {
        memberReader.checkDuplicateEmail(email);

        AuthCode authCode = AuthCode.createCode();
        mailProvider.sendEmail(email, "Round Table 이메일 인증 번호", authCode.getCode());
        authCodeStoreStrategy.saveAuthCode(authCode);
    }

    public boolean isCorrectAuthCode(AuthCode authCode) {
        return authCodeStoreStrategy.isCorrectAuthCode(authCode);
    }

    public void register(final RegisterMember registerMember) {
        memberReader.checkDuplicateEmail(registerMember.email());
        Long memberId = memberMaker.register(registerMember);

        log.info(String.format("ID = %d User 생성", memberId));
    }

    @Transactional(readOnly = true)
    public Token login(final LoginMember loginMember) {
        return loginManager.login(loginMember);
    }
}
