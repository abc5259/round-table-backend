package com.roundtable.roundtable.business.chore.dto;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateChore(
        LocalDate startDate,
        Schedule schedule,
        List<Member> assignedMember
) {
}
