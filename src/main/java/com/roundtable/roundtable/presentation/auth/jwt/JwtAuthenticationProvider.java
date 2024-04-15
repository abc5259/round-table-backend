package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.dto.JwtPayload;
import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.business.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

    private final JwtProvider jwtProvider;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null || !supports(authentication.getClass())) {
            throw new IllegalArgumentException("지원하지 않는 인증 토큰입니다." + "[인증 토큰: " + authentication + "]");
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        Tokens token = jwtAuthenticationToken.getTokens();
        final JwtPayload jwtPayload = jwtProvider.extractPayload(token.getAccessToken());

        return new JwtAuthenticationToken(token, jwtPayload, null, null);
    }
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}
