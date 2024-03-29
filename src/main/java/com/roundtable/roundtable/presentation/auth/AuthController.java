package com.roundtable.roundtable.presentation.auth;

import com.roundtable.roundtable.global.properties.JwtProperties;
import com.roundtable.roundtable.presentation.auth.jwt.JwtAuthenticationConverter;
import com.roundtable.roundtable.business.auth.Token;
import com.roundtable.roundtable.business.auth.authcode.AuthCode;
import com.roundtable.roundtable.business.auth.AuthService;
import com.roundtable.roundtable.presentation.auth.request.EmailRequest;
import com.roundtable.roundtable.presentation.auth.request.LoginRequest;
import com.roundtable.roundtable.presentation.auth.request.RegisterRequest;
import com.roundtable.roundtable.presentation.auth.response.LoginResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProperties jwtProperties;

    private final AuthService authService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendAuthCode(@Valid @RequestBody final EmailRequest emailRequest) {
        authService.sendCodeToEmail(emailRequest.email());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emails/verification-requests")
    public ResponseEntity<Void> isCorrectAuthCode(@Valid @NotBlank @RequestParam final String code) {
        boolean isCorrect = authService.isCorrectAuthCode(new AuthCode(code));
        if(!isCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody final RegisterRequest memberRegisterRequest) {
        authService.register(memberRegisterRequest.toRegisterMember());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@Valid @RequestBody final LoginRequest memberLoginRequest) {

        Token token = authService.login(memberLoginRequest.toLoginMember());

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from(JwtAuthenticationConverter.COOKIE_AUTH_TOKEN, token.getRefreshToken())
                .httpOnly(true) // HttpOnly 속성을 사용하여 JavaScript로 쿠키에 접근하는 것을 방지
                .maxAge(jwtProperties.getRefreshTokenExpireTime()) // 쿠키의 만료 시간을 설정 (초 단위, 여기서는 7일)
                .path("/")
                .build(); // 쿠키의 유효 경로 설정

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse(token.getAccessToken()));
    }
}
