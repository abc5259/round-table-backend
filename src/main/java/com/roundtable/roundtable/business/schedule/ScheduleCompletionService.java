package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.dto.ScheduleCompletionEvent;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.ScheduleException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleCompletionService {

    private final ScheduleDayRepository scheduleDayRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleCompletionRepository scheduleCompletionRepository;
    private final ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void complete(Long scheduleId, AuthMember member, LocalDate completionDate) {
        validateCompletionSchedule(scheduleId, completionDate);
        ScheduleMember manager = scheduleMemberRepository.findByScheduleIdAndMemberId(scheduleId, member.memberId())
                .orElseThrow(() -> new NotFoundEntityException(ScheduleErrorCode.NOT_ASSIGNED_MANAGER));

        manager.complete();
        ScheduleCompletion scheduleCompletion = appendScheduleCompletion(scheduleId, completionDate);

        List<ScheduleMember> managers = scheduleMemberRepository.findByScheduleIdAndSequence(scheduleId, manager.getSequence());
        appendScheduleCompletionMember(managers, scheduleCompletion);
        applicationEventPublisher.publishEvent(new ScheduleCompletionEvent(scheduleId, managers.stream().map(ScheduleMember::getId).toList()));
    }

    private void validateCompletionSchedule(Long scheduleId, LocalDate completionDate) {
        if(!scheduleDayRepository.existsByScheduleIdAndDayOfWeek(scheduleId, Day.forDayOfWeek(completionDate.getDayOfWeek()))) {
            throw new ScheduleException(ScheduleErrorCode.NOT_TODAY_SCHEDULE);
        }
        if(scheduleCompletionRepository.existsByScheduleIdAndCompletionDate(scheduleId, completionDate)) {
            throw new ScheduleException(ScheduleErrorCode.ALREADY_COMPLETION_SCHEDULE);
        }
    }

    private ScheduleCompletion appendScheduleCompletion(Long scheduleId, LocalDate completionDate) {
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.create(Schedule.Id(scheduleId), completionDate);
        scheduleCompletionRepository.save(scheduleCompletion);
        return scheduleCompletion;
    }

    private void appendScheduleCompletionMember(List<ScheduleMember> managers, ScheduleCompletion scheduleCompletion) {
        List<ScheduleCompletionMember> scheduleCompletionMembers = managers.stream()
                .map(scheduleMember -> scheduleMember.toScheduleCompletionMember(scheduleCompletion))
                .toList();
        scheduleCompletionMemberRepository.saveAll(scheduleCompletionMembers);
    }
}
