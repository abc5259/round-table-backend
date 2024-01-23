package com.roundtable.roundtable.member.presentation.controller;

import com.roundtable.roundtable.global.config.argumentresolver.Login;
import com.roundtable.roundtable.member.application.dto.SettingProfileRequest;
import com.roundtable.roundtable.member.application.service.MemberService;
import com.roundtable.roundtable.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<Void> settingProfile(@Login Member loginMember, @Valid @RequestBody final SettingProfileRequest settingProfileRequest) {
        memberService.settingProfile(loginMember, settingProfileRequest);
        return ResponseEntity.ok().build();
    }

}
