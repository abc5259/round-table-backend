package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public NotificationReader notificationReader;

    public List<NotificationResponse> findNotificationsByMemberId(CursorBasedRequest cursorBasedRequest, AuthMember authMember) {
        List<Notification> notifications = notificationReader.readNotificationsByReceiverId(authMember.memberId(),
                cursorBasedRequest);

        List<NotificationResponse> response = notifications.stream().map(notification -> {
            if (notification instanceof InviteNotification inviteNotification) {
                return new NotificationResponse(
                        inviteNotification.getId(),
                        "INVITE",
                        inviteNotification.getCreatedAt(),
                        inviteNotification.getInvitedHouseId(),
                        inviteNotification.getInvitedHouseName(),
                        null,
                        null,
                        null
                );
            }

            if (notification instanceof ChoreCompleteNotification choreCompleteNotification) {
                return new NotificationResponse(
                        choreCompleteNotification.getId(),
                        "INVITE",
                        choreCompleteNotification.getCreatedAt(),
                        null,
                        null,
                        choreCompleteNotification.getChoreId(),
                        choreCompleteNotification.getChoreName(),
                        choreCompleteNotification.getMemberNames()
                );
            }

            return null;
        }).toList();

        return response;
    }

}
