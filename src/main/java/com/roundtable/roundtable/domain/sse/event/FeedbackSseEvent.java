package com.roundtable.roundtable.domain.sse.event;

import com.roundtable.roundtable.domain.sse.event.FeedbackSseEvent.FeedbackSseData;

public class FeedbackSseEvent extends SseEventTemplate<FeedbackSseData> {

    private static final String EVENT_NAME = "feedback";

    private FeedbackSseEvent(FeedbackSseData eventData) {
        super(eventData);
    }

    public static FeedbackSseEvent of(String senderName, Long feedbackId, String scheduleName) {
        return new FeedbackSseEvent(new FeedbackSseData(senderName, feedbackId, scheduleName));
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public record FeedbackSseData(
            String senderName,
            Long feedbackId,
            String scheduleName
    ) {}
}
