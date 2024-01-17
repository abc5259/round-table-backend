package com.roundtable.roundtable.auth.jwt.provider;

import com.roundtable.roundtable.auth.exception.AuthenticationException;
import com.roundtable.roundtable.auth.jwt.token.JwtAuthenticationToken;
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

        final Long userId = jwtProvider.getSubject(token.getAccessToken());

        return new JwtAuthenticationToken(token, userId, null, null);
    }
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}
