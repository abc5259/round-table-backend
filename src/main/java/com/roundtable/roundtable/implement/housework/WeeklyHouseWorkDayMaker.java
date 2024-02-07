package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.Day;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWork;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWorkDay;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWorkDayRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class WeeklyHouseWorkDayMaker {

    private final DayReader dayReader;
    private final WeeklyHouseWorkDayRepository weeklyHouseWorkDayRepository;

    public void createWeeklyHouseWorkDays(List<Long> dayOfWeeks, WeeklyHouseWork weeklyHouseWork) {
        List<Day> days = dayReader.findAllById(dayOfWeeks);

        List<WeeklyHouseWorkDay> weeklyHouseWorkDays = days.stream().map(
                day -> new WeeklyHouseWorkDay(day, weeklyHouseWork)
        ).toList();

        weeklyHouseWorkDayRepository.saveAll(weeklyHouseWorkDays);
    }
}
