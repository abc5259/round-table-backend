package com.roundtable.roundtable.business.chore.event;

public record ChoreCompleteEvent(
        Long houseId,
        Long completedChoreId,
        Long completedMemberId
) {
}
