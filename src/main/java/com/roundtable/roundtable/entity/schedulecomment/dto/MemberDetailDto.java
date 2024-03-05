package com.roundtable.roundtable.entity.schedulecomment.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberDetailDto(
        Long id,
        String name
) {
    @QueryProjection
    public MemberDetailDto {
    }
}
