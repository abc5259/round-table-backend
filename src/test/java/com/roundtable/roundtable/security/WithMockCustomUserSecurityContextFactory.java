package com.roundtable.roundtable.security;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.token.dto.JwtPayload;
import com.roundtable.roundtable.presentation.auth.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser mockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
                Tokens.of(mockCustomUser.accessToken(), mockCustomUser.refreshToken()),
                new JwtPayload(mockCustomUser.userId(), mockCustomUser.houseId()),
                null,
                null
        );
        context.setAuthentication(jwtAuthenticationToken);
        return context;
    }
}
