package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.Schedule;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@RequiredArgsConstructor
public class ScheduleUpdater {

    private final EntityManager em;

    public void updateSequence(List<Schedule> schedules) {
        schedules.forEach(schedule -> em.merge(schedule).increaseSequence());
    }
}
