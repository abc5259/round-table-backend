package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreMembersChooser {

    public List<Long> chooseChoreMemberIds(List<ScheduleMember> scheduleMembers) {
        return scheduleMembers
                .stream()
                .filter(ScheduleMember::isStartSequence)
                .map(scheduleMember -> scheduleMember.getMember().getId()).toList();
    }
}
