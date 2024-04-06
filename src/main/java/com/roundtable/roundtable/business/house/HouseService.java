package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.member.MemberHouseManagerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseAppender houseAppender;

    private final MemberHouseManager memberHouseManager;

    public Long createHouse(CreateHouse createHouse, AuthMember authMember) {
        Long houseId = houseAppender.appendHouse(createHouse);
        memberHouseManager.enterHouse(houseId, authMember.memberId());

        return houseId;
    }

    public void enterHouse(Long houseId, AuthMember authMember) {
        memberHouseManager.enterHouse(houseId, authMember.memberId());
    }
}
