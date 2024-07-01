package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.dto.CreateScheduleDto;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleServiceTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleService scheduleService;


    @Autowired
    private EntityManager em;

    @DisplayName("반복 스케줄에서 시작날짜가 오늘과 같다면 스케줄과 집안일 둘다 생성한다.")
    @Test
    void createScheduleWhenStartDateWithTodayAndRepeatSchedule() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house);

        CreateScheduleDto createScheduleDto = new CreateScheduleDto(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                ScheduleType.REPEAT,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when
        Long scheduleId = scheduleService.createSchedule(createScheduleDto, authMember, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();
        assertThat(chore).hasSize(1);
    }

    @DisplayName("반복 스케줄에서 시작날짜가 오늘이 아니라면 스케줄만 생성한다.")
    @Test
    void createScheduleWhenStartDateWithNotTodayAndRepeatSchedule() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,15);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house);

        CreateScheduleDto createSchedule = new CreateScheduleDto(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                ScheduleType.REPEAT,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, authMember, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(0);
    }

    @DisplayName("일회성 스케줄에서 시작날짜가 오늘이라면 스케줄과 집안일을 같이 생성한다.")
    @Test
    void createScheduleWhenStartDateWithTodayAndOneTimeSchedule() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,14);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house);

        CreateScheduleDto createSchedule = new CreateScheduleDto(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                ScheduleType.ONE_TIME,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, authMember, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(1);

    }

    @DisplayName("일회성 스케줄에서 시작날짜가 오늘이 아니더라도 스케줄과 집안일을 같이 생성한다.")
    @Test
    void createScheduleWhenStartDateWithNotTodayAndOneTimeSchedule() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,15);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house);

        CreateScheduleDto createSchedule = new CreateScheduleDto(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                ScheduleType.ONE_TIME,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, authMember, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(1);

    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);
        return member;
    }

    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        em.persist(house);
        return house;
    }
}