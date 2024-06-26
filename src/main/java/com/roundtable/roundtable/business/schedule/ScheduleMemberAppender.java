package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMemberAppender {

    private final ScheduleMemberFactory scheduleMemberFactory;

    private final ScheduleMemberRepository scheduleMemberRepository;

    public List<ScheduleMember> createScheduleMembers(List<Member> members, Schedule schedule) {
        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members, schedule);

        return scheduleMemberRepository.saveAll(scheduleMembers);
    }
}
