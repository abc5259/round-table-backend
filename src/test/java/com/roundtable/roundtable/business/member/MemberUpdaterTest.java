package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.member.dto.MemberProfile;
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
class MemberUpdaterTest extends IntegrationTestSupport {
    @Autowired
    MemberUpdater memberUpdater;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HouseRepository houseRepository;

    @DisplayName("member의 프로필을 업데이트할 수 있다.")
    @Test
    void test() {
        //given
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        Member member = Member.builder().house(house).email("email").password("password").build();
        Member savedMember = memberRepository.save(member);

        String name = "name";
        Gender gender = Gender.MEN;
        MemberProfile memberProfile = new MemberProfile(name, gender);

        //when
        memberUpdater.settingProfile(savedMember.getId(), memberProfile);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();
        Assertions.assertThat(findMember).isNotNull()
                .extracting("name", "gender")
                .contains(name, gender);

    }
}