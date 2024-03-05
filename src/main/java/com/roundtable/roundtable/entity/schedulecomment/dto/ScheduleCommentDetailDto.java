package com.roundtable.roundtable.entity.schedulecomment.dto;

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
