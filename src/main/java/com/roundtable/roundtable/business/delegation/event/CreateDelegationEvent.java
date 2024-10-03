package com.roundtable.roundtable.business.delegation.event;

import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.member.Member;

public record CreateDelegationEvent(
        Long houseId,
        Member sender,
        Member receiver,
        Long scheduleId,
        Delegation delegation
) {
}
