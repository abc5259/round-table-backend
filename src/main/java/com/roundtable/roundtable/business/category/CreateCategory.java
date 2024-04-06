package com.roundtable.roundtable.business.category;

import com.roundtable.roundtable.entity.house.House;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

public record CreateCategory(

        String name,
        Integer point,
        Long memberId,
        Long houseId
) {
}
