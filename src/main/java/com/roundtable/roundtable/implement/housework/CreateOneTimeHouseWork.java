package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.HouseWorkCategory;
import java.time.LocalDateTime;

public record CreateOneTimeHouseWork(
        String name,
        HouseWorkCategory houseWorkCategory,
        int currSequence,
        int sequenceSize,
        LocalDateTime assignedDate
) {
}
