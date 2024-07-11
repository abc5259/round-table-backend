package com.roundtable.roundtable.business.chore.dto.event;

import java.util.List;

public record ChoreCompleteEvent(
        Long houseId,
        Long completedChoreId,
        Long completedMemberId
) {
}
