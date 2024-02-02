package com.roundtable.roundtable.presentation.auth;

import com.roundtable.roundtable.presentation.auth.jwt.JwtAuthenticationConverter;
import com.roundtable.roundtable.business.auth.service.Token;
import com.roundtable.roundtable.business.auth.authcode.AuthCode;
import com.roundtable.roundtable.business.auth.dto.EmailRequest;
import com.roundtable.roundtable.business.auth.dto.LoginRequest;
import com.roundtable.roundtable.business.auth.dto.RegisterRequest;
import com.roundtable.roundtable.business.auth.service.AuthService;
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
    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    private final AuthService memberService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendAuthCode(@Valid @RequestBody final EmailRequest emailRequest) {
        memberService.sendCodeToEmail(emailRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/emails/verification-requests")
    public ResponseEntity<Void> isCorrectAuthCode(@Valid @NotBlank @RequestParam final String code) {
        boolean isCorrect = memberService.isCorrectAuthCode(new AuthCode(code));
        if(!isCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody final RegisterRequest memberRegisterRequest) {
        memberService.register(memberRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@Valid @RequestBody final LoginRequest memberLoginRequest) {

        Token token = memberService.login(memberLoginRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from(JwtAuthenticationConverter.COOKIE_AUTH_TOKEN, token.getRefreshToken())
                .httpOnly(true) // HttpOnly 속성을 사용하여 JavaScript로 쿠키에 접근하는 것을 방지
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME) // 쿠키의 만료 시간을 설정 (초 단위, 여기서는 7일)
                .path("/")
                .build(); // 쿠키의 유효 경로 설정

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginResponse(token.getAccessToken()));
    }
}
