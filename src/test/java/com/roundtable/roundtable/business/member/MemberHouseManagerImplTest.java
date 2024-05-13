package com.roundtable.roundtable.business.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MemberHouseManagerImplTest extends IntegrationTestSupport {

    @Autowired
    private MemberHouseManagerImpl memberHouseManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("하우스에 속한 모든 맴버를 조회할 수 있다.")
    @Test
    void findHouseMembers() {
        //given
        House house1 = createHouse("code1");
        House house2 = createHouse("code2");

        Member member = createMember(house1, "email1");
        Member member2 = createMember(house1, "email2");
        Member member3 = createMember(house2, "email3");

        //when
        List<HouseMember> result = memberHouseManager.findHouseMembers(house1.getId());

        //then
        assertThat(result).hasSize(2)
                .extracting("memberId")
                .contains(
                        member.getId(), member2.getId()
                );
    }

    public House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    public Member createMember(House house, String email) {
        Member member = Member.builder().house(house).email(email).password("password").build();
        return memberRepository.save(member);
    }
}