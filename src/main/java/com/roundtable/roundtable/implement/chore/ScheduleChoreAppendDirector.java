package com.roundtable.roundtable.implement.chore;

import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import com.roundtable.roundtable.implement.schedule.ScheduleAppender;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduleChoreAppendDirector {

    private final ScheduleAppender scheduleAppender;

    private final ChoreAppender choreAppender;

    public Schedule append(CreateSchedule createSchedule, House house, LocalDate now) {
        Schedule schedule = scheduleAppender.createSchedule(createSchedule, house);

        if(isStartToday(createSchedule, now)) {
            choreAppender.appendChore(
                    new CreateChore(schedule, createSchedule.memberIds()),
                    house);
        }

        return schedule;
    }

    private boolean isStartToday(CreateSchedule createSchedule, LocalDate now) {
        return createSchedule.startDate().equals(now);
    }
}
