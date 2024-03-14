package com.roundtable.roundtable.business.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {

    @Scheduled(fixedDelay = 1000)
    public void chore() {
        //1. 오늘 해야 할 스케줄 찾기

        //2. 오늘 해야 할 일의 담당자 찾기

        //3. chore insert

        //4.
    }
}
