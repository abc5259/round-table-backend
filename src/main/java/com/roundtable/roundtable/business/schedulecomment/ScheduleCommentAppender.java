package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.domain.schedulecomment.Content;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduleCommentAppender {

    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleComment append(CreateScheduleComment createScheduleComment) {
        ScheduleComment scheduleComment = ScheduleComment.create(
                Content.of(createScheduleComment.content()),
                createScheduleComment.schedule(),
                createScheduleComment.writer()
        );

        return scheduleCommentRepository.save(scheduleComment);
    }
}
