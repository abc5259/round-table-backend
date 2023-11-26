package com.roundtable.roundtable.member.controller;

import com.roundtable.roundtable.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/emails/verification-requests")
    public ResponseEntity sendMessage(@RequestParam("email") String email) {
        memberService.sendCodeToEmail(email);
        return new ResponseEntity(HttpStatus.OK);
    }
}
