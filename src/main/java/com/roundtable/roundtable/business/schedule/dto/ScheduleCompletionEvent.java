package com.roundtable.roundtable.business.schedule.dto;

import java.util.List;

public record ScheduleCompletionEvent(
        Long houseId,
        Long scheduleId,
        List<Long> managerIds
) {
}
