package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.house.HouseReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberHouseManager {

    private final HouseReader houseReader;

    public void enterHouse(Long houseId, Member member) {
        House house = houseReader.findById(houseId);
        member.enterHouse(house);
    }
}
