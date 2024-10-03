package com.roundtable.roundtable.business.delegation.event;

import com.roundtable.roundtable.domain.delegation.Delegation;

public record UpdateDelegationEvent(
        Long houseId,
        Delegation delegation
) {
}
