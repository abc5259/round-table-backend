package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleUpdaterTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleUpdater scheduleUpdater;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("여러 스케줄의 sequence를 증가시킬 수 있다.")
    @Test
    void updateSequence() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Schedule schedule1 = appendSchedule(house, "schedule1", 1, 3);
        Schedule schedule2 = appendSchedule(house, "schedule2", 2, 3);

        //when
        scheduleUpdater.updateSequence(List.of(schedule1, schedule2));
        em.flush();
        em.clear();

        //then
        List<Schedule> schedules = scheduleRepository.findAll();
        Assertions.assertThat(schedules)
                .extracting("sequence")
                .contains(2,3);

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

    private Schedule appendSchedule(House house, String name, int sequence, int sequenceSize) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(Category.COOKING)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(sequence)
                .sequenceSize(sequenceSize)
                .house(house)
                .divisionType(DivisionType.FIX)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }
}