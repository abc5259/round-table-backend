package com.roundtable.roundtable.implement.schedule;

import static com.roundtable.roundtable.entity.schedule.DivisionType.FIX;
import static com.roundtable.roundtable.entity.schedule.DivisionType.ROTATION;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMemberFactory {
    public static final int START_SEQUENCE = 1;

    public List<ScheduleMember> createScheduleMembers(List<Member> members, Schedule schedule) {
        List<ScheduleMember> scheduleMembers = null;

        DivisionType divisionType = schedule.getDivisionType();

        if(divisionType.equals(FIX)) {
            scheduleMembers = toScheduleMembersWithFixSequence(members, schedule);
        }

        if(divisionType.equals(ROTATION)) {
            scheduleMembers = toScheduleMembersWithIncreaseSequence(members, schedule);
        }

        return scheduleMembers;
    }

    private List<ScheduleMember> toScheduleMembersWithFixSequence(List<Member> assignedMembers, Schedule schedule) {
        return assignedMembers.stream().map(member -> ScheduleMember.of(
                member,
                schedule,
                START_SEQUENCE
        )).toList();
    }

    private List<ScheduleMember> toScheduleMembersWithIncreaseSequence(List<Member> assignedMembers, Schedule schedule) {
        AtomicInteger index = new AtomicInteger(START_SEQUENCE-1);

        return assignedMembers.stream().map(member -> ScheduleMember.of(
                member,
                schedule,
                index.getAndIncrement() + 1
        )).toList();
    }
}
