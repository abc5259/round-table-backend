package com.roundtable.roundtable.business.notification.unit;

import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.FeedbackNotificationAppender;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedbackNotificationAppenderUnitTest {

    @Mock
    private FeedbackNotificationAppender feedbackNotificationAppender;

    @InjectMocks
    private MemberReader memberReader;

    @InjectMocks
    private ChoreReader choreReader;

    @InjectMocks
    private ChoreMemberReader choreMemberReader;

    @InjectMocks
    private NotificationRepository notificationRepository;

    @DisplayName("집안일을 완료한 맴버들에게 피드백 알림을 만든다.")
    @Test
    void append() {
        //given
        Long feedbackId = 1L;
        Long houseId = 1L;
        Long choreId = 1L;
        Long senderId = 1L;

        Member sender = Member.builder().id(1L).name("sender").build();
        Chore chore = Chore.builder().id(1L).build();
        List<Member> receivers = List.of(
                Member.builder().id(2L).name("sender").build(),
                Member.builder().id(3L).name("sender").build(),
                Member.builder().id(4L).name("sender").build()
        );

        given(memberReader.findById(feedbackId)).willReturn(sender);
        given(choreReader.readById(choreId)).willReturn(chore);
        given(choreMemberReader.readMembersByChoreId(choreId)).willReturn(receivers);

        //when


        //then
//        verify(notificationRepository, times(1)).saveAll();

    }

}