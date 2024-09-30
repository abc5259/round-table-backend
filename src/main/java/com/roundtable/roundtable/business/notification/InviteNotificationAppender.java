package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class InviteNotificationAppender {

    private final NotificationFactory notificationFactory;

    private final NotificationRepository notificationRepository;

    public List<Long> append(CreateInviteNotification createInviteNotification) {

        List<InviteNotification> inviteNotifications = notificationFactory.createInviteNotifications(createInviteNotification);
        List<InviteNotification> savedInviteNotifications = notificationRepository.saveAll(inviteNotifications);
        return savedInviteNotifications.stream().map(Notification::getId).toList();
    }
}
