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

    private House createHouse(String name) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }
}