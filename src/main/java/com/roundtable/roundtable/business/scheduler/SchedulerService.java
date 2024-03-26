package com.roundtable.roundtable.business.scheduler;

import com.roundtable.roundtable.business.schedule.ScheduleMemberReader;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.time.LocalDate;
import java.util.ArrayList;
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

    private final ScheduleReader scheduleReader;
    private final ScheduleMemberReader scheduleMemberReader;


//    @Scheduled(fixedDelay = 1000)
    public void chore() {
        LocalDate targetDate = LocalDate.now();
        //1. 오늘 해야 할 스케줄 찾기
        List<Schedule> schedules = scheduleReader.readScheduleByDate(targetDate);

        //2. 오늘 해야 할 일의 담당자 찾기
        Map<Schedule, List<Member>> scheduleAllocatorsMap = scheduleMemberReader.readAllocators(schedules, targetDate);

        //3. chore insert
//        Map<Chore, List<ChoreMember>>
        List<Chore> chores = schedules.stream().map(schedule -> Chore.create(schedule, targetDate)).toList();

        //4.
    }
}
