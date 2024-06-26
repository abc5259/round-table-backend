package com.roundtable.roundtable.business.schedule;

import static java.util.stream.Collectors.groupingBy;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberQueryRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleMemberDetailDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleMemberReader {

    private final ScheduleMemberQueryRepository scheduleMemberQueryRepository;

    public List<ScheduleMemberDetailDto> readScheduleMemberDetail(Long scheduleId) {
        return scheduleMemberQueryRepository.findScheduleMemberDetail(scheduleId);
    }

    public Map<ScheduleIdDto, List<Member>>  readAllocators(List<Schedule> schedules) {
        return scheduleMemberQueryRepository.findAllocators(schedules);
    }

}
