package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

//    public NotiRea

    public List<NotificationResponse> findNotificationsByMemberId(AuthMember authMember) {

        return List.of();
    }

}
