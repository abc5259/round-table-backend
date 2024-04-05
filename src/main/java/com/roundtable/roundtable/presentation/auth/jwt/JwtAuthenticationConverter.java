package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
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

        if(accessToken == null) { //accessToken 이 있을때만 Token 생성
            return null;
        }

        return new JwtAuthenticationToken(Token.of(accessToken), null, null);
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

        return header.substring(AUTHENTICATION_SCHEME_BEAT.length() + 1);
    }
}
