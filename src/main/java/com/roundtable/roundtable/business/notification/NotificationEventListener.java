package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.house.dto.event.HouseCreatedEvent;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationAppender notificationAppender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createNotification(HouseCreatedEvent houseCreatedEvent) {
        try {
            notificationAppender.append(
                    new CreateInviteNotification(
                            houseCreatedEvent.houseId(),
                            houseCreatedEvent.houseId(),
                            houseCreatedEvent.invitedEmails()
                    )
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.warn("[HouseCreatedEvent 에러] - " + errorCode.getMessage() + " " + errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.warn("[HouseCreatedEvent 에러] - " + e.getMessage(), e);
        }
    }
}
