package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ChoreMapper {

    public List<Chore> toEntities(Map<ScheduleIdDto, List<Member>> scheduleAllocatorsMap, LocalDate startDate) {
        return scheduleAllocatorsMap.entrySet().stream()
                .map(entry -> Chore.create(
                            Schedule.Id(entry.getKey().id()),
                            entry.getValue().stream().map(member -> ChoreMember.create(null, member)).toList(),
                            startDate
                        )
                ).toList();
    }
}
