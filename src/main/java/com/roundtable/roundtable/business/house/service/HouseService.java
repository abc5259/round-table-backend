package com.roundtable.roundtable.business.house.service;

import com.roundtable.roundtable.business.house.dto.CreateHouseRequest;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.business.house.exception.HouseException.HouseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository houseRepository;

    public void createHouse(CreateHouseRequest createHouseRequest, Member houseOwner) {
        House house = House.of(createHouseRequest.name());
        houseRepository.save(house);

        houseOwner.enterHouse(house);
    }

    public House findById(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(HouseNotFoundException::new);
    }
}
