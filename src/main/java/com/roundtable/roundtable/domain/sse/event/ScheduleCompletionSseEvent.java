package com.roundtable.roundtable.domain.sse.event;

import com.roundtable.roundtable.domain.sse.event.ScheduleCompletionSseEvent.ScheduleCompletionSseData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScheduleCompletionSseEvent extends SseEventTemplate<ScheduleCompletionSseData> {

    private static final String EVENT_NAME = "schedule_completion";

    public ScheduleCompletionSseEvent(ScheduleCompletionSseData eventData) {
        super(eventData);
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public static record ScheduleCompletionSseData(
            Long scheduleId,
            String scheduleName,
            String scheduleCompletionMemberNames
    ){}
}



