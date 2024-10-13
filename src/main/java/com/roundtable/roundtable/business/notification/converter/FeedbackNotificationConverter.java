package com.roundtable.roundtable.business.notification.converter;

import com.roundtable.roundtable.business.notification.dto.response.FeedbackNotificationResponse;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class FeedbackNotificationConverter implements NotificationResponseConverter {

    @Override
    public NotificationResponse toNotificationResponse(Notification notification) {
        FeedbackNotification feedbackNotification = (FeedbackNotification) notification;

        return new FeedbackNotificationResponse(
                feedbackNotification.getId(),
                NotificationType.FEEDBACK,
                feedbackNotification.getCreatedAt(),
                feedbackNotification.getSender().getId(),
                feedbackNotification.getSender().getName(),
                feedbackNotification.getFeedbackId(),
                feedbackNotification.getScheduleName()
        );
    }

    @Override
    public NotificationType getNotificationSupportType() {
        return NotificationType.FEEDBACK;
    }
}
