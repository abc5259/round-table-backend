package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.DelegationNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.domain.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class DelegationNotificationAppender {

    private final ScheduleReader scheduleReader;
    private final NotificationRepository notificationRepository;

    public void append(Long houseId, Member sender, Member receiver, Long scheduleId, Delegation delegation) {
        Schedule schedule = scheduleReader.findById(scheduleId);
        DelegationNotification delegationNotification = DelegationNotification.create(House.Id(houseId), sender, receiver, delegation, schedule.getName());
        notificationRepository.save(delegationNotification);
    }
}
