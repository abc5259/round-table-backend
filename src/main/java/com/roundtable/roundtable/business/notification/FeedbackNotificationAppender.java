package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class FeedbackNotificationAppender {

    private final NotificationFactory notificationFactory;
    private final NotificationRepository notificationRepository;

    public void append(Long feedbackId, Long houseId, Long scheduleCompletionId, Long senderId) {
        List<FeedbackNotification> feedbackNotifications =
                notificationFactory.createFeedbackNotifications(
                    feedbackId,
                    houseId,
                    scheduleCompletionId,
                    senderId
                );
        notificationRepository.saveAll(feedbackNotifications);

    }
}
