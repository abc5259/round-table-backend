package com.roundtable.roundtable.presentation.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import com.roundtable.roundtable.global.response.FailResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.warn(authException.getMessage());
        // FailResponse를 JSON 문자열로 변환
        AuthErrorCode errorCode = AuthErrorCode.INVALID_AUTH_USER;
        String jsonResponse = objectMapper.writeValueAsString(
                FailResponse.fail(errorCode.getMessage(), errorCode.getCode())
        );

        // 응답 설정 및 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
