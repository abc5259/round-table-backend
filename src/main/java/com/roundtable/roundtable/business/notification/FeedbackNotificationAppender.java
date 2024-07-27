package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class FeedbackNotificationAppender {

    private final MemberReader memberReader;
    private final ChoreReader choreReader;
    private final ChoreMemberReader choreMemberReader;
    private final NotificationRepository notificationRepository;

    public void append(Long feedbackId, Long houseId, Long choreId, Long senderId) {

        Member sender = memberReader.findById(senderId);
        Chore chore = choreReader.readById(choreId);
        List<Member> receivers = choreMemberReader.readMembersByChoreId(choreId);

        List<FeedbackNotification> choreCompleteNotifications = receivers.stream()
                .map(receiver ->
                        FeedbackNotification.create(
                                sender,
                                receiver,
                                House.Id(houseId),
                                feedbackId,
                                chore.getName()
                        )).toList();

        notificationRepository.saveAll(choreCompleteNotifications);
    }
}
