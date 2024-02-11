package com.roundtable.roundtable.implement.schedule;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import com.roundtable.roundtable.entity.schedule.ScheduleRepository;
import com.roundtable.roundtable.implement.member.MemberReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduleMaker {
    private final MemberReader memberReader;
    private final ScheduleMemberMaker scheduleMemberMaker;
    private final ScheduleRepository scheduleRepository;

    public Schedule createSchedule(CreateSchedule createSchedule, House house) {
        List<Member> members = memberReader.findAllById(createSchedule.memberIds());

        List<ScheduleMember> scheduleMembers = scheduleMemberMaker.createScheduleMembers(members,
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
}
