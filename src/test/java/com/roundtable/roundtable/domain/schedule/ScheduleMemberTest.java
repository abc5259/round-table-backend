package com.roundtable.roundtable.domain.schedule;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleMemberTest {

    @DisplayName("스케줄의 담당자를 알 수 있다.")
    @Test
    void isManager() {
        //given
        int managerSequence = 2;
        int notManagerSequence = 3;

        Schedule schedule = Schedule.builder()
                .sequence(managerSequence)
                .build();

        ScheduleMember scheduleMember1 = ScheduleMember.builder()
                .schedule(schedule)
                .sequence(managerSequence)
                .build();

        ScheduleMember scheduleMember2 = ScheduleMember.builder()
                .schedule(schedule)
                .sequence(notManagerSequence)
                .build();

        //when
        boolean result1 = scheduleMember1.isManager();
        boolean result2 = scheduleMember2.isManager();

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
     }

     @DisplayName("스케줄을 완료한다.")
     @Test
     void complete() {
         //given
         int managerSequence = 1;

         Schedule schedule = Schedule.builder()
                 .sequence(managerSequence)
                 .sequenceSize(3)
                 .build();

         ScheduleMember sut = ScheduleMember.builder()
                 .schedule(schedule)
                 .sequence(managerSequence)
                 .build();

         //when
         sut.complete();

         //then
         assertThat(schedule.getSequence()).isEqualTo(managerSequence + 1);
     }
}