package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.category.CategoryReader;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.CreateScheduleDto;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.business.chore.ScheduleChoreAppendDirector;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final CategoryReader categoryReader;
    private final ScheduleChoreAppendDirector scheduleChoreAppendDirector;

    public Long createSchedule(CreateScheduleDto createScheduleDto, AuthMember authMember, LocalDate now) {
        House house = House.Id(authMember.houseId());

        Category category = categoryReader.findCategory(createScheduleDto.categoryId(), house);

        Schedule schedule = scheduleChoreAppendDirector.append(
                createScheduleDto.toCreateSchedule(category),
                house,
                now);

        return schedule.getId();
    }
}
