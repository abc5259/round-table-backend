package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.schedule.dto.ScheduleOfMemberResponse;
import com.roundtable.roundtable.business.schedule.dto.ScheduleResponse;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleQueryRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleOfMemberDto;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleReader {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleQueryRepository scheduleQueryRepository;

    public CursorBasedResponse<List<ScheduleResponse>> findHomeSchedulesByDate(Long homeId, LocalDate date, CursorBasedRequest cursorBasedRequest) {
        List<ScheduleDto> schedulesDtos = scheduleQueryRepository.findSchedulesByDate(homeId, date, cursorBasedRequest.toCursorPagination());
        List<ScheduleResponse> scheduleResponses = schedulesDtos.stream().map(ScheduleResponse::from).toList();

        Long lastId = scheduleResponses.isEmpty() ? 0L : scheduleResponses.get(scheduleResponses.size() - 1).id();
        return CursorBasedResponse.of(scheduleResponses, lastId);
    }

    public CursorBasedResponse<List<ScheduleOfMemberResponse>> findMemberSchedulesByDate(Long homeId, LocalDate date, Long memberId, CursorBasedRequest cursorBasedRequest) {
        List<ScheduleOfMemberDto> schedulesDtos = scheduleQueryRepository.findSchedulesByDateAndMemberId(homeId, date, memberId, cursorBasedRequest.toCursorPagination());
        List<ScheduleOfMemberResponse> scheduleResponses = schedulesDtos.stream().map(ScheduleOfMemberResponse::from).toList();

        Long lastId = scheduleResponses.isEmpty() ? 0L : scheduleResponses.get(scheduleResponses.size() - 1).id();
        return CursorBasedResponse.of(scheduleResponses, lastId);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(ScheduleErrorCode.NOT_FOUND_ID));
    }
}
