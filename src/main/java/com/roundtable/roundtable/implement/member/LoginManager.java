package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.auth.JwtProvider;
import com.roundtable.roundtable.implement.auth.Token;
import com.roundtable.roundtable.implement.member.MemberException.MemberUnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginManager {
    private final PasswordEncoder passwordEncoder;
    private final MemberReader memberReader;
    private final JwtProvider jwtProvider;
    public Token login(final LoginMember loginMember) {
        Member member = memberReader.findByEmail(loginMember.email());
        if(member.isCorrectPassword(loginMember.password(), passwordEncoder)) {
            return jwtProvider.issueToken(member.getId());
        }

        throw new MemberUnAuthorizationException("이메일 또는 패스워드가 틀렸습니다.");
    }
}
