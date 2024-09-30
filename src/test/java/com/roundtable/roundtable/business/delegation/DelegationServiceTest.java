package com.roundtable.roundtable.business.delegation;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class DelegationServiceTest extends IntegrationTestSupport {

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
    private DelegationRepository delegationRepository;

    @Autowired
    private DelegationService sut;

    @DisplayName("Delegation을 생성한다.")
    @Test
    void createDelegation() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek()));
        CreateDelegationDto createDelegationDto = new CreateDelegationDto(schedule.getId(), "부탁해", member1.getId(), member2.getId(), now);

        //when
        Long resultId = sut.createDelegation(house.getId(), createDelegationDto);

        //then
        Delegation delegation = delegationRepository.findById(resultId).orElseThrow();
        assertThat(delegation)
                .extracting("message", "status", "delegationDate")
                .contains("부탁해", DelegationStatus.PENDING, now);
        assertThat(delegation.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(delegation.getSender().getId()).isEqualTo(member1.getId());
        assertThat(delegation.getReceiver().getId()).isEqualTo(member2.getId());
    }

    private House appendHouse(String code) {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code(code).build()).build();
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