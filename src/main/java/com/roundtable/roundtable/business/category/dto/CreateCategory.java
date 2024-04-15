package com.roundtable.roundtable.business.category.dto;

public record CreateCategory(

        String name,
        Integer point,
        Long memberId,
        Long houseId
) {
}
