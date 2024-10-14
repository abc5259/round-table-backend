package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.house.HouseReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationFactory {

    private final MemberReader memberReader;

    private final ChoreReader choreReader;

    private final ChoreMemberReader choreMemberReader;

    public List<ChoreCompleteNotification> createChoreCompleteNotifications(Long houseId, Long completedChoreId, Long completedMemberId) {
        Member sender = memberReader.findById(completedMemberId);
        Chore completedChore = choreReader.readById(completedChoreId);
        List<Member> houseMembers = memberReader.findAllByHouseId(houseId);
        List<Member> choreMembers = choreMemberReader.readMembersByChoreId(completedChoreId);

        List<Member> receivers = getReceivers(houseMembers, choreMembers);

        List<String> memberNames = choreMembers.stream().map(Member::getName).toList();

        return receivers.stream()
                .map(receiver ->
                        ChoreCompleteNotification.create(
                                sender,
                                receiver,
                                House.Id(houseId),
                                completedChoreId,
                                completedChore.getName(),
                                memberNames
                        )).toList();
    }

    private List<Member> getReceivers(List<Member> houseMembers, List<Member> choreMembers) {
        return houseMembers.stream().filter(member -> !choreMembers.contains(member)).toList();
    }
}
