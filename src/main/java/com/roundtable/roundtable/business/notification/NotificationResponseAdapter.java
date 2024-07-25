package com.roundtable.roundtable.business.notification;


import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;

public interface NotificationResponseAdapter {

    boolean isSupport(Notification notification);

    NotificationResponse toNotificationResponse(Notification notification);
}
