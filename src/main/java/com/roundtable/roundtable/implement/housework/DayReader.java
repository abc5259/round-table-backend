package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.Day;
import com.roundtable.roundtable.entity.housework.DayRepository;
import com.roundtable.roundtable.implement.housework.DayException.DayNotFindException;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DayReader {
    private final DayRepository dayRepository;

    public List<Day> findAllById(List<DayOfWeek> dayOfWeeks) {
        List<Day> days = dayRepository.findAllById(dayOfWeeks);

        if(dayOfWeeks.size() != days.size()) {
            throw new DayNotFindException();
        }

        return days;
    }
}
