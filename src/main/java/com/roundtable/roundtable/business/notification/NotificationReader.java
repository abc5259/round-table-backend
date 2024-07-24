package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationReader {

    private final NotificationRepository notificationRepository;

//    public List<Notification> readNotificationsByReceiverId(Long receiverId, CursorBasedRequest cursorBasedRequest) {
//        return notificationRepository.findByReceiverId(receiverId, cursorBasedRequest.lastId(), PageRequest.of(0, cursorBasedRequest.limit()));
//    }
}
