package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.business.schedulecomment.dto.CreateScheduleComment;
import com.roundtable.roundtable.business.schedulecomment.dto.CreateScheduleCommentDto;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
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
    private final ScheduleReader scheduleReader;


    public Long createScheduleComment(AuthMember authMember, CreateScheduleCommentDto createScheduleCommentDto) {

        Member writer = Member.toAuthMember(authMember.memberId(), authMember.houseId());

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

    public CursorBasedResponse<List<ScheduleCommentDetailDto>> findByScheduleId(AuthMember authMember, Long scheduleId, CursorBasedRequest cursorBasedRequest) {
        Schedule schedule = scheduleReader.findById(scheduleId);
        if(!schedule.isSameHouse(Member.toAuthMember(authMember.memberId(), authMember.houseId()))) {
            throw new CreateEntityException(ScheduleCommentErrorCode.NOT_SAME_HOUSE);
        }

        return scheduleCommentReader.findByScheduleId(scheduleId, cursorBasedRequest);
    }

}
