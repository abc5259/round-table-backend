package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.event.ChoreCompleteEvent;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.business.house.event.HouseCreatedEvent;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.business.schedule.dto.ScheduleCompletionEvent;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final InviteNotificationAppender inviteNotificationAppender;
    private final ChoreCompleteNotificationAppender choreCompleteNotificationAppender;
    private final FeedbackNotificationAppender feedbackNotificationAppender;
    private final ScheduleCompletionNotificationAppender scheduleCompletionNotificationAppender;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createInviteNotification(HouseCreatedEvent houseCreatedEvent) {
        try {
            inviteNotificationAppender.append(
                    new CreateInviteNotification(
                            houseCreatedEvent.appenderId(),
                            houseCreatedEvent.houseId(),
                            houseCreatedEvent.invitedEmails()
                    )
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[HouseCreatedEvent 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[HouseCreatedEvent 에러] - {}", e.getMessage(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createChoreCompleteNotification(ChoreCompleteEvent choreCompleteEvent) {
        try {
            choreCompleteNotificationAppender.append(
                    choreCompleteEvent.houseId(),
                    choreCompleteEvent.completedChoreId(),
                    choreCompleteEvent.completedMemberId()
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[ChoreCompleteEvent 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[ChoreCompleteEvent 에러] - {}", e.getMessage(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createScheduleCompletionNotification(ScheduleCompletionEvent scheduleCompletionEvent) {
        try {
            scheduleCompletionNotificationAppender.append(
                    scheduleCompletionEvent.houseId(),
                    scheduleCompletionEvent.scheduleId(),
                    scheduleCompletionEvent.managerIds()
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[ChoreCompleteEvent 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[ChoreCompleteEvent 에러] - {}", e.getMessage(), e);
        }
    }

    /**
     * 고민.. 이 메서드는 무조건 트랜잭션 끝난 후 실행되야함
     */
    @EventListener
    @Async
    public void createFeedbackNotification(CreateFeedbackEvent createFeedbackEvent) {
        try {
            feedbackNotificationAppender.append(
                    createFeedbackEvent.feedbackId(),
                    createFeedbackEvent.houseId(),
                    createFeedbackEvent.choreId(),
                    createFeedbackEvent.senderId()
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.warn("[ChoreCompleteEvent 에러] - " + errorCode.getMessage() + " " + errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.warn("[ChoreCompleteEvent 에러] - " + e.getMessage(), e);
        }
    }
}
