package com.roundtable.roundtable.business.schedulecomment;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;

public record CreateScheduleComment(

        Member writer,
        Schedule schedule,
        String content
) {
}
