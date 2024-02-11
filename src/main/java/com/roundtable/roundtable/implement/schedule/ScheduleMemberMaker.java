package com.roundtable.roundtable.implement.schedule;

import static com.roundtable.roundtable.entity.schedule.DivisionType.*;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMemberMaker {
    public static final int START_SEQUENCE = 1;

    public List<ScheduleMember> createScheduleMembers(List<Member> members, DivisionType divisionType) {
        if(divisionType.equals(FIX)) return toScheduleMembersWithFixSequence(members);

        return toScheduleMembersWithIncreaseSequence(members);
    }

    private List<ScheduleMember> toScheduleMembersWithFixSequence(List<Member> assignedMembers) {
        return assignedMembers.stream().map(member -> ScheduleMember.of(
                member,
                START_SEQUENCE
        )).toList();
    }

    private List<ScheduleMember> toScheduleMembersWithIncreaseSequence(List<Member> assignedMembers) {
        AtomicInteger index = new AtomicInteger(START_SEQUENCE);

        return assignedMembers.stream().map(member -> ScheduleMember.of(
                member,
                index.getAndIncrement() + 1
        )).toList();
    }
}
