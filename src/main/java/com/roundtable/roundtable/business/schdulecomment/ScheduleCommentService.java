package com.roundtable.roundtable.business.schdulecomment;

import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleCommentService {

    private final ScheduleCommentAppender scheduleCommentAppender;
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

}
