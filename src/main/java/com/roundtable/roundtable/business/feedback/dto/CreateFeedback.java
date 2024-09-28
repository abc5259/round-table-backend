package com.roundtable.roundtable.business.feedback.dto;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import java.util.List;

public record CreateFeedback(
        Emoji emoji,
        String message,
        Member sender,
        ScheduleCompletion scheduleCompletion,
        List<Integer> predefinedFeedbackIds
) {
}
