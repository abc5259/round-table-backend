package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.response.FeedbackNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;

public class FeedbackNotificationAdaptor implements NotificationResponseAdapter {

    @Override
    public boolean isSupport(Notification notification) {
        return notification instanceof FeedbackNotification;
    }

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        FeedbackNotification feedbackNotification = (FeedbackNotification) notification;

        return new FeedbackNotificationResponse(
                feedbackNotification.getId(),
                NotificationType.FEEDBACK,
                feedbackNotification.getCreatedAt(),
                feedbackNotification.getFeedbackId(),
                feedbackNotification.getScheduleName()
        );
    }
}
