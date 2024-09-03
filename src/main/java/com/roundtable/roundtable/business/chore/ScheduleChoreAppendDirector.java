package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.business.chore.dto.CreateChore;
import com.roundtable.roundtable.business.schedule.dto.CreateSchedule;
import com.roundtable.roundtable.business.schedule.ScheduleAppender;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
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
        Schedule schedule = null;

        switch (createSchedule.scheduleType()) {
            case REPEAT -> schedule = createRepeatSchedule(createSchedule, house, now);
            case ONE_TIME -> schedule = createOneTimeSchedule(createSchedule, house, now);
        }

        return schedule;
    }

    private Schedule createRepeatSchedule(CreateSchedule createSchedule, House house, LocalDate now) {
        Schedule schedule = scheduleAppender.appendSchedule(createSchedule, house, now);

        if(isStartToday(createSchedule, now)) {
            List<Member> members = choreMembersChooser.chooseChoreMembers(schedule.getScheduleMembers());
            choreAppender.appendChore(new CreateChore(now, schedule, members), house);
        }

        return schedule;
    }

    private Schedule createOneTimeSchedule(CreateSchedule createSchedule, House house, LocalDate now) {
        Schedule schedule = scheduleAppender.appendSchedule(createSchedule, house, now);

        List<Member> members = choreMembersChooser.chooseChoreMembers(schedule.getScheduleMembers());
        choreAppender.appendChore(new CreateChore(schedule.getStartDate(), schedule, members), house);

        return schedule;
    }

    private boolean isStartToday(CreateSchedule createSchedule, LocalDate now) {
        return createSchedule.startDate().equals(now) && isStartDayIncludedInDayIds(createSchedule.dayIds(), createSchedule.startDate());
    }

    private boolean isStartDayIncludedInDayIds(List<Integer> dayIds, LocalDate startDate) {
        Day targetDay = Day.forDayOfWeek(startDate.getDayOfWeek());
        return dayIds.stream()
                .map(Day::fromId)
                .anyMatch(day -> day == targetDay);
    }

}
