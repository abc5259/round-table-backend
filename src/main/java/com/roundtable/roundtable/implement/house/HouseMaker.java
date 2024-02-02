package com.roundtable.roundtable.implement.house;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseMaker {
    private final HouseRepository houseRepository;

    public void createHouse(CreateHouse createHouse, Member houseOwner) {
        House house = House.of(createHouse.name());
        houseRepository.save(house);

        houseOwner.enterHouse(house);
    }
}
