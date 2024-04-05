package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.JwtPayload;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.business.auth.JwtProvider;
import com.roundtable.roundtable.business.auth.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final JwtProvider jwtProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null || !supports(authentication.getClass())) {
            return null;
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        Token token = jwtAuthenticationToken.getToken();
        /**
         * 사용자가 요청으로 보낸 인증 헤더가 빈 값이면 token은 null일 수 있다.
         */
        if(token == null || !jwtProvider.isValidToken(token.getAccessToken())) {
            return null;
        }

        final JwtPayload jwtPayload = jwtProvider.getSubject(token.getAccessToken());

        return new JwtAuthenticationToken(token, jwtPayload, null, null);
    }
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}
