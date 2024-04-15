package com.roundtable.roundtable.presentation.schedulecomment.request;

import com.roundtable.roundtable.business.schedulecomment.dto.CreateScheduleCommentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateScheduleCommentRequest(
        @NotBlank
        String content,
        @NotNull
        Long scheduleId
) {
    public CreateScheduleCommentDto toCreateScheduleCommentDto() {
        return new CreateScheduleCommentDto(
                content,
                scheduleId
        );
    }

}
