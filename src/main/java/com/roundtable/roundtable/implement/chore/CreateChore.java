package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;

public record CreateChore(
        LocalDate startDate,
        Schedule schedule,
        List<Member> assignedMember
) {
}
