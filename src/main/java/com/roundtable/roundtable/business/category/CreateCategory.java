package com.roundtable.roundtable.business.category;

public record CreateCategory(

        String name,
        Integer point,
        Long memberId,
        Long houseId
) {
}
