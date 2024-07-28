package com.roundtable.roundtable.business.notification.unit;

import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.business.notification.FeedbackNotificationAppender;
import com.roundtable.roundtable.business.notification.NotificationFactory;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedbackNotificationAppenderUnitTest {

    @InjectMocks
    private FeedbackNotificationAppender feedbackNotificationAppender;

    @Mock
    private NotificationFactory notificationFactory;

    @Mock
    private NotificationRepository notificationRepository;

    @DisplayName("집안일을 완료한 맴버들에게 피드백 알림을 만든다.")
    @Test
    void append() {
        //given
        Long feedbackId = 1L;
        Long houseId = 2L;
        Long choreId = 3L;
        Long senderId = 4L;

        Member sender = Member.builder().id(1L).name("sender").build();
        List<Member> receivers = List.of(
                Member.builder().id(2L).name("sender").build(),
                Member.builder().id(3L).name("sender").build()
        );

        List<FeedbackNotification> feedbackNotifications = List.of(
                FeedbackNotification.builder().sender(sender).receiver(receivers.get(0)).feedbackId(feedbackId).build(),
                FeedbackNotification.builder().sender(sender).receiver(receivers.get(1)).feedbackId(feedbackId).build()
        );
        given(notificationFactory.createFeedbackNotifications(feedbackId, houseId, choreId, senderId)).willReturn(
                feedbackNotifications
        );

        //when
        feedbackNotificationAppender.append(feedbackId, houseId, choreId, senderId);

        //then
        verify(notificationFactory).createFeedbackNotifications(feedbackId, houseId, choreId, senderId);
        verify(notificationRepository, times(1)).saveAll(feedbackNotifications);
    }

}