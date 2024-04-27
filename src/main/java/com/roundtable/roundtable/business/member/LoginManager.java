package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.member.dto.LoginMember;
import com.roundtable.roundtable.business.token.TokenManager;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import com.roundtable.roundtable.global.exception.MemberException.MemberUnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LoginManager {

    private final PasswordEncoder passwordEncoder;

    private final MemberReader memberReader;

    private final TokenManager tokenManager;

    public Tokens login(final LoginMember loginMember) {
        Member member = memberReader.findByEmail(loginMember.email());

        if(member.isCorrectPassword(loginMember.password(), passwordEncoder)) {
            return tokenManager.saveOrUpdateToken(member);
        }

        throw new MemberUnAuthorizationException(MemberErrorCode.INVALID_LOGIN);
    }
}
