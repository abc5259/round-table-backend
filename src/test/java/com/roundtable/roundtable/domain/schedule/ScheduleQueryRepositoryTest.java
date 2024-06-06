package com.roundtable.roundtable.domain.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleQueryRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private ScheduleQueryRepository scheduleQueryRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("스케줄의 상세 내용을 조회할 수 있다.")
    @Test
    void test() {
        //given
        House house = appendHouse();
        Category category = Category.CLEANING;
        Schedule schedule = appendSchedule(category, house, "schedule", LocalDate.now());

        //when
        ScheduleDetailDto scheduleDetail = scheduleQueryRepository.findScheduleDetail(schedule.getId());

        //then
        assertThat(scheduleDetail).isNotNull()
                .extracting("scheduleId", "name", "startDate", "startTime", "divisionType", "category")
                .containsExactly(
                        schedule.getId(),
                        schedule.getName(),
                        schedule.getStartDate(),
                        schedule.getStartTime(),
                        schedule.getDivisionType(),
                        category);

     }


    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house, String name,
                                    LocalDate startDate) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(startDate)
                .startTime(LocalTime.of(11,11))
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }
}