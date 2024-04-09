package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.Tokens;
import com.roundtable.roundtable.global.exception.AuthenticationException.JwtAuthenticationException;
import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String AUTHENTICATION_SCHEME_BEAT = "Bearer";

    private static final int TOKEN_INDEX = 1;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String accessToken = getAccessTokenFromHttpServletRequest(request);
        return new JwtAuthenticationToken(Tokens.of(accessToken), null, null);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null) {
            throw new JwtAuthenticationException(AuthErrorCode.NOT_FOUND_AUTH_HEADER);
        }
        header = header.trim();

        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEAT)) {
            throw new JwtAuthenticationException(AuthErrorCode.NOT_START_HEADER_BEAR);
        }

        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEAT)) {
            throw new JwtAuthenticationException(AuthErrorCode.NO_FOUND_TOKEN);
        }

        return header.split(" ")[TOKEN_INDEX];
    }
}
