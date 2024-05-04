package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.house.HouseReader;
import com.roundtable.roundtable.business.house.HouseValidator;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.InviteNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class NotificationAppender {

    private final NotificationRepository notificationRepository;

    private final MemberReader memberReader;

    private final HouseReader houseReader;

    public List<Long> append(CreateInviteNotification createInviteNotification) {

        House house = houseReader.findById(createInviteNotification.invitedHouseId());

        // senderId가 실제로 있는지 확인
        if(memberReader.nonExistMemberId(createInviteNotification.senderId())) {
            throw new NotFoundEntityException(MemberErrorCode.NOT_FOUND);
        };

        // 받는 이메일이 있는 멤버중 하우스가 없는 멤버만 걸러냄
        List<Member> receivers = memberReader.findByEmail(createInviteNotification.receiverEmails()).stream()
                .filter(member -> !member.isEnterHouse()).toList();

        // 알림 만듦
        List<InviteNotification> inviteNotifications = receivers.stream()
                .map(receiver -> InviteNotification.create(
                        Member.Id(createInviteNotification.senderId()),
                        receiver,
                        createInviteNotification.invitedHouseId(),
                        house.getName())
                )
                .toList();

        List<InviteNotification> savedInviteNotifications = notificationRepository.saveAll(inviteNotifications);

        return savedInviteNotifications.stream().map(Notification::getId).toList();
    }
}
