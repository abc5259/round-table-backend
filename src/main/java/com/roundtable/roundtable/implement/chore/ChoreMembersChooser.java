package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreMembersChooser {

    /**
     * ScheduleMember에 Member 없을시 n + 1 문제 발생할 수 있음
     */
    public List<Member> chooseChoreMembers(List<ScheduleMember> scheduleMembers) {
        return scheduleMembers
                .stream()
                .filter(ScheduleMember::isManager)
                .map(ScheduleMember::getMember).toList();
    }
}
