package com.roundtable.roundtable.presentation.member;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.presentation.argumentresolver.Login;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.FailResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.member.request.ExistEmailRequest;
import com.roundtable.roundtable.presentation.member.request.SettingProfileRequest;
import com.roundtable.roundtable.business.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final HouseService houseService;

    // TODO: api url 어떻게 할지 고민..
    // @PATCH /members/me 로 바꾸고 파라미터로 바꿀거 다 받아서 null 값이 아닌 부분만 바꿔주자!
    @PatchMapping("/setting/profile")
    public ResponseEntity<ApiResponse<Void>> settingProfile(@Login AuthMember authMember, @Valid @RequestBody final SettingProfileRequest settingProfileRequest) {
        memberService.settingProfile(authMember.memberId(), settingProfileRequest.toMemberProfile());
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @GetMapping("/exist")
    public ResponseEntity<ApiResponse<?>> existMemberEmail(@Valid @ModelAttribute ExistEmailRequest existEmailRequest) {
        boolean isExistEmail = memberService.isExistEmail(existEmailRequest.email());

        if(!isExistEmail) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FailResponse.fail("이메일에 해당하는 유저가 존재하지 않습니다."));
        }

        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @PatchMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<?>> enterHouse(@PathVariable Long houseId, @Login AuthMember authMember) {
        houseService.enterHouse(houseId, authMember);
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }
}
