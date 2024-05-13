package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.dto.CreateScheduleDto;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.business.chore.ScheduleChoreAppendDirector;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleChoreAppendDirector scheduleChoreAppendDirector;

    public Long createSchedule(CreateScheduleDto createScheduleDto, AuthMember authMember, LocalDate now) {
        House house = House.Id(authMember.houseId());

        Schedule schedule = scheduleChoreAppendDirector.append(
                createScheduleDto.toCreateSchedule(),
                house,
                now);

        return schedule.getId();
    }
}
