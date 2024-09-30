package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleCompletionNotificationResponse extends NotificationResponse {
    private Long scheduleId;
    private String scheduleName;
    private String memberNames;

    public ScheduleCompletionNotificationResponse(
            Long id,
            NotificationType type,
            LocalDateTime createdAt,
            Long scheduleId,
            String scheduleName,
            String memberNames
    ) {
        super(id, type, createdAt);
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.memberNames = memberNames;
    }
}
