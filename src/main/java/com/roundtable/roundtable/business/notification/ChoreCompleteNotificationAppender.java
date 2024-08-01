package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class ChoreCompleteNotificationAppender {

    private final NotificationFactory notificationFactory;

    private final NotificationRepository notificationRepository;

    public void append(Long houseId, Long completedChoreId, Long completedMemberId) {
        List<ChoreCompleteNotification> choreCompleteNotifications = notificationFactory.createChoreCompleteNotifications(houseId, completedChoreId, completedMemberId);

        notificationRepository.saveAll(choreCompleteNotifications);
    }
}
