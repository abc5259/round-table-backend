package com.roundtable.roundtable.auth.jwt.filter;

import com.roundtable.roundtable.auth.jwt.provider.Token;
import com.roundtable.roundtable.auth.jwt.token.JwtAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    public Authentication convert(HttpServletRequest request) {
        String accessToken = getAccessTokenFromHttpServletRequest(request);
        Token token = Token.of(accessToken, null);
        return new JwtAuthenticationToken(token, null, null);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(BEARER.length());
        }
        throw new BadCredentialsException("BadCredentialsException");
    }
}
