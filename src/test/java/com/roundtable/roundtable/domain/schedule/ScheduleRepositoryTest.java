package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMemberRepository scheduleMemberRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("")
    @Test
    void test() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, "test");
        appendScheduleMember(schedule, member1, 1);
        appendScheduleMember(schedule, member2, 2);

        em.flush();
        em.clear();

        //when
        Schedule result = scheduleRepository.findById(schedule.getId()).get();

        //then
        result.complete(member1.getId());

    }

    private ScheduleDay appendScheduleDay(Schedule schedule1, Day day) {
        ScheduleDay scheduleDay = ScheduleDay.builder().schedule(schedule1).dayOfWeek(day).build();
        return scheduleDayRepository.save(scheduleDay);
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).password("password").house(house).build();
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

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member1, int sequence) {
        ScheduleMember scheduleMember = ScheduleMember.builder().schedule(schedule).member(member1).sequence(sequence).build();
        return scheduleMemberRepository.save(scheduleMember);
    }

}