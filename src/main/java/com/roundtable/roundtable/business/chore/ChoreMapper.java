package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChoreMapper {

    public List<Chore> toChoreEntities(Map<Schedule, List<Member>> scheduleAllocatorsMap, LocalDate startDate) {
        return scheduleAllocatorsMap.entrySet().stream()
                .map(entry -> Chore.create(
                        entry.getKey(),
                        entry.getValue().stream().map(member -> ChoreMember.create(null, member)).toList(),
                        startDate)).toList();
    }
}
