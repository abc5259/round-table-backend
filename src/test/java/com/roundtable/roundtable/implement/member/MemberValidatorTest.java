package com.roundtable.roundtable.implement.member;

import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.member.MemberException.MemberNoHouseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberValidatorTest {
    MemberValidator memberValidator = new MemberValidator();

    @DisplayName("하우스에 들어가 있는 member라면 에러를 던지지 않는다.")
    @Test
    void validateMemberInHouse() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .build();
        member.enterHouse(House.of("house"));

        //when //then
        assertDoesNotThrow(() -> memberValidator.validateMemberInHouse(member));
    }
    @DisplayName("하우스에 들어가 있지 않은 member라면 에러를 던진다.")
    @Test
    void validateMemberInHouse_throw() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .build();

        //when //then
        Assertions.assertThatThrownBy(() -> memberValidator.validateMemberInHouse(member))
                .isInstanceOf(MemberNoHouseException.class);
     }
}