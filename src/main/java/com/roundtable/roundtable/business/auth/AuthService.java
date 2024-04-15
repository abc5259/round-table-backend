package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.member.MemberMaker;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.business.member.dto.RegisterMember;
import com.roundtable.roundtable.business.member.LoginManager;
import com.roundtable.roundtable.business.member.dto.LoginMember;
import com.roundtable.roundtable.domain.otp.AuthCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberValidator memberValidator;
    private final MemberMaker memberMaker;
    private final EmailAuthCodeManager emailAuthCodeManager;
    private final LoginManager loginManager;

    public void sendCodeToEmail(final String email) {
        memberValidator.validateDuplicatedEmail(email);
        emailAuthCodeManager.saveAuthCodeAndSendMail(AuthCode.create(email), email);
    }

    public boolean isCorrectAuthCode(AuthCode authCode) {
        return emailAuthCodeManager.isCorrectAuthCode(authCode);
    }

    public Long register(final RegisterMember registerMember) {
        memberValidator.validateDuplicatedEmail(registerMember.email());
        return memberMaker.register(registerMember);
    }

    public Tokens login(final LoginMember loginMember) {
        return loginManager.login(loginMember);
    }
}
