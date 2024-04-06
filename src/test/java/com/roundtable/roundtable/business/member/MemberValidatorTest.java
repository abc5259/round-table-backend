package com.roundtable.roundtable.business.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import org.assertj.core.api.Assertions;
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

    @DisplayName("하우스에 들어가 있는 member라면 에러를 던지지 않는다.")
    @Test
    void validateMemberInHouse() {
        //given
        Member member = Member.builder()
                .email("email")
                .password("password")
                .build();
        member.enterHouse(House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build());

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
        assertThatThrownBy(() -> memberValidator.validateMemberInHouse(member))
                .isInstanceOf(MemberNoHouseException.class);
     }

     @DisplayName("멤버가 하우스에 들어가지 않았다면 에러를 던진다.")
     @Test
     void validateMemberBelongsToHouse() {
         //given
         Member member = Member.builder()
                 .email("email")
                 .password("password")
                 .build();

         //when //then
         assertThatThrownBy(() -> memberValidator.validateMemberBelongsToHouse(member.getId(), null))
                 .isInstanceOf(MemberNoHouseException.class);

      }

      @DisplayName("하우스에 속하지 않는 멤버라면 에러를 던진다.")
      @Test
      void test() {
          //given
          House house = createHouse("house");
          Member member = createMemberInHouse(house);

          //when
          assertThatThrownBy(() -> memberValidator.validateMemberBelongsToHouse(member.getId(), house.getId() + 1))
                  .isInstanceOf(MemberNotSameHouseException.class);

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