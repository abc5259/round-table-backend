package com.roundtable.roundtable.business.feedback.dto;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;

public record CreateFeedback(
        Emoji emoji,
        String message,
        Member sender,
        Member receiver,
        Chore chore,
        List<Long> predefinedFeedbackIds
) {
}
