package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.dto.ScheduleCompletionEvent;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RecordApplicationEvents
class ScheduleCompletionServiceTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleCompletionService sut;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMemberRepository scheduleMemberRepository;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private ScheduleCompletionRepository scheduleCompletionRepository;

    @Autowired
    private ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @DisplayName("스케줄을 완료한다.")
    @Test
    void complete() {
        //given
        LocalDate date = LocalDate.of(2021, 12, 2);
        House house = appendHouse();
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleMember(schedule, member2, 1);
        appendScheduleDay(schedule, Day.forDayOfWeek(date.getDayOfWeek()));

        //when
        sut.complete(schedule.getId(), new AuthMember(member1.getId(), house.getId()), date);

        //then
        assertThat(schedule.getSequence()).isEqualTo(1);

        ScheduleCompletion scheduleCompletion = scheduleCompletionRepository.findAll().get(0);
        assertThat(scheduleCompletion.getCompletionDate()).isEqualTo(date);
        assertThat(scheduleCompletion.getSchedule().getId()).isEqualTo(schedule.getId());

        ScheduleCompletionMember scheduleCompletionMember = scheduleCompletionMemberRepository.findAll().get(0);
        assertThat(scheduleCompletionMember)
                .extracting("member", "scheduleCompletion")
                .containsExactly(member1, scheduleCompletion);
        assertThat(applicationEvents.stream(ScheduleCompletionEvent.class))
                .hasSize(1)
                .anySatisfy(event -> {
                    assertAll(
                            () -> assertThat(event.scheduleId()).isEqualTo(schedule.getId()),
                            () -> assertThat(event.houseId()).isEqualTo(house.getId()),
                            () -> assertThat(event.managerIds()).isEqualTo(List.of(member1.getId()))
                    );
                });
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Schedule appendSchedule(House house, DivisionType divisionType, ScheduleType scheduleType) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(Category.COOKING)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(0)
                .sequenceSize(2)
                .house(house)
                .divisionType(divisionType)
                .scheduleType(scheduleType)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member, int sequence) {
        ScheduleMember scheduleMember = ScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .sequence(sequence)
                .build();
        return scheduleMemberRepository.save(scheduleMember);
    }

    private ScheduleDay appendScheduleDay(Schedule schedule, Day day) {
        ScheduleDay scheduleDay = ScheduleDay.builder()
                .schedule(schedule)
                .dayOfWeek(day)
                .build();
        return scheduleDayRepository.save(scheduleDay);
    }


}