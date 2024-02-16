package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.implement.schedule.ScheduleMemberReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreMembersChooser {

    private final ScheduleMemberReader scheduleMemberReader;

    public List<Long> chooseChoreMemberIds(Schedule schedule) {
        return scheduleMemberReader.findScheduleManager(schedule)
                .stream()
                .map(scheduleMember -> scheduleMember.getMember().getId()).toList();
    }
}
