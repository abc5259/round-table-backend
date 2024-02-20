package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.implement.house.CreateHouse;
import com.roundtable.roundtable.implement.house.HouseMaker;
import com.roundtable.roundtable.implement.member.MemberHouseManager;
import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseMaker houseMaker;
    private final MemberHouseManager memberHouseManager;

    public void createHouse(CreateHouse createHouse, Member houseOwner) {
        houseMaker.createHouse(createHouse, houseOwner);
    }

    public void enterHouse(Long houseId, Member loginMember) {
        memberHouseManager.enterHouse(houseId, loginMember);
    }


}
