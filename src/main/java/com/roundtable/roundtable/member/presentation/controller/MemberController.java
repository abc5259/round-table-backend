package com.roundtable.roundtable.member.presentation.controller;

import com.roundtable.roundtable.global.config.argumentresolver.Login;
import com.roundtable.roundtable.global.presentation.response.ErrorResponse;
import com.roundtable.roundtable.global.presentation.response.Response;
import com.roundtable.roundtable.global.presentation.response.SuccessResponse;
import com.roundtable.roundtable.member.application.dto.ExistEmailRequest;
import com.roundtable.roundtable.member.application.dto.SettingProfileRequest;
import com.roundtable.roundtable.member.application.service.MemberService;
import com.roundtable.roundtable.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    // TODO: api url 어떻게 할지 고민..
    // @PATCH /members/me 로 바꾸고 파라미터로 바꿀거 다 받아서 null 값이 아닌 부분만 바꿔주자!
    @PatchMapping("/setting/profile")
    public ResponseEntity<SuccessResponse<?>> settingProfile(@Login Member loginMember, @Valid @RequestBody final SettingProfileRequest settingProfileRequest) {
        memberService.settingProfile(loginMember, settingProfileRequest);
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @GetMapping("/exist")
    public ResponseEntity<Response<?>> existMemberEmail(@Valid @ModelAttribute ExistEmailRequest existEmailRequest) {
        boolean isExistEmail = memberService.isExistEmail(existEmailRequest);

        if(!isExistEmail) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.fail("이메일에 해당하는 유저가 존재하지 않습니다."));
        }

        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

}
