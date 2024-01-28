package com.roundtable.roundtable.house.application.service;

import com.roundtable.roundtable.house.application.dto.CreateHouseRequest;
import com.roundtable.roundtable.house.domain.House;
import com.roundtable.roundtable.house.domain.HouseRepository;
import com.roundtable.roundtable.house.exception.HouseException.HouseNotFoundException;
import com.roundtable.roundtable.member.domain.Member;
import com.roundtable.roundtable.member.domain.MemberRepository;
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
