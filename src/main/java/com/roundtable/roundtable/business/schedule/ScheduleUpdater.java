package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class ScheduleUpdater {

    public void updateSequence(List<Schedule> schedules) {
        schedules.forEach(Schedule::increaseSequence);
    }
}
