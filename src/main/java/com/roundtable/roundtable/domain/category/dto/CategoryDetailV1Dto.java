package com.roundtable.roundtable.domain.category.dto;

import com.querydsl.core.annotations.QueryProjection;

public record CategoryDetailV1Dto(
        Long categoryId,
        String name,
        Integer point
) {

    @QueryProjection
    public CategoryDetailV1Dto {
    }
}
