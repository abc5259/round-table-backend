package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Gender;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberUpdaterTest {
    @Autowired
    MemberUpdater memberUpdater;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("member의 프로필을 업데이트할 수 있다.")
    @Test
    void test() {
        //given
        Member member = Member.builder().email("email").password("password").build();
        Member savedMember = memberRepository.save(member);

        String name = "name";
        Gender gender = Gender.MEN;
        MemberProfile memberProfile = new MemberProfile(name, gender);

        //when
        memberUpdater.settingProfile(savedMember, memberProfile);

        //then
        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();
        Assertions.assertThat(findMember).isNotNull()
                .extracting("name", "gender")
                .contains(name, gender);

    }
}