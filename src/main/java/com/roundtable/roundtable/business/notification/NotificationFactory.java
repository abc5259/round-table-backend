package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFactory {

    private final MemberReader memberReader;
    private final ChoreReader choreReader;
    private final ChoreMemberReader choreMemberReader;

    public List<FeedbackNotification> createFeedbackNotifications(Long feedbackId, Long houseId, Long choreId, Long senderId) {
        Member sender = memberReader.findById(senderId);
        Chore chore = choreReader.readById(choreId);
        List<Member> receivers = choreMemberReader.readMembersByChoreId(choreId);

        return receivers.stream()
                .map(receiver ->
                        FeedbackNotification.create(
                                sender,
                                receiver,
                                House.Id(houseId),
                                feedbackId,
                                chore.getName()
                        )).toList();
    }
}
