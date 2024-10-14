package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.house.HouseReader;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.business.sse.HouseSsePublisher;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class InviteNotificationAppender {

    private final HouseReader houseReader;
    private final MemberReader memberReader;
    private final NotificationRepository notificationRepository;
    private final HouseSsePublisher houseSsePublisher;

    public void append(CreateInviteNotification createInviteNotification) {
        House house = houseReader.findById(createInviteNotification.invitedHouseId());
        validateExistSender(createInviteNotification.senderId());
        List<Member> receivers = getReceivers(createInviteNotification.receiverEmails());
        List<InviteNotification> inviteNotifications = createInviteNotifications(createInviteNotification, receivers, house);
        notificationRepository.saveAll(inviteNotifications);

        if(!inviteNotifications.isEmpty()) {
            houseSsePublisher.sendToNonHouseMembers(receivers.stream().map(Member::getId).toList(), inviteNotifications.get(0).toSseEvent());
        }
    }

    private void validateExistSender(Long senderId) {
        if(memberReader.nonExistMemberId(senderId)) {
            throw new NotFoundEntityException(MemberErrorCode.NOT_FOUND);
        }
    }

    private List<Member> getReceivers(List<String> receiverEmails) {
        return memberReader.findByEmail(receiverEmails).stream()
                .filter(member -> !member.isEnterHouse())
                .toList();
    }

    private List<InviteNotification> createInviteNotifications(
            CreateInviteNotification createInviteNotification,
            List<Member> receivers,
            House house
    ) {
        return receivers.stream()
                .map(receiver -> InviteNotification.create(
                        Member.Id(createInviteNotification.senderId()),
                        receiver,
                        createInviteNotification.invitedHouseId(),
                        house.getName())
                )
                .toList();
    }
}
