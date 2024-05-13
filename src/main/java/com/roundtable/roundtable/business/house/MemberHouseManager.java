package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.house.dto.HouseMember;
import java.util.List;

public interface MemberHouseManager {
    void enterHouse(Long houseId, Long memberId);

    List<HouseMember> findHouseMembers(Long houseId);
}

