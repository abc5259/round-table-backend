package com.roundtable.roundtable.business.house;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.CreateHouse;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class HouseServiceTest extends IntegrationTestSupport {

    @Autowired
    private HouseService houseService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("하우스를 생성한다.")
    @Test
    void createHouse() {
        //given
        CreateHouse createHouse = new CreateHouse("house");
        Member member = appendMember(null);
        AuthMember authMember = new AuthMember(member.getId(), null);

        //when
        Long result = houseService.createHouse(createHouse, authMember);

        //then
        assertThat(result).isNotNull();

        House house = houseRepository.findById(result).orElseThrow();
        assertThat(house)
                .extracting("name")
                .isEqualTo(createHouse.name());
        assertThat(house.getInviteCode()).isNotNull();
        assertThat(member.getHouse().getId()).isEqualTo(result);
    }

    @DisplayName("하우스에 이미 들어간 사람은 하우스를 생성할 수 없다.")
    @Test
    void createHouse_already_enter_house() {
        //given
        CreateHouse createHouse = new CreateHouse("house");
        House house = appendHouse();
        Member member = appendMember(house);
        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when //then
        assertThatThrownBy(() -> houseService.createHouse(createHouse, authMember))
                .isInstanceOf(MemberAlreadyHasHouseException.class)
                .hasMessage(MemberErrorCode.ALREADY_HAS_HOUSE.getMessage());
    }

    private Member appendMember(House house) {
        Member member = Member.builder().name("name").email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse() {
        House house = House.builder().name("house").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }

}