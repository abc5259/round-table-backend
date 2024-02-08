package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.Day;
import com.roundtable.roundtable.entity.housework.HouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkDay;
import com.roundtable.roundtable.entity.housework.HouseWorkDayRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkDayMaker {

    private final DayReader dayReader;
    private final HouseWorkDayRepository houseWorkDayRepository;

    public void createHouseWorkDays(List<Long> dayOfWeeks, HouseWork houseWork) {
        List<Day> days = dayReader.findAllById(dayOfWeeks);

        List<HouseWorkDay> houseWorkDays = days.stream().map(
                day -> new HouseWorkDay(day, houseWork)
        ).toList();

        houseWorkDayRepository.saveAll(houseWorkDays);
    }

    public void createHouseWorkDay(DayOfWeek dayOfWeek, HouseWork houseWork) {
        Day day = dayReader.findByDayOfWeek(dayOfWeek);
        HouseWorkDay houseWorkDay = new HouseWorkDay(day, houseWork);
        houseWorkDayRepository.save(houseWorkDay);
    }
}
