package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.response.DelegationNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.DelegationNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;

public class DelegationNotificationAdapter implements NotificationResponseAdapter {

    @Override
    public boolean isSupport(Notification notification) {
        return notification instanceof DelegationNotification;
    }

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        DelegationNotification delegationNotification = (DelegationNotification) notification;

        return new DelegationNotificationResponse(
                delegationNotification.getId(),
                NotificationType.DELEGATION,
                delegationNotification.getCreatedAt(),
                delegationNotification.getDelegationId(),
                delegationNotification.getStatus(),
                delegationNotification.getScheduleName()
        );
    }
}
