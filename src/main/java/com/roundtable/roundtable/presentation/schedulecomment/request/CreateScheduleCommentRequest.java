package com.roundtable.roundtable.presentation.schedulecomment.request;

import com.roundtable.roundtable.business.schdulecomment.CreateScheduleCommentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateScheduleCommentRequest(
        @NotBlank
        String content,
        @NotNull
        Long scheduleId
) {
    public CreateScheduleCommentDto toCreateScheduleCommentDto(Long memberId) {
        return new CreateScheduleCommentDto(
                content,
                memberId,
                scheduleId
        );
    }

}
