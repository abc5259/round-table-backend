package com.roundtable.roundtable.entity.housework;

import java.time.LocalDateTime;
import java.util.List;

public record CreateOneTimeHouseWork(
        String name,
        HouseWorkCategory houseWorkCategory,
        int currSequence,
        int sequenceSize,
        LocalDateTime assignedDate
) {
}
