package com.roundtable.roundtable.implement.schedule;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleException.CreateScheduleException;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import com.roundtable.roundtable.entity.schedule.ScheduleRepository;
import com.roundtable.roundtable.implement.member.MemberReader;
import com.roundtable.roundtable.implement.member.MemberValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduleMaker {
    private final MemberReader memberReader;
    private final MemberValidator memberValidator;
    private final ScheduleMemberFactory scheduleMemberFactory;
    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(CreateSchedule createSchedule, House house) {
        /**
         * TODO: 입력값에 대한 예외 처리는 controller의 역할일까??
         */
        validate(createSchedule);

        List<Member> members = memberReader.findAllById(createSchedule.memberIds());
        memberValidator.validateMembersSameHouse(members,house);

        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members,
                createSchedule.divisionType());

        Schedule schedule = Schedule.create(
                createSchedule.name(),
                Frequency.of(createSchedule.frequencyType(), createSchedule.frequencyInterval()),
                createSchedule.startDate(),
                createSchedule.startTime(),
                createSchedule.divisionType(),
                house,
                scheduleMembers
        );

        return scheduleRepository.save(schedule);
    }

    private void validate(CreateSchedule createSchedule) {

        checkDuplicateMemberId(createSchedule);

        checkBeforeDate(createSchedule);

        checkSupportFrequency(createSchedule);

        checkWeeklyInterval(createSchedule);
    }

    private static void checkDuplicateMemberId(CreateSchedule createSchedule) {
        if(createSchedule.memberIds().size() != createSchedule.memberIds().stream().distinct().count()) {
            throw new CreateScheduleException("중복된 member id값이 있습니다.");
        }
    }

    private static void checkBeforeDate(CreateSchedule createSchedule) {
        if(createSchedule.startDate().isBefore(LocalDate.now())) {
            throw new CreateScheduleException("시작날짜는 과거일 수 없습니다.");
        }
    }

    private static void checkSupportFrequency(CreateSchedule createSchedule) {
        if(!Frequency.isSupport(createSchedule.frequencyType(), createSchedule.frequencyInterval())) {
            throw new CreateScheduleException("frequencyType에 맞는 frequencyInterval값이 아닙니다.");
        }
    }

    private static void checkWeeklyInterval(CreateSchedule createSchedule) {
        if(createSchedule.frequencyType().equals(FrequencyType.WEEKLY)) {
            DayOfWeek day = DayOfWeek.of(createSchedule.frequencyInterval());
            if(!createSchedule.startDate().getDayOfWeek().equals(day)) {
                throw new CreateScheduleException("Weekly타입일땐 시작날짜는 interval로 준 값에 해당하는 요일이어야합니다.");
            }
        }
    }
}