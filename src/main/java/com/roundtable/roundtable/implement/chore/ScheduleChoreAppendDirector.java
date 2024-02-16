package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import com.roundtable.roundtable.implement.schedule.ScheduleAppender;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ScheduleChoreAppendDirector {

    private final ScheduleAppender scheduleAppender;

    private final ChoreAppender choreAppender;

    private final ChoreMembersChooser choreMembersChooser;

    public Schedule append(CreateSchedule createSchedule, House house, LocalDate now) {
        Schedule schedule = scheduleAppender.appendSchedule(createSchedule, house, now);

        if(isStartToday(createSchedule, now)) {
            List<Long> choreMemberIds = choreMembersChooser.chooseChoreMemberIds(schedule);

            choreAppender.appendChore(
                    new CreateChore(schedule, choreMemberIds),
                    house);
        }

        return schedule;
    }

    private boolean isStartToday(CreateSchedule createSchedule, LocalDate now) {
        return createSchedule.startDate().equals(now);
    }
}
