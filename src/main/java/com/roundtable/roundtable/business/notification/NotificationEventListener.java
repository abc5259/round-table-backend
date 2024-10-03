package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.event.ChoreCompleteEvent;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
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
import org.springframework.transaction.annotation.Transactional;
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
    private final DelegationNotificationAppender delegationNotificationAppender;

    @Transactional
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
            log.error("[createInviteNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[createInviteNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
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
            log.error("[createChoreCompleteNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[createChoreCompleteNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
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
            log.error("[createScheduleCompletionNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[createScheduleCompletionNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createFeedbackNotification(CreateFeedbackEvent createFeedbackEvent) {
        try {
            feedbackNotificationAppender.append(
                    createFeedbackEvent.feedbackId(),
                    createFeedbackEvent.houseId(),
                    createFeedbackEvent.scheduleCompletionId(),
                    createFeedbackEvent.senderId()
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createFeedbackNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[createFeedbackNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createDelegationNotification(CreateDelegationEvent createDelegationEvent) {
        try {
            delegationNotificationAppender.append(
                    createDelegationEvent.houseId(),
                    createDelegationEvent.sender(),
                    createDelegationEvent.receiver(),
                    createDelegationEvent.scheduleId(),
                    createDelegationEvent.delegation()
            );
        }catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createDelegationNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        }catch (RuntimeException e) {
            log.error("[createDelegationNotification 에러] - {}", e.getMessage(), e);
        }
    }
}
