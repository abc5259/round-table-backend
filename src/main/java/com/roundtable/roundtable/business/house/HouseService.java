package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.member.MemberHouseManager;
import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
