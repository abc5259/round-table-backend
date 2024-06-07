package com.roundtable.roundtable.domain.schedule;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("sequence를 1 증가시킬 수 있다.")
    @Test
    void increaseSequence() {
        //given
        Schedule schedule = Schedule.builder().sequence(1).sequenceSize(3).build();

        //when
        schedule.increaseSequence();

        //then
        Assertions.assertThat(schedule.getSequence()).isEqualTo(2);
    }

    @DisplayName("sequence를 증가시킬때 sequenceSize보다 크다면 다시 처음으로 돌아간다.")
    @Test
    void increaseSequenceWithMax() {
        //given
        Schedule schedule = Schedule.builder().sequence(3).sequenceSize(3).build();

        //when
        schedule.increaseSequence();

        //then
        Assertions.assertThat(schedule.getSequence()).isEqualTo(Schedule.START_SEQUENCE);
    }
}