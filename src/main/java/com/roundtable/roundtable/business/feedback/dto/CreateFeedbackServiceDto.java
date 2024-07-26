package com.roundtable.roundtable.business.feedback.dto;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;

public record CreateFeedbackServiceDto(
        Emoji emoji,
        String message,
        Long senderId,
        Long choreId,
        List<Integer> predefinedFeedbackIds
) {

    public CreateFeedback toCreateFeedback(Member sender, Chore chore) {
        return new CreateFeedback(
                emoji,
                message,
                sender,
                chore,
                predefinedFeedbackIds
        );
    }
}
