package com.roundtable.roundtable.business.housework.response;

import com.roundtable.roundtable.entity.housework.HouseWorkCategory;
import java.time.LocalTime;

public record HouseWorkResponse(
        Long id,
        String name,
        HouseWorkCategory houseWorkCategory,
        LocalTime assignedTime
) {
}
