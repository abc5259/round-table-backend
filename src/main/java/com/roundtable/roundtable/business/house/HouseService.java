package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.house.CreateHouse;
import com.roundtable.roundtable.implement.house.HouseMaker;
import com.roundtable.roundtable.implement.house.HouseReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseService {

    private final HouseReader houseReader;
    private final HouseMaker houseMaker;

    public void createHouse(CreateHouse createHouse, Member houseOwner) {
        houseMaker.createHouse(createHouse, houseOwner);
    }

    public House findById(Long houseId) {
        return houseReader.findById(houseId);
    }
}
