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
    public static final String AUTHENTICATION_SCHEME_BEAT = "Bearer";
    @Override
    public Authentication convert(HttpServletRequest request) {
        String accessToken = getAccessTokenFromHttpServletRequest(request);
        Token token = Token.of(accessToken, null);
        return new JwtAuthenticationToken(token, null, null);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEAT)) {
            return null;
        }
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEAT)) {
            throw new BadCredentialsException("Empty bearer authentication token");
        }

        return header.substring(7);
    }
}
