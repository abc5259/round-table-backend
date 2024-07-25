package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;

public class ChoreCompletedNotificationResponseAdapter implements NotificationResponseAdapter {
    @Override
    public boolean isSupport(Notification notification) {
        return notification instanceof ChoreCompleteNotification;
    }

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        ChoreCompleteNotification choreCompleteNotification = (ChoreCompleteNotification) notification;

        return new NotificationResponse(
                choreCompleteNotification.getId(),
                NotificationType.CHORE_COMPLETE,
                choreCompleteNotification.getCreatedAt(),
                null,
                null,
                choreCompleteNotification.getChoreId(),
                choreCompleteNotification.getChoreName(),
                choreCompleteNotification.getMemberNames()
        );
    }
}
