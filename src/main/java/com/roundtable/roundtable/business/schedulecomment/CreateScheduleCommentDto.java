package com.roundtable.roundtable.business.schedulecomment;

public record CreateScheduleCommentDto(
        String content,
        Long writerId,
        Long scheduleId
) {
}
