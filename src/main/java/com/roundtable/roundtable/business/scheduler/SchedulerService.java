package com.roundtable.roundtable.business.scheduler;

import com.roundtable.roundtable.business.chore.ChoreAppender;
import com.roundtable.roundtable.business.schedule.ScheduleDayReader;
import com.roundtable.roundtable.business.schedule.ScheduleMemberReader;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.business.schedule.ScheduleUpdater;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final ScheduleDayReader scheduleDayReader;

    private final ScheduleMemberReader scheduleMemberReader;

    private final ChoreAppender choreAppender;

    private final ScheduleUpdater scheduleUpdater;


    @Scheduled(cron = "0 0 0 * * *")
    public void createChores() {
        LocalDate targetDate = LocalDate.now();

        //1. 오늘 해야 할 스케줄 찾기
        List<Schedule> schedules = scheduleDayReader.readScheduleByDate(targetDate);

        //2. 오늘 해야 할 스케줄 sequence 업데이트 (sequence값으로 해당 스케줄의 담당자를 찾기때문에 담당자 찾기 전에 해당 날에 맞게 업데이트 해줘야함)
        scheduleUpdater.updateSequence(schedules);

        //2. 오늘 해야 할 스케줄의 담당자 찾기
        Map<ScheduleIdDto, List<Member>> scheduleAllocatorsMap = scheduleMemberReader.readAllocators(schedules);

        //3. chore insert
        choreAppender.appendChores(scheduleAllocatorsMap, targetDate);
    }
}
