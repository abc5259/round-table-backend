package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.CreateHouse;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.business.house.dto.event.HouseCreatedEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseAppender houseAppender;

    private final MemberHouseManager memberHouseManager;

    private final ApplicationEventPublisher eventPublisher;

    public List<HouseMember> findHouseMembers(AuthMember houseAuthMember) {
        return memberHouseManager.findHouseMembers(houseAuthMember.houseId());
    }

    public Long createHouse(CreateHouse createHouse, AuthMember authMember) {
        Long houseId = houseAppender.appendHouse(createHouse);
        memberHouseManager.enterHouse(houseId, authMember.memberId());

        eventPublisher.publishEvent(new HouseCreatedEvent(authMember.memberId(), houseId, createHouse.inviteEmails()));
        return houseId;
    }

    public void enterHouse(Long houseId, AuthMember authMember) {
        memberHouseManager.enterHouse(houseId, authMember.memberId());
    }
}
