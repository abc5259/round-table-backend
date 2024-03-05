package com.roundtable.roundtable.business.schdulecomment;

import com.roundtable.roundtable.entity.schedulecomment.ScheduleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleCommentReader {

    private final ScheduleCommentRepository scheduleCommentRepository;

//    public List<Sch>
}
