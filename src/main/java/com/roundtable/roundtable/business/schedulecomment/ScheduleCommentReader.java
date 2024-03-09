package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleCommentQueryRepository;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleCommentRepository;
import com.roundtable.roundtable.entity.schedulecomment.dto.ScheduleCommentDetailDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleCommentReader {

    private final ScheduleCommentQueryRepository scheduleCommentQueryRepository;

    public CursorBasedResponse<List<ScheduleCommentDetailDto>> findByScheduleId(Long scheduleId, CursorBasedRequest cursorBasedRequest) {

        List<ScheduleCommentDetailDto> content = scheduleCommentQueryRepository.findBySchedule(scheduleId, cursorBasedRequest.toCursorPagination());

        return CursorBasedResponse.of(
                content,
                content.get(content.size() - 1).commentId()
        );
    }
}
