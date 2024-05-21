package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduleDayAppender {

    private final ScheduleDayRepository scheduleDayRepository;

    public void append(Schedule schedule, List<Integer> dayIds) {
        List<Day> days = dayIds.stream().map(Day::forId).toList();
        List<ScheduleDay> scheduleDays = days.stream().map(day -> ScheduleDay.create(schedule, day)).toList();
        scheduleDayRepository.saveAll(scheduleDays);
    }
}
