package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.housework.Day;
import com.roundtable.roundtable.entity.housework.HouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseWorkReader {
    private final HouseWorkRepository houseWorkRepository;
    private final DayReader dayReader;
    public List<HouseWork> findHouseWorksByDate(LocalDate targetDate, House house) {
        Day day = dayReader.findByDayOfWeek(targetDate.getDayOfWeek());

        return houseWorkRepository.findHouseWorksByDayAndHouse(day, house, targetDate);
    }
}
