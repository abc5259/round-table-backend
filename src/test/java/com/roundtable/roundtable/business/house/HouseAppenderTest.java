package com.roundtable.roundtable.business.house;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.house.dto.CreateHouse;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class HouseAppenderTest extends IntegrationTestSupport {

    @Autowired
    private HouseAppender houseAppender;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("House를 추가할 수 있다.")
    @Test
    void appendHouse() {
        //given
        CreateHouse createHouse = new CreateHouse("house");

        //when
        Long result = houseAppender.appendHouse(createHouse);

        //then
        assertThat(result).isNotNull();

        House house = houseRepository.findById(result).orElseThrow();
        assertThat(house)
                .extracting("name")
                .isEqualTo(createHouse.name());
        assertThat(house.getInviteCode()).isNotNull();
    }
}