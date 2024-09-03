package com.roundtable.roundtable.business.scheduler;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class SchedulerServiceTest extends IntegrationTestSupport {

    @Autowired
    private SchedulerService schedulerService;

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
    private ChoreRepository choreRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;

    @DisplayName("오늘 해야할 스케줄에 따라 집안일과 오늘 담당자를 생성할 수 있다.")
    @Test
    void createChores() {
        //given
        LocalDate now = LocalDate.now();
        Day day = Day.forDayOfWeek(now.getDayOfWeek());

        House house = createHouse();
        Member member1 = createMember(house, "member1");
        Member member2 = createMember(house, "member2");

        for(int i=0; i<10; i++) {
            Schedule schedule = createSchedule(Category.COOKING, house, 0, 2);
            createScheduleDay(day, schedule);
            createScheduleMembers(schedule, member1, member2, 1,2);
        }

        //when
        schedulerService.createChores();

        //then
        List<Chore> chores = choreRepository.findAll();
        List<ChoreMember> choreMembers = choreMemberRepository.findAll();

        assertThat(chores).hasSize(10);
        assertThat(choreMembers).hasSize(10);
    }

    private void createScheduleDay(Day day, Schedule schedule) {
        ScheduleDay scheduleDay = ScheduleDay.builder().dayOfWeek(day).schedule(schedule).build();
        scheduleDayRepository.save(scheduleDay);
    }

    private void createScheduleMembers(Schedule schedule, Member member1, Member member2, int sequence1, int sequence2) {
        List<ScheduleMember> scheduleMembers = List.of(
                ScheduleMember.builder().member(member1).schedule(schedule).schedule(schedule).sequence(sequence1).build(),
                ScheduleMember.builder().member(member2).schedule(schedule).schedule(schedule).sequence(sequence2).build()
        );
        scheduleMemberRepository.saveAll(scheduleMembers);
    }

    private House createHouse() {
        return houseRepository.save(House.builder().name("house1").inviteCode(InviteCode.builder().code("code1").build()).build());
    }

    private Member createMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).house(house).password("password").build();
        return memberRepository.save(member);
    }

    private Chore createChore(Schedule schedule, String matchKey) {
        return Chore.builder()
                .startDate(LocalDate.now())
                .schedule(schedule)
                .isCompleted(false)
                .matchKey(matchKey)
                .build();
    }

    private Schedule createSchedule(Category category, House house, int sequence, int sequenceSize) {
        return scheduleRepository.save(Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(category)
                .scheduleType(ScheduleType.REPEAT)
                .house(house)
                .sequence(sequence)
                .sequenceSize(sequenceSize)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .build());
    }

}