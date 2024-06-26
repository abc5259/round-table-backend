package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleQueryRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDetailDto;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleReader {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleQueryRepository scheduleQueryRepository;

    private final ScheduleMemberReader scheduleMemberReader;

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundEntityException(ScheduleErrorCode.NOT_FOUND_ID));
    }

    public ScheduleDetailDto findScheduleDetail(Long scheduleId) {

        return scheduleQueryRepository
                .findScheduleDetail(scheduleId)
                .withAllocators(scheduleMemberReader.readScheduleMemberDetail(scheduleId));
    }
}
