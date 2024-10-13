package com.roundtable.roundtable.business.notification.converter;


import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;

public interface NotificationResponseConverter {
    NotificationResponse toNotificationResponse(Notification notification);

    NotificationType getNotificationSupportType();
}
