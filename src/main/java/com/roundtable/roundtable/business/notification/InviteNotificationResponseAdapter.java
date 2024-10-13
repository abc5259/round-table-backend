package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.response.InviteNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;

public class InviteNotificationResponseAdapter implements NotificationResponseAdapter {
    @Override
    public boolean isSupport(Notification notification) {
        return notification instanceof InviteNotification;
    }

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
}
