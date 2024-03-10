package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.entity.schedule.ScheduleMemberQueryRepository;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleMemberDetailDto;
import java.util.List;
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

}
