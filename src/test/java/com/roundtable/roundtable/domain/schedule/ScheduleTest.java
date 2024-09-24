package com.roundtable.roundtable.domain.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.domain.house.House;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("스케줄을 생성할때 sequence값은 0이다.")
    @Test
    void create_schedule() {
        //given
        String name = "schedule";
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalTime startTime = LocalTime.of(12, 00);
        DivisionType divisionType = DivisionType.ROTATION;
        ScheduleType scheduleType = ScheduleType.REPEAT;
        House house = House.Id(1L);
        int sequenceSize = 2;
        Category category = Category.COOKING;

        //when
        Schedule schedule = Schedule.create(name, startDate, startTime, divisionType, scheduleType, house, sequenceSize, category);

        //thenR
        assertThat(schedule.getSequence()).isEqualTo(0);
        assertThat(schedule.getSequenceSize()).isEqualTo(2);
    }

    @DisplayName("일회성 이벤트를 만들때 division type이 FIX가 아니라면 에러를 던진다.")
    @Test
    void one_time_schedule_division_type_is_not_FIX_throw_error() {
        //given
        String name = "schedule";
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalTime startTime = LocalTime.of(12, 00);
        ScheduleType scheduleType = ScheduleType.ONE_TIME;
        DivisionType divisionType = DivisionType.ROTATION;
        House house = House.Id(1L);
        int sequenceSize = 2;
        Category category = Category.COOKING;

        //when //then
        assertThatThrownBy(() -> Schedule.create(name, startDate, startTime, divisionType, scheduleType, house, sequenceSize, category))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("일회성 스케줄은 고정 분담 방식만 사용가능합니다.");
    }



    @DisplayName("스케줄을 생성할때 분담방식이 고정이라면 sequenceSize는 무조건 1이다.")
    @Test
    void create_fix_schedule_sequenceSize_is_one() {
        //given
        String name = "schedule";
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalTime startTime = LocalTime.of(12, 00);
        DivisionType divisionType = DivisionType.FIX;
        ScheduleType scheduleType = ScheduleType.ONE_TIME;
        House house = House.Id(1L);
        int sequenceSize = 12;
        Category category = Category.COOKING;

        //when
        Schedule schedule = Schedule.create(name, startDate, startTime, divisionType, scheduleType, house, sequenceSize, category);

        //thenR
        assertThat(schedule.getSequenceSize()).isEqualTo(1);
    }
}