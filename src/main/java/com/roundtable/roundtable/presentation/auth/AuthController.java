package com.roundtable.roundtable.presentation.auth;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.FailResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.auth.AuthService;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.auth.request.EmailRequest;
import com.roundtable.roundtable.presentation.auth.request.LoginRequest;
import com.roundtable.roundtable.presentation.auth.request.RefreshTokenRequest;
import com.roundtable.roundtable.presentation.auth.request.RegisterRequest;
import com.roundtable.roundtable.presentation.auth.response.LoginResponse;
import com.roundtable.roundtable.presentation.auth.response.MemberCreateResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final AuthService authService;

    @PostMapping("/emails")
    public ResponseEntity<ApiResponse<Void>> sendAuthCode(@Valid @RequestBody final EmailRequest emailRequest) {
        authService.sendCodeToEmail(emailRequest.email());
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @GetMapping("/emails")
    public ResponseEntity<ApiResponse<Void>> isCorrectAuthCode(
            @Valid @NotBlank @Email @RequestParam final String email,
            @Valid @NotBlank @RequestParam final String code) {
        boolean isCorrect = authService.isCorrectAuthCode(AuthCode.of(email, code));
        if(!isCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(FailResponse.fail(
                    "인증코드가 잘못되었습니다."
            ));
        }
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<MemberCreateResponse>> registerMember(@Valid @RequestBody final RegisterRequest memberRegisterRequest) {
        Long memberId = authService.register(memberRegisterRequest.toRegisterMember());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    SuccessResponse.from(new MemberCreateResponse(memberId))
                );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginMember(@Valid @RequestBody final LoginRequest memberLoginRequest) {

        Tokens token = authService.login(memberLoginRequest.toLoginMember());

        return ResponseEntity.ok()
                .body(
                        new LoginResponse(token.getAccessToken(), token.getRefreshToken())
                );
    }
}
