package com.roundtable.roundtable.entity.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
        Frequency frequency = Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build();
        Schedule schedule = appendSchedule(category, house, "schedule", frequency, LocalDate.now());

        //when
        ScheduleDetailDto scheduleDetail = scheduleQueryRepository.findScheduleDetail(schedule.getId());

        //then
        assertThat(scheduleDetail).isNotNull()
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

     @DisplayName("해당 날짜에 시행하는 스케줄을 조회할 수 있다.")
     @Test
     void findScheduleByDate() {
         //given
         House house = appendHouse();
         Category category = appendCategory(house, "category");
         Frequency frequency1 = Frequency.builder().frequencyType(FrequencyType.ONCE).frequencyInterval(0).build();

         //2틀 간격
         Frequency frequency2 = Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build();
         //4일 간격
         Frequency frequency3 = Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(4).build();

         //화요일마다
         Frequency frequency4 = Frequency.builder().frequencyType(FrequencyType.WEEKLY).frequencyInterval(3).build();
         //수요일마다
         Frequency frequency5 = Frequency.builder().frequencyType(FrequencyType.WEEKLY).frequencyInterval(4).build();

         LocalDate date = LocalDate.of(2024,3,18); //월요일
         LocalDate targetDate = LocalDate.of(2024, 3, 20); //수요일

         Schedule schedule = appendSchedule(category, house, "schedule1", frequency1, targetDate);
         Schedule schedule2 = appendSchedule(category, house, "schedule2", frequency2, date);
         Schedule schedule3 = appendSchedule(category, house, "schedule3", frequency3, date);
         Schedule schedule4 = appendSchedule(category, house, "schedule4", frequency4, date);
         Schedule schedule5 = appendSchedule(category, house, "schedule5", frequency5, date);

         //when
         List<Schedule> schedules = scheduleQueryRepository.findScheduleByDate(targetDate);

         //then
         assertThat(schedules).hasSize(3)
                 .containsExactly(schedule, schedule2, schedule5);
      }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house, String name, Frequency frequency,
                                    LocalDate startDate) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(startDate)
                .startTime(LocalTime.of(11,11))
                .frequency(frequency)
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