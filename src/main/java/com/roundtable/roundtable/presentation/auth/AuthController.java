package com.roundtable.roundtable.presentation.auth;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.FailResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.auth.AuthService;
import com.roundtable.roundtable.presentation.auth.request.EmailRequest;
import com.roundtable.roundtable.presentation.auth.request.LoginRequest;
import com.roundtable.roundtable.presentation.auth.request.RegisterRequest;
import com.roundtable.roundtable.presentation.auth.response.LoginResponse;
import com.roundtable.roundtable.presentation.auth.response.MemberCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "이메일 인증 코드 전송", description = "이메일 인증 코드를 전송합니다. 인증 코드 유효기간은 3분입니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/emails")
    public ResponseEntity<ResponseDto<Void>> sendAuthCode(@Valid @RequestBody final EmailRequest emailRequest) {
        authService.sendCodeToEmail(emailRequest.email());
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @Operation(summary = "이메일 인증 코드 검증", description = "이메일 인증 코드가 맞는지 검증합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "이메일 인증 코드 검증 실패")
    @GetMapping("/emails")
    public ResponseEntity<ResponseDto<Void>> isCorrectAuthCode(
            @Valid @NotBlank @Email @RequestParam final String email,
            @Valid @NotBlank @RequestParam final String code) {
        boolean isCorrect = authService.isCorrectAuthCode(AuthCode.of(email, code));
        if(!isCorrect) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FailResponse.fail(
                    "인증코드가 잘못되었습니다."
            ));
        }
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<MemberCreateResponse>> registerMember(@Valid @RequestBody final RegisterRequest memberRegisterRequest) {
        Long memberId = authService.register(memberRegisterRequest.toRegisterMember());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    SuccessResponse.from(new MemberCreateResponse(memberId))
                );
    }

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> loginMember(@Valid @RequestBody final LoginRequest memberLoginRequest) {

        Tokens token = authService.login(memberLoginRequest.toLoginMember());

        return ResponseEntity.ok()
                .body(
                        SuccessResponse.from(new LoginResponse(token.getAccessToken(), token.getRefreshToken()))
                );
    }
}
