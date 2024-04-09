package com.roundtable.roundtable.business.house;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class HouseReaderTest extends IntegrationTestSupport {

    @Autowired
    private HouseReader houseReader;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("id에 해당하는 House를 조회할 수 있다.")
    @Test
    void findById() {
        //given
        InviteCode inviteCode = InviteCode.builder().code("code").build();
        House house = House.builder().name("house").inviteCode(inviteCode).build();
        houseRepository.save(house);

        //when
        House result = houseReader.findById(house.getId());

        //then
        assertThat(result).isNotNull()
                .extracting("name", "inviteCode")
                .contains(house.getName(), house.getInviteCode());
     }

    @DisplayName("id에 해당하는 House가 없으면 에러를 던진다.")
    @Test
    void findById_fail() {
        //given

        //when //then
        assertThatThrownBy(() -> houseReader.findById(1L))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(HouseErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("특정 InviteCode가 있는지 확인할 수 있다.")
    @Test
    void isExistInviteCode() {
        //given
        InviteCode inviteCode = InviteCode.builder().code("code").build();
        House house = House.builder().name("house").inviteCode(inviteCode).build();
        houseRepository.save(house);

        //when
        boolean result = houseRepository.existsByInviteCode(inviteCode);

        //then
        assertThat(result).isTrue();
    }
}