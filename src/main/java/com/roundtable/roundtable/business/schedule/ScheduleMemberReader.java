package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import com.roundtable.roundtable.entity.schedule.ScheduleMemberQueryRepository;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleMemberDetailDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleMemberReader {

    private final ScheduleMemberQueryRepository scheduleMemberQueryRepository;

    public List<ScheduleMemberDetailDto> findScheduleMemberDetail(Long scheduleId) {
        return scheduleMemberQueryRepository.findScheduleMemberDetail(scheduleId);
    }

    public Map<Schedule, List<ScheduleMember>> readAllocators(List<Schedule> schedules, LocalDate date) {
        return scheduleMemberQueryRepository.findAllocators(schedules, date).stream()
                .collect(Collectors.groupingBy(ScheduleMember::getSchedule));
    }

}
