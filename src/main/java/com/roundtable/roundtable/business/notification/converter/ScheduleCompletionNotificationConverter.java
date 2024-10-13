package com.roundtable.roundtable.business.notification.converter;

import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.ScheduleCompletionNotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import com.roundtable.roundtable.domain.notification.ScheduleCompletionNotification;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCompletionNotificationConverter implements NotificationResponseConverter {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        ScheduleCompletionNotification scheduleCompletionNotification = (ScheduleCompletionNotification) notification;

        return new ScheduleCompletionNotificationResponse(
                scheduleCompletionNotification.getId(),
                NotificationType.SCHEDULE_COMPLETION,
                scheduleCompletionNotification.getCreatedAt(),
                scheduleCompletionNotification.getSender().getId(),
                scheduleCompletionNotification.getSender().getName(),
                scheduleCompletionNotification.getScheduleId(),
                scheduleCompletionNotification.getScheduleName(),
                scheduleCompletionNotification.getMemberNames()
        );
    }

    @Override
    public NotificationType getNotificationSupportType() {
        return NotificationType.SCHEDULE_COMPLETION;
    }
}
