package com.roundtable.roundtable.business.schdulecomment;

public record CreateScheduleCommentDto(
        String content,
        Long writerId,
        Long scheduleId
) {
}
