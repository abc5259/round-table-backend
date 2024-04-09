package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;

public record CreateScheduleComment(

        Member writer,
        Schedule schedule,
        String content
) {
}
