package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.ScheduleCompletionNotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import com.roundtable.roundtable.domain.notification.ScheduleCompletionNotification;

public class ScheduleCompletionNotificationAdapter implements NotificationResponseAdapter {
    @Override
    public boolean isSupport(Notification notification) {
        return notification instanceof ScheduleCompletionNotification;
    }

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
}
