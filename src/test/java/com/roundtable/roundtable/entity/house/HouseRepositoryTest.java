package com.roundtable.roundtable.entity.house;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class HouseRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("특정 InviteCode가 있는지 확인할 수 있다.")
    @Test
    void existsByInviteCode() {
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