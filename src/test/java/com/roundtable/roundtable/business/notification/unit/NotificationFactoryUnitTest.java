package com.roundtable.roundtable.business.notification.unit;

import static org.assertj.core.groups.Tuple.*;
import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.NotificationFactory;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.schedule.Schedule;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationFactoryUnitTest {

    @InjectMocks
    private NotificationFactory notificationFactory;

    @Mock
    private MemberReader memberReader;

    @Mock
    private ChoreReader choreReader;

    @Mock
    private ChoreMemberReader choreMemberReader;

    @DisplayName("FeedbackNotification을 생성한다.")
    @Test
    void createFeedbackNotifications() {
        //given
        Long feedbackId = 1L;
        Long houseId = 2L;
        Long choreId = 3L;
        Long senderId = 4L;

        Member sender = Member.builder().id(senderId).name("sender").build();
        Schedule schedule = Schedule.builder().name("schedule").build();
        Chore chore = Chore.builder().id(choreId).schedule(schedule).build();
        List<Member> receivers = List.of(
                Member.builder().id(2L).name("recever1").build(),
                Member.builder().id(3L).name("recever2").build()
        );

        given(memberReader.findById(senderId)).willReturn(sender);
        given(choreReader.readById(choreId)).willReturn(chore);
        given(choreMemberReader.readMembersByChoreId(choreId)).willReturn(receivers);

        //when
        List<FeedbackNotification> feedbackNotifications = notificationFactory.createFeedbackNotifications(feedbackId,
                houseId, choreId, senderId);

        //then
        Assertions.assertThat(feedbackNotifications).hasSize(receivers.size())
                .extracting("sender", "receiver", "feedbackId" , "choreName")
                .containsExactly(
                        tuple(sender, receivers.get(0), feedbackId, schedule.getName()),
                        tuple(sender, receivers.get(1), feedbackId, schedule.getName())
                );

    }

}