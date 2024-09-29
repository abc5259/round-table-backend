package com.roundtable.roundtable.business.feedback.dto;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import java.util.List;

public record CreateFeedbackServiceDto(
        Emoji emoji,
        String message,
        Long senderId,
        Long scheduleId,
        Long scheduleCompletionId,
        List<Integer> predefinedFeedbackIds
) {

    public CreateFeedback toCreateFeedback(Member sender, ScheduleCompletion scheduleCompletion) {
        return new CreateFeedback(
                emoji,
                message,
                sender,
                scheduleCompletion,
                predefinedFeedbackIds
        );
    }
}
