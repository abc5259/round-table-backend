package com.roundtable.roundtable.business.notification.converter;

import com.roundtable.roundtable.business.notification.dto.response.InviteNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class InviteNotificationResponseConverter implements NotificationResponseConverter {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        InviteNotification inviteNotification = (InviteNotification) notification;
        return new InviteNotificationResponse(
                inviteNotification.getId(),
                NotificationType.INVITE,
                inviteNotification.getCreatedAt(),
                inviteNotification.getSender().getId(),
                inviteNotification.getSender().getName(),
                inviteNotification.getInvitedHouseId(),
                inviteNotification.getInvitedHouseName()
        );
    }

    @Override
    public NotificationType getNotificationSupportType() {
        return NotificationType.INVITE;
    }
}
