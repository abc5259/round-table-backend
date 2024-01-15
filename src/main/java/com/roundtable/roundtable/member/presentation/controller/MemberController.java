package com.roundtable.roundtable.member.presentation.controller;

import com.roundtable.roundtable.auth.jwt.provider.Token;
import com.roundtable.roundtable.member.application.authcode.AuthCode;
import com.roundtable.roundtable.member.application.dto.EmailRequest;
import com.roundtable.roundtable.member.application.dto.MemberLoginRequest;
import com.roundtable.roundtable.member.application.dto.MemberRegisterRequest;
import com.roundtable.roundtable.member.application.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<Void> sendAuthCode(@Valid @RequestBody final EmailRequest emailRequest) {
        memberService.sendCodeToEmail(emailRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/emails/verification-requests")
    public ResponseEntity<Void> isCorrectAuthCode(@Valid @NotBlank @RequestParam final String code) {
        boolean isCorrect = memberService.isCorrectAuthCode(new AuthCode(code));
        if(!isCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody final MemberRegisterRequest memberRegisterRequest) {
        memberService.register(memberRegisterRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Token> loginMember(@Valid @RequestBody final MemberLoginRequest memberLoginRequest) {

        Token token = memberService.login(memberLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
