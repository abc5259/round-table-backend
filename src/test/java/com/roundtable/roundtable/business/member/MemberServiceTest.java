package com.roundtable.roundtable.business.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.member.dto.response.MemberDetailResponse;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Gender;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("사용자 id를 이용해 Detail한 정보를 알 수 있다.")
    @Test
    void findMemberDetail() {
        //given
        Member member = createMember("lee", Gender.MEN, null);

        //when
        MemberDetailResponse result = memberService.findMemberDetail(member.getId());

        //then
        assertThat(result).extracting("memberId", "name", "gender", "house")
                .contains(member.getName(), member.getGender(), null);
    }

    @DisplayName("사용자가 하우스에 들어가 있다면 House에 관한 세부사항도 알 수 있다.")
    @Test
    void findMemberDetail_with_house() {
        //given
        House house = createHouse("house");
        Member member = createMember("lee", Gender.MEN, house);

        //when
        MemberDetailResponse result = memberService.findMemberDetail(member.getId());

        //then
        assertThat(result).extracting("memberId", "name", "gender")
                .contains(member.getName(), member.getGender());
        assertThat(result.house()).extracting("houseId", "name")
                .contains(house.getName(), house.getName());
    }

    @DisplayName("사용자 id가 데이터베이스에 없다면 세부사항을 알려할때 에러를 던진다.")
    @Test
    void findMemberDetail_non_exist_member_id() {
        //given
        Long nonExistMemberId = 1L;

        //when
        assertThatThrownBy(() -> memberService.findMemberDetail(nonExistMemberId))
                .isInstanceOf(NotFoundEntityException.class);
    }


    @DisplayName("사용자가 하우스 초대를 받을 수 있는지 알 수 있다.")
    @Test
    void canInviteHouse() {
        //given
        Member member = createMember("name", Gender.MEN, null);

        //when
        boolean result = memberService.canInviteHouse(member.getId());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("하우스에 이미 들어간 사용자가 하우스 초대를 받을 수 없다.")
    @Test
    void canInviteHouse_already_enter_house() {
        //given
        Member member = createMember("name", Gender.MEN, createHouse("house"));

        //when
        boolean result = memberService.canInviteHouse(member.getId());

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("하우스 초대는 하우스에 들어가지 않은 사용자만 가능하다.")
    @Test
    void validateCanInviteHouse() {
        //given
        Member member = createMember(null);

        //when //then;
        assertThatNoException().isThrownBy(() -> memberService.validateCanInviteHouse(member.getEmail()));

    }

    @DisplayName("하우스 초대 검증에서 존재하지 않는 이메일이라면 에러를 던진다.")
    @Test
    void validateCanInviteHouseWithNoEmail() {
        //given
        String email = "email";

        //when //then
        assertThatThrownBy(() -> memberService.validateCanInviteHouse(email))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(MemberErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("하우스 초대는 하우스에 들어가지 않은 사용자만 가능하다.")
    @Test
    void validateCanInviteHouseWithInHouse() {
        //given
        House house = createHouse("house");
        Member member = createMember(house);

        //when //then
        assertThatThrownBy(() -> memberService.validateCanInviteHouse(member.getEmail()))
                .isInstanceOf(MemberAlreadyHasHouseException.class)
                .hasMessage(MemberErrorCode.ALREADY_HAS_HOUSE.getMessage());
    }

    private Member createMember(String name, Gender gender, House house) {
        Member member = Member.builder()
                .email("email")
                .password("password")
                .name(name)
                .gender(gender)
                .house(house)
                .build();
        return memberRepository.save(member);
    }

    private Member createMember(House house) {
        Member member = Member.builder()
                .email("email")
                .password("password")
                .name("name")
                .gender(Gender.GIRL)
                .house(house)
                .build();
        return memberRepository.save(member);
    }

    private House createHouse(String name) {
        House house = House.builder().name(name).inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }
}