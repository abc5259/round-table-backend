package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import java.util.List;

public record CreateChore(
        Schedule schedule,
        List<Member> assignedMember
) {
}
