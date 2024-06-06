package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDetailDto;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleReaderTest extends IntegrationTestSupport {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleReader scheduleReader;

    @DisplayName("id를 기반으로 schedule을 조회할 수 있다.")
    @Test
    void findById() {
        //given
        House house = appendHouse();
        Category category = Category.COOKING;
        Schedule schedule = appendSchedule(category, house, "schedule");

        //when
        Schedule findSchedule = scheduleReader.findById(schedule.getId());

        //then
        assertThat(findSchedule).isEqualTo(schedule);
     }

    @DisplayName("ScheduleId에 해당하는 레코드가 없는 경우 에러를 던진다.")
    @Test
    void scheduleDetailDto() {
        //given //when //then
        assertThatThrownBy(() -> scheduleReader.findById(1L))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(ScheduleErrorCode.NOT_FOUND_ID.getMessage());
    }

    @DisplayName("스케줄의 세부사항을 조회할 수 있다.")
    @Test
    void findScheduleDetail() {
        //given
        House house = appendHouse();
        Category category = Category.COOKING;
        Schedule schedule = appendSchedule(category, house, "schedule");

        //when
        ScheduleDetailDto scheduleDetail = scheduleReader.findScheduleDetail(schedule.getId());

        //then
        assertThat(scheduleDetail).isNotNull()
                .extracting("scheduleId", "name",  "startDate", "startTime", "divisionType", "category")
                .containsExactly(
                        schedule.getId(),
                        schedule.getName(),
                        schedule.getStartDate(),
                        schedule.getStartTime(),
                        schedule.getDivisionType(),
                        schedule.getCategory());

     }


    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house, String name) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(11,11))
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }
}