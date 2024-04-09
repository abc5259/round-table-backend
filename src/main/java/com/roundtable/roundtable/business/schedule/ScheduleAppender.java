package com.roundtable.roundtable.business.schedule;

import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.DUPLICATED_MEMBER_ID;
import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.FREQUENCY_NOT_SUPPORT;
import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.FREQUENCY_NOT_SUPPORT_WEEKLY;
import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.INVALID_START_DATE;

import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Frequency;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.business.member.MemberReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduleAppender {
    private final MemberReader memberReader;
    private final MemberValidator memberValidator;
    private final ScheduleMemberAppender scheduleMemberAppender;
    private final ScheduleRepository scheduleRepository;

    public Schedule appendSchedule(CreateSchedule createSchedule, House house, LocalDate currDate) {
        validateCreateSchedule(createSchedule, currDate);

        List<Member> members = memberReader.findAllById(createSchedule.memberIds());
        memberValidator.validateMembersSameHouse(members,house);

        Schedule schedule = appendSchedule(createSchedule, house, members);

        List<ScheduleMember> scheduleMembers = scheduleMemberAppender.createScheduleMembers(members, schedule);
        schedule.addScheduleMembers(scheduleMembers);

        return schedule;
    }


    private Schedule appendSchedule(CreateSchedule createSchedule, House house, List<Member> members) {

        //여기서 카테고리가 같은 하우스인지 체크 해야 할까? 아니면 도메인에게 맡길까?
        Schedule schedule = Schedule.create(
                createSchedule.name(),
                Frequency.of(createSchedule.frequencyType(), createSchedule.frequencyInterval()),
                createSchedule.startDate(),
                createSchedule.startTime(),
                createSchedule.divisionType(),
                house,
                members.size(),
                createSchedule.category()
        );

        return scheduleRepository.save(schedule);
    }

    private void validateCreateSchedule(CreateSchedule createSchedule, LocalDate currDate) {

        checkDuplicateMemberId(createSchedule);

        checkBeforeDate(createSchedule, currDate);

        checkSupportFrequency(createSchedule);

        checkWeeklyInterval(createSchedule);
    }

    private void checkDuplicateMemberId(CreateSchedule createSchedule) {
        if(createSchedule.memberIds().size() != createSchedule.memberIds().stream().distinct().count()) {
            throw new CreateEntityException(DUPLICATED_MEMBER_ID);
        }
    }

    private void checkBeforeDate(CreateSchedule createSchedule, LocalDate currDate) {
        if(createSchedule.startDate().isBefore(currDate)) {
            throw new CreateEntityException(INVALID_START_DATE);
        }
    }

    private void checkSupportFrequency(CreateSchedule createSchedule) {
        if(!Frequency.isSupport(createSchedule.frequencyType(), createSchedule.frequencyInterval())) {
            throw new CreateEntityException(FREQUENCY_NOT_SUPPORT);
        }
    }

    private void checkWeeklyInterval(CreateSchedule createSchedule) {
        if(createSchedule.frequencyType().equals(FrequencyType.WEEKLY)) {
            DayOfWeek day = DayOfWeek.of(createSchedule.frequencyInterval());
            if(!createSchedule.startDate().getDayOfWeek().equals(day)) {
                throw new CreateEntityException(FREQUENCY_NOT_SUPPORT_WEEKLY);
            }
        }
    }

}
