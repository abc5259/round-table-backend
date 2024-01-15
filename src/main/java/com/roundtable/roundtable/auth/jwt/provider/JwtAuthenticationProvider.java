package com.roundtable.roundtable.auth.jwt.provider;

import com.roundtable.roundtable.auth.jwt.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {
    private final JwtProvider jwtProvider;

    public Authentication authenticate(Authentication authentication) {
        if(!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
            throw new IllegalArgumentException("");
        }

        Token token = jwtAuthenticationToken.getToken();

        if(!jwtProvider.validateAccessToken(token.getAccessToken())) {
            throw new BadCredentialsException("유효기간이 지났습니다.");
        }

        final Long userId = jwtProvider.getSubject(token.getAccessToken());

        return new JwtAuthenticationToken(token, userId, null, null);
    }
}
