package com.roundtable.roundtable.presentation.member;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.FailResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.member.request.CheckInviteRequest;
import com.roundtable.roundtable.presentation.member.request.ExistEmailRequest;
import com.roundtable.roundtable.presentation.member.request.SettingProfileRequest;
import com.roundtable.roundtable.business.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    @Operation(summary = "로그인 유저 정보 수정", description = "로그인 유저의 이름과 성별을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/setting/profile")
    public ResponseEntity<ResponseDto<Void>> settingProfile(@Login AuthMember authMember, @Valid @RequestBody final SettingProfileRequest settingProfileRequest) {
        memberService.settingProfile(authMember.memberId(), settingProfileRequest.toMemberProfile());
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @Operation(summary = "로그인 유저 정보 조회", description = "로그인 유저의 상세한 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<?>> me(@Login AuthMember authMember) {
        return ResponseEntity.ok(SuccessResponse.from(
                memberService.findMemberDetail(authMember.memberId())
        ));
    }

    @Operation(summary = "이메일 존재 여부 확인", description = "요청으로 온 이메일을 가진 맴버가 있는지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/exist")
    public ResponseEntity<ResponseDto<?>> existMemberEmail(@Valid @ModelAttribute ExistEmailRequest existEmailRequest) {
        boolean isExistEmail = memberService.isExistEmail(existEmailRequest.email());

        if(!isExistEmail) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(FailResponse.fail("이메일에 해당하는 유저가 존재하지 않습니다."));
        }

        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @Operation(summary = "하우스 초대 가능 여부 조회", description = "하우스에 초대 가능한 유저인지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/check-invite")
    public ResponseEntity<ResponseDto<?>> checkInviteHouse(@Valid @ModelAttribute CheckInviteRequest checkInviteRequest) {
        memberService.validateCanInviteHouse(checkInviteRequest.email());
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }

    @Operation(summary = "하우스 참여", description = "하우스에 참여하는 api")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/house")
    public ResponseEntity<ResponseDto<?>> enterHouse(@RequestParam Long houseId, @Login AuthMember authMember) {
        houseService.enterHouse(houseId, authMember);
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }
}
