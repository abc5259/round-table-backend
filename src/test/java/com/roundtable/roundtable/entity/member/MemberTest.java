package com.roundtable.roundtable.entity.member;

import static com.roundtable.roundtable.entity.member.Gender.*;
import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("password를 passwordEncoder로 encode한 Member를 생성한다.")
    @Test
    void of() {
        //given
        String password = "password";

        //when
        Member member = Member.of("email", password, passwordEncoder);

        //then
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
     }

    @DisplayName("member는 비밀번호가 맞는지 확인할 수 있다.")
    @Test
    void isCorrectPassword() {
        //given
        String password = "password";
        Member member = Member.of("email", password, passwordEncoder);

        //when
        boolean isCorrect = member.isCorrectPassword(password, passwordEncoder);

        //then
        assertThat(isCorrect).isTrue();
    }



    @DisplayName("member는 비밀번호가 틀린지 확인할 수 있다.")
    @Test
    void isNotCorrectPassword() {
        //given
        String password = "password";
        Member member = Member.of("email", password, passwordEncoder);

        //when
        boolean isCorrect = member.isCorrectPassword(password, passwordEncoder);
        //then
        assertThat(member.isCorrectPassword("pppp", passwordEncoder)).isFalse();
    }

    @DisplayName("member는 프로필에 대한 정보를 setting 할 수 있다.")
    @Test
    void settingProfile() {
        //given
        String name = "name";
        Gender gender = MEN;
        String password = "password";
        Member member = Member.of("email", password, passwordEncoder);

        //when
        member.settingProfile(name, gender);

        //then
        assertThat(member)
                .extracting("name", "gender")
                .contains(name, gender);
    }

    @DisplayName("member는 하우스에 들어갈 수 있다.")
    @Test
    void enterHouse() {
        //given
        House house = House.of("house");
        Member member = Member.of("email", "password", passwordEncoder);

        //when
        member.enterHouse(house);

        //then
        assertThat(member.getHouse()).isEqualTo(house);
     }

    @DisplayName("member는 하우스에 이미 들어가 있으면 다른 하우스에 들어가면 에러를 던진다.")
    @Test
    void enterHouse_fail() {
        //given
        House house1 = House.of("house1");
        House house2 = House.of("house2");
        Member member = Member.of("email", "password", passwordEncoder);
        member.enterHouse(house1);

        //when
        assertThatThrownBy(() -> member.enterHouse(house2))
                .isInstanceOf(MemberAlreadyHasHouseException.class)
                .hasMessage("참여중인 하우스가 있습니다.");
    }
}