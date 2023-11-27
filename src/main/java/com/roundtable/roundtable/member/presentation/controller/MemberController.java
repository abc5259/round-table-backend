package com.roundtable.roundtable.member.presentation.controller;

import com.roundtable.roundtable.member.application.authcode.AuthCode;
import com.roundtable.roundtable.member.application.authcode.AuthCodeStoreStrategy;
import com.roundtable.roundtable.member.application.dto.EmailDto;
import com.roundtable.roundtable.member.application.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendAuthCode(@Valid final EmailDto emailDto) {
        memberService.sendCodeToEmail(emailDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/emails/verification-requests")
    public ResponseEntity isCorrectAuthCode(@Valid @NotBlank String code) {
        boolean isCorrect = memberService.isCorrectAuthCode(new AuthCode(code));
        if(!isCorrect) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
