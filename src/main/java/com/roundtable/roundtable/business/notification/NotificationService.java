package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.notification.converter.NotificationResponseConvertMapper;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.domain.notification.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationReader notificationReader;
    private final NotificationResponseConvertMapper notificationResponseConvertMapper;


    public CursorBasedResponse<List<NotificationResponse>> findNotificationsByMemberId(CursorBasedRequest cursorBasedRequest, AuthMember authMember) {
        List<Notification> notifications = notificationReader.readNotificationsByReceiverId(authMember.memberId(),
                authMember.houseId(),
                cursorBasedRequest);

        Long lastCursorId = notifications.isEmpty() ? 0 : notifications.get(notifications.size() - 1).getId();

        return CursorBasedResponse.of(notifications.stream().map(this::toNotificationResponse).toList(), lastCursorId);
    }

    private NotificationResponse toNotificationResponse(Notification notification) {
        return notificationResponseConvertMapper.convert(notification);
    }


}
