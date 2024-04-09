package com.roundtable.roundtable.domain.schedulecomment.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberDetailDto(
        Long id,
        String name
) {
    @QueryProjection
    public MemberDetailDto {
    }
}
