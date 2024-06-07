package com.roundtable.roundtable.business.chore;

import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChoreMapperTest {

    @DisplayName("Map<ScheduleIdDto, List<Member>> 타입의 map 객체를 Chore Entity 들로 변환할 수 있다.")
    @Test
    void toEntities() {
        //given
        Map<ScheduleIdDto, List<Member>> scheduleAllocatorsMap = new LinkedHashMap<>();

        scheduleAllocatorsMap.put(new ScheduleIdDto(1L), List.of(createMember(), createMember()));
        scheduleAllocatorsMap.put(new ScheduleIdDto(2L), List.of(createMember()));
        scheduleAllocatorsMap.put(new ScheduleIdDto(3L), List.of(createMember(), createMember(), createMember()));

        ChoreMapper choreMapper = new ChoreMapper();
        LocalDate targetDate = LocalDate.now();

        //when
        List<Chore> chores = choreMapper.toEntities(scheduleAllocatorsMap, targetDate);

        //then
        List<Long> expectedChoreScheduleIds = List.of(1L, 2L, 3L);
        List<Integer> expectedChoreMemberSize = List.of(2,1,3);
        for (int i=0; i<chores.size(); i++) {
            Assertions.assertThat(chores.get(i).getSchedule().getId()).isEqualTo(expectedChoreScheduleIds.get(i));
            Assertions.assertThat(chores.get(i).getChoreMembers()).hasSize(expectedChoreMemberSize.get(i));
            Assertions.assertThat(chores.get(i).getStartDate()).isEqualTo(targetDate);
        }

    }

    private Member createMember() {
        return Member.builder().name("name").build();
    }

}