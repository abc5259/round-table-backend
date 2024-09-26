package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.domain.notification.ScheduleCompletionNotification;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduleCompletionNotificationAppender {
    private final MemberReader memberReader;
    private final ScheduleRepository scheduleRepository;
    private final NotificationRepository notificationRepository;

    public void append(Long houseId, Long scheduleId, List<Long> managerIds) {
        Schedule schedule = getSchedule(scheduleId);
        List<Member> houseMembers = memberReader.findAllByHouseId(houseId);
        List<Member> managers = getManagers(managerIds, houseMembers);
        List<Member> receivers = getReceivers(managerIds, houseMembers);

        List<ScheduleCompletionNotification> notifications = createScheduleCompletionNotifications(houseId, scheduleId, receivers, schedule,
                managers);
        notificationRepository.saveAll(notifications);
    }

    private List<Member> getManagers(List<Long> managerIds, List<Member> houseMembers) {
        return houseMembers.stream()
                .filter(houseMember -> managerIds.contains(houseMember.getId()))
                .toList();
    }

    private List<Member> getReceivers(List<Long> managerIds, List<Member> houseMembers) {
        return houseMembers.stream()
                .filter(houseMember -> !managerIds.contains(houseMember.getId()))
                .toList();
    }

    private List<ScheduleCompletionNotification> createScheduleCompletionNotifications(Long houseId, Long scheduleId, List<Member> receivers, Schedule schedule, List<Member> managers) {
        return receivers.stream()
                .map(receiver -> ScheduleCompletionNotification.create(receiver, House.Id(houseId), scheduleId,
                        schedule.getName(), managers))
                .toList();
    }

    private Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ScheduleErrorCode.NOT_FOUND_ID));
    }


}
