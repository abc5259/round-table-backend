package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.member.MemberHouseManager;
import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseAppender houseAppender;
    private final MemberHouseManager memberHouseManager;

    public Long createHouse(CreateHouse createHouse, Member houseOwner) {
        Long houseId = houseAppender.appendHouse(createHouse);
        memberHouseManager.enterHouse(houseId, houseOwner);

        return houseId;
    }

    public void enterHouse(Long houseId, Member loginMember) {
        memberHouseManager.enterHouse(houseId, loginMember);
    }


}
