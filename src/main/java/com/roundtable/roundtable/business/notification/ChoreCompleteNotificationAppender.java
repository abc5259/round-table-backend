package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class ChoreCompleteNotificationAppender {

    private final MemberReader memberReader;
    private final ChoreReader choreReader;
    private final ChoreMemberReader choreMemberReader;
    private final NotificationRepository notificationRepository;

    public void append(Long houseId, Long completedChoreId, Long completedMemberId) {

        Member sender = memberReader.findById(completedMemberId);
        Chore completedChore = choreReader.readById(completedChoreId);
        List<Member> houseMembers = memberReader.findAllByHouseId(houseId);
        List<Member> choreMembers = choreMemberReader.readMembersByChoreId(completedChoreId);

        List<Member> receivers = getReceivers(houseMembers, choreMembers);

        List<String> memberNames = choreMembers.stream().map(Member::getName).toList();

        List<ChoreCompleteNotification> choreCompleteNotifications = receivers.stream()
                .map(receiver ->
                        ChoreCompleteNotification.create(
                                sender,
                                receiver,
                                completedChoreId,
                                completedChore.getName(),
                                String.join(",", memberNames)
        )).toList();

        notificationRepository.saveAll(choreCompleteNotifications);
    }

    private List<Member> getReceivers(List<Member> houseMembers, List<Member> choreMembers) {
        return houseMembers.stream().filter(member -> !choreMembers.contains(member)).toList();
    }
}
