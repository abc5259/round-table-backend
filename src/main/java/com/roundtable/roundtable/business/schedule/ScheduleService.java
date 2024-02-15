package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.implement.chore.ScheduleChoreAppendDirector;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleChoreAppendDirector scheduleChoreAppendDirector;

    public Long createSchedule(CreateSchedule createSchedule, Member loginMember, LocalDate now) {
        Schedule schedule = scheduleChoreAppendDirector.append(createSchedule, loginMember.getHouse(), now);
        return schedule.getId();
    }
}
