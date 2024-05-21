package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleDayReader {

    private final ScheduleDayRepository scheduleDayRepository;

    public List<Schedule> readScheduleByDate(LocalDate date) {
        Day day = Day.forDayOfWeek(date.getDayOfWeek());
        return scheduleDayRepository.findSchedulesByDay(day);
    }
}
