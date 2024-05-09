package com.roundtable.roundtable.business.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MemberValidatorTest extends IntegrationTestSupport {

    @Autowired
    private MemberValidator memberValidator;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("멤버가 하우스에 들어가지 않았다면 에러를 던진다.")
    @Test
    void validateMemberBelongsToHouse() {
         //given
        Member member = createMemberInHouse(null);

        //when //then
         assertThatThrownBy(() -> memberValidator.validateMemberBelongsToHouse(member.getId()))
                 .isInstanceOf(MemberNoHouseException.class);

    }

    @DisplayName("하우스 초대는 하우스에 들어가지 않은 사용자만 가능하다.")
    @Test
    void validateCanInviteHouse() {
        //given
        Member member = createMemberInHouse(null);

        //when //then;
        assertThatNoException().isThrownBy(() -> memberValidator.validateCanInviteHouse(member));

    }

    @DisplayName("하우스 초대는 하우스에 들어가지 않은 사용자만 가능하다.")
    @Test
    void validateCanInviteHouseWithInHouse() {
        //given
        House house = createHouse("house");
        Member member = createMemberInHouse(house);

        //when //then
        assertThatThrownBy(() -> memberValidator.validateCanInviteHouse(member))
                .isInstanceOf(MemberAlreadyHasHouseException.class)
                .hasMessage(MemberErrorCode.ALREADY_HAS_HOUSE.getMessage());
    }

    private House createHouse(String name) {
        House house = House.builder().name(name).inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }

    private Member createMemberInHouse(House house) {
        Member member = Member.builder().email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }
}