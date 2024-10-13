package com.roundtable.roundtable.business.notification.converter;

import com.roundtable.roundtable.business.notification.dto.response.DelegationNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.DelegationNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class DelegationNotificationConverter implements NotificationResponseConverter {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        DelegationNotification delegationNotification = (DelegationNotification) notification;

        return new DelegationNotificationResponse(
                delegationNotification.getId(),
                NotificationType.DELEGATION,
                delegationNotification.getCreatedAt(),
                delegationNotification.getSender().getId(),
                delegationNotification.getSender().getName(),
                delegationNotification.getDelegationId(),
                delegationNotification.getStatus(),
                delegationNotification.getScheduleName()
        );
    }

    @Override
    public NotificationType getNotificationSupportType() {
        return NotificationType.DELEGATION;
    }
}
