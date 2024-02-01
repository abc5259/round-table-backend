package com.roundtable.roundtable.business.auth.jwt.filter;

import com.roundtable.roundtable.business.auth.jwt.provider.JwtAuthenticationProvider;
import com.roundtable.roundtable.business.auth.exception.AuthenticationException;
import com.roundtable.roundtable.business.auth.exception.AuthenticationException.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Authentication authRequest = jwtAuthenticationConverter.convert(request);
            Authentication authenticate = jwtAuthenticationProvider.authenticate(authRequest);
            if(authenticate == null) {
                throw new JwtAuthenticationException("유효하지 않은 인증입니다.");
            }
            setAuthentication(authenticate);
        }catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
        } finally {
            filterChain.doFilter(request, response);
        }
    }


    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
