package com.roundtable.roundtable.domain.schedulecomment.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ScheduleCommentDetailDto(
        Long commentId,
        String content,
        MemberDetailDto writer
) {
    @QueryProjection
    public ScheduleCommentDetailDto {
    }
}
