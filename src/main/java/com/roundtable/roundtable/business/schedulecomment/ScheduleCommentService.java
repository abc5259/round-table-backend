package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.entity.schedulecomment.dto.ScheduleCommentDetailDto;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleCommentErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleCommentService {

    private final ScheduleCommentAppender scheduleCommentAppender;
    private final ScheduleCommentReader scheduleCommentReader;
    private final MemberReader memberReader;
    private final ScheduleReader scheduleReader;


    public Long createScheduleComment(CreateScheduleCommentDto createScheduleCommentDto) {

        Member writer = memberReader.findById(createScheduleCommentDto.writerId());

        Schedule schedule = scheduleReader.findById(createScheduleCommentDto.scheduleId());

        ScheduleComment scheduleComment = scheduleCommentAppender.append(
                new CreateScheduleComment(
                        writer,
                        schedule,
                        createScheduleCommentDto.content()
                )
        );

        return scheduleComment.getId();
    }

    public CursorBasedResponse<List<ScheduleCommentDetailDto>> findByScheduleId(Member loginMember, Long scheduleId, CursorBasedRequest cursorBasedRequest) {
        Schedule schedule = scheduleReader.findById(scheduleId);
        if(!schedule.isSameHouse(loginMember)) {
            throw new CreateEntityException(ScheduleCommentErrorCode.NOT_SAME_HOUSE);
        }

        return scheduleCommentReader.findByScheduleId(scheduleId, cursorBasedRequest);
    }

}
