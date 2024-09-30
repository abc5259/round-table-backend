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

    private final HouseReader houseReader;

    private final MemberReader memberReader;

    private final ChoreReader choreReader;

    private final ChoreMemberReader choreMemberReader;

    public List<InviteNotification> createInviteNotifications(CreateInviteNotification createInviteNotification) {
        House house = houseReader.findById(createInviteNotification.invitedHouseId());

        // senderId가 실제로 있는지 확인
        if(memberReader.nonExistMemberId(createInviteNotification.senderId())) {
            throw new NotFoundEntityException(MemberErrorCode.NOT_FOUND);
        };

        // 받는 이메일이 있는 멤버중 하우스가 없는 멤버만 걸러냄
        List<Member> receivers = memberReader.findByEmail(createInviteNotification.receiverEmails()).stream()
                .filter(member -> !member.isEnterHouse())
                .toList();

        return receivers.stream()
                .map(receiver -> InviteNotification.create(
                        Member.Id(createInviteNotification.senderId()),
                        receiver,
                        createInviteNotification.invitedHouseId(),
                        house.getName())
                )
                .toList();
    }

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
