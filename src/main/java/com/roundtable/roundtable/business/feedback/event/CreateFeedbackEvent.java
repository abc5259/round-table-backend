package com.roundtable.roundtable.business.feedback.event;

public record CreateFeedbackEvent(
        Long feedbackId,
        Long senderId,
        Long choreId,
        Long houseId
) {
}
