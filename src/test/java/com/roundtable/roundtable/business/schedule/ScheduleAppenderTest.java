package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.schedule.dto.CreateSchedule;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleAppenderTest extends IntegrationTestSupport {
    @Autowired
    ScheduleAppender scheduleAppender;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    HouseRepository houseRepository;

    @Autowired
    ScheduleMemberRepository scheduleMemberRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        house = houseRepository.save(house);

        Member member1 = Member.builder().email("email1").password("password").build();
        Member member2 = Member.builder().email("email2").password("password").build();
        member1.enterHouse(house);
        member2.enterHouse(house);
        memberRepository.saveAll(List.of(member1, member2));
    }

    @DisplayName("스케줄에 필요한 내용과 수행할 맴버를 받아 스케줄을 생성한다.")
    @Test
    void createSchedule_FIX() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        CreateSchedule request =
                creatCreateRequest("schedule1", memberIds, DivisionType.FIX, LocalDate.now(), Category.CLEANING, List.of(1));

        //when
        Schedule schedule = scheduleAppender.appendSchedule(request, house, LocalDate.now());

        //then
        assertThat(schedule).isNotNull()
                .extracting("id", "name", "startDate", "startTime", "sequence", "sequenceSize", "category")
                .contains(
                        schedule.getId(), request.name(), request.startDate(), request.startTime(), 1, memberIds.size(), Category.CLEANING
                );
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findAll();
        assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence", "Schedule")
                .contains(
                        tuple(1,schedule),
                        tuple(1,schedule)
                );



    }

    @DisplayName("스케줄에 필요한 내용과 수행할 맴버를 받아 스케줄을 생성한다.")
    @Test
    void createSchedule_ROTATION() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        CreateSchedule request =
                creatCreateRequest("schedule1", memberIds, DivisionType.ROTATION, LocalDate.now(), Category.CLEANING, List.of(1));

        //when
        Schedule schedule = scheduleAppender.appendSchedule(request, house, LocalDate.now());

        //then
        assertThat(schedule).isNotNull()
                .extracting("id", "name", "startDate", "startTime", "sequence", "sequenceSize", "category")
                .contains(
                        schedule.getId(), request.name(), request.startDate(), request.startTime(), 1, memberIds.size(), Category.CLEANING
                );
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findAll();
        assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence","schedule")
                .contains(
                        tuple(1,schedule),
                        tuple(2,schedule)
                );

    }

    @DisplayName("스케줄을 생성할때 중복된 member id가 들어오면 에러를 던진다.")
    @Test
    void createScheduleWhenDuplicatedMemberId_fail() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();
        List<Long> duplicatedIds = new ArrayList<>(memberIds);
        duplicatedIds.add(memberIds.get(0));

        CreateSchedule request =
                creatCreateRequest("schedule1", duplicatedIds, DivisionType.FIX, LocalDate.now(), Category.CLEANING, List.of(1));

        //when //then
        assertThatThrownBy(() -> scheduleAppender.appendSchedule(request, house, LocalDate.now()))
                .isInstanceOf(CreateEntityException.class)
                .hasMessage(ScheduleErrorCode.DUPLICATED_MEMBER_ID.getMessage());

    }

    @DisplayName("스케줄을 생성할때는 시작날짜는 과거일 수 없다.")
    @Test
    void createScheduleWhenBeforeDate_fail() {
        //given
        House house = houseRepository.findAll().get(0);
        List<Long> memberIds = memberRepository.findAll().stream().map(Member::getId).toList();

        LocalDate startDate = LocalDate.now().minusDays(1);

        CreateSchedule request =
                creatCreateRequest("schedule1", memberIds, DivisionType.FIX, startDate, Category.CLEANING, List.of(1));

        //when //then
        assertThatThrownBy(() -> scheduleAppender.appendSchedule(request, house, LocalDate.now()))
                .isInstanceOf(CreateEntityException.class)
                .hasMessage(ScheduleErrorCode.INVALID_START_DATE.getMessage());

    }

     private CreateSchedule creatCreateRequest(String name, List<Long> memberIds,
                                               DivisionType divisionType, LocalDate startDate, Category category, List<Integer> dayIds) {
        return new CreateSchedule(
                name,
                startDate,
                LocalTime.of(23,0),
                divisionType,
                memberIds,
                category,
                dayIds
        );
     }
}