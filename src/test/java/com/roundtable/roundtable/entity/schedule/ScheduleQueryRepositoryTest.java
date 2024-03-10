package com.roundtable.roundtable.entity.schedule;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("스케줄의 상세 내용을 조회할 수 있다.")
    @Test
    void test() {
        //given
        House house = appendHouse();
        Category category = appendCategory(house, "category");
        Schedule schedule = appendSchedule(category, house, "schedule");

        //when
        ScheduleDetailDto scheduleDetail = scheduleQueryRepository.findScheduleDetail(schedule.getId());

        //then
        Assertions.assertThat(scheduleDetail).isNotNull()
                .extracting("scheduleId", "name", "frequency", "startDate", "startTime", "divisionType", "category")
                .containsExactly(
                        schedule.getId(),
                        schedule.getName(),
                        schedule.getFrequency(),
                        schedule.getStartDate(),
                        schedule.getStartTime(),
                        schedule.getDivisionType(),
                        new ScheduleDetailDto.CategoryDetailDto(
                                category.getId(),
                                category.getName(),
                                category.getPoint()
                        ));

     }

    private House appendHouse() {
        House house = House.builder().name("house").build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house, String name) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(11,11))
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Category appendCategory(House house, String name) {
        Category category = Category.builder().house(house).name(name).point(1).build();
        return categoryRepository.save(category);
    }
}