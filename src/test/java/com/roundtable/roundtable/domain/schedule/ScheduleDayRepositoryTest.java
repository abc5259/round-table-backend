package com.roundtable.roundtable.domain.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
class ScheduleDayRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("요일에 해당하는 스케줄을 찾을 수 있다.")
    @Test
    void findSchedulesByDay() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Schedule schedule1 = appendSchedule(house, "schedule1");
        Schedule schedule2 = appendSchedule(house, "schedule2");
        Schedule schedule3 = appendSchedule(house, "schedule3");
        appendScheduleDay(schedule1, Day.MONDAY);
        appendScheduleDay(schedule2, Day.MONDAY);
        appendScheduleDay(schedule3, Day.SATURDAY);

        //when
        List<Schedule> schedules = scheduleDayRepository.findRepeatSchedulesByDay(Day.MONDAY);

        //then
        assertThat(schedules).hasSize(2)
                .extracting("id", "name")
                .contains(
                        Tuple.tuple(schedule1.getId(), schedule1.getName()),
                        Tuple.tuple(schedule2.getId(), schedule2.getName())
                );

    }

    private ScheduleDay appendScheduleDay(Schedule schedule1, Day day) {
        ScheduleDay scheduleDay = ScheduleDay.builder().schedule(schedule1).dayOfWeek(day).build();
        return scheduleDayRepository.save(scheduleDay);
    }

    private Member appendMember(House house) {
        Member member = Member.builder().name("name").email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(House house, String name) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(Category.COOKING)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }
}