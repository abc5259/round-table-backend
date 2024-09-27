package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.schedule.DivisionType.*;
import static com.roundtable.roundtable.domain.schedule.ScheduleType.*;
import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleQueryRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private ScheduleQueryRepository sut;

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
    private ExtraScheduleMemberRepository extraScheduleMemberRepository;

    @DisplayName("주어진 날짜에 해야할 스케줄을 조회한다.")
    @Test
    void findSchedulesByDate() {
        //given
        LocalDate date = LocalDate.of(2022,1,1);
        House house = appendHouse();
        Member member1 = appendMember(house, "email1", "name1");
        Member member2 = appendMember(house, "email2", "name2");
        Member member3 = appendMember(house, "email3", "name3");
        Member member4 = appendMember(house, "email4", "name4");

        Schedule schedule1 = prepareSchedule(house, date.getDayOfWeek(), List.of(member1, member2), List.of(member3, member4));
        Schedule schedule2 = prepareCompletionSchedule(house, date, member2, member3);
        Schedule schedule3 = prepareSchedule(house, date.getDayOfWeek().plus(1), List.of(member1, member2), List.of(member3, member4));

        //when
        List<ScheduleDto> schedules = sut.findSchedulesByDate(house.getId(), date, 0L);

        //then
        assertThat(schedules).hasSize(2)
                .containsExactly(
                        new ScheduleDto(
                                schedule1.getId(),
                                schedule1.getName(),
                                schedule1.getCategory(),
                                false,
                                schedule1.getStartTime(),
                                member1.getName() + "," + member2.getName(),
                                member3.getName() + "," + member4.getName()
                        ),
                        new ScheduleDto(
                                schedule2.getId(),
                                schedule2.getName(),
                                schedule2.getCategory(),
                                true,
                                schedule2.getStartTime(),
                                member3.getName(),
                                null
                        )

                );

    }

    private Schedule prepareSchedule(House house, DayOfWeek date, List<Member> managers, List<Member> extraMembers) {
        Schedule schedule = appendSchedule(house, FIX, ONE_TIME, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(date));
        managers.forEach(member -> appendScheduleMember(schedule, member, 0));
        extraMembers.forEach(member -> appendExtraScheduleMember(schedule, member));
        return schedule;
    }

    private Schedule prepareCompletionSchedule(House house, LocalDate date, Member member, Member completionMember) {
        Schedule schedule = appendSchedule(house, ROTATION, REPEAT, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(date.getDayOfWeek()));
        appendScheduleMember(schedule, member, 0);
        appendScheduleMember(schedule, completionMember, 1);
        appendScheduleCompletion(schedule, date, 1);
        return schedule;
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }


    private Member appendMember(House house, String email, String name) {
        Member member = Member.builder().name(name).email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Schedule appendSchedule(House house, DivisionType divisionType, ScheduleType scheduleType, int sequence) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(Category.COOKING)
                .startDate(LocalDate.now())
                .startTime(LocalTime.of(1,1))
                .sequence(sequence)
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

    private ScheduleCompletion appendScheduleCompletion(Schedule schedule, LocalDate date, Integer sequence) {
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.builder()
                .schedule(schedule)
                .completionDate(date)
                .sequence(sequence)
                .build();
        return scheduleCompletionRepository.save(scheduleCompletion);
    }

    private ExtraScheduleMember appendExtraScheduleMember(Schedule schedule, Member member) {
        ExtraScheduleMember extraScheduleMember = ExtraScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .build();
        return extraScheduleMemberRepository.save(extraScheduleMember);
    }
}