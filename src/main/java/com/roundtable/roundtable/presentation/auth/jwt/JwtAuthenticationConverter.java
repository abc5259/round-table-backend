package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private static final String AUTHORIZATION = "Authorization";
    public static final String AUTHENTICATION_SCHEME_BEAT = "Bearer";

    public static final String COOKIE_AUTH_TOKEN = "authToken";
    @Override
    public Authentication convert(HttpServletRequest request) {
        String accessToken = getAccessTokenFromHttpServletRequest(request);
        String refreshToken = getRefreshTokenFromHttpCookie(request);

        Token token = null;
        if(accessToken != null) { //accessToken 이 있을때만 Token 생성
            token = Token.of(accessToken, refreshToken);
        }

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

    private String getRefreshTokenFromHttpCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(cookie -> JwtAuthenticationConverter.COOKIE_AUTH_TOKEN.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
