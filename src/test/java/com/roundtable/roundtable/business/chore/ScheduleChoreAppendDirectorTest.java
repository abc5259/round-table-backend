package com.roundtable.roundtable.business.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.business.schedule.dto.CreateSchedule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleChoreAppendDirectorTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleChoreAppendDirector scheduleChoreAppendDirector;

    @Autowired
    private EntityManager em;


    @DisplayName("시작날짜가 오늘과 같다면 스케줄과 집안일 둘다 추가한다.")
    @Test
    void appendWhenStartDateWithToday() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house, "email");

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        //when
        Schedule schedule = scheduleChoreAppendDirector.append(createSchedule, house, now);

        //then
        assertThat(schedule.getId()).isNotNull();
        assertThat(schedule).isNotNull()
                .extracting( "name", "sequence", "sequenceSize", "divisionType", "house", "category")
                .contains(
                        createSchedule.name(),
                        1,
                        createSchedule.memberIds().size(),
                        createSchedule.divisionType(),
                        house,
                        category
                );

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(1)
                .extracting("schedule")
                .contains(schedule);
    }

    @DisplayName("시작날짜가 오늘이 아니라면 스케줄만 추가한다.")
    @Test
    void appendWhenStartDateWithNotToday() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,15);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member = createMemberInHouse(house, "email");

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId()),
                category,
                List.of(1)
        );

        //when
        Schedule schedule = scheduleChoreAppendDirector.append(createSchedule, house, now);

        //then
        assertThat(schedule.getId()).isNotNull();
        assertThat(schedule).isNotNull()
                .extracting( "name", "sequence", "sequenceSize", "divisionType", "house","category")
                .contains(
                        createSchedule.name(),
                        1,
                        createSchedule.memberIds().size(),
                        createSchedule.divisionType(),
                        house,
                        category
                );

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(0);
    }

    @DisplayName("시작날짜가 오늘이라 스케줄과 집안일을 둘다 생성해야 할때 스케줄의 분담방식이 로테이션이라면 스케줄을 맡은 멤버와 집안일을 맡은 멤버는 다를 수 있다.")
    @Test
    void appendWhenStartDateWithTodayAndDifMembers() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Category category = Category.CLEANING;
        Member member1 = createMemberInHouse(house, "email1");
        Member member2 = createMemberInHouse(house, "email2");

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.ROTATION,
                List.of(member1.getId(), member2.getId()),
                category,
                List.of(1)
        );

        //when
        Schedule schedule = scheduleChoreAppendDirector.append(createSchedule, house, now);

        //then
        assertThat(schedule.getId()).isNotNull();
        assertThat(schedule).isNotNull()
                .extracting( "name", "sequence", "sequenceSize", "divisionType", "house","category")
                .contains(
                        createSchedule.name(),
                        1,
                        createSchedule.memberIds().size(),
                        createSchedule.divisionType(),
                        house,
                        category
                );
        List<ScheduleMember> scheduleMembers = em.createQuery("select sm from ScheduleMember sm",
                ScheduleMember.class).getResultList();
        assertThat(scheduleMembers).hasSize(2)
                .extracting("member", "schedule", "sequence")
                .containsExactlyInAnyOrder(
                        tuple(member1,schedule,1),
                        tuple(member2,schedule,2)
                );

        List<Chore> chores = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();
        assertThat(chores).hasSize(1);

        List<ChoreMember> choreMembers = em.createQuery("select cm from ChoreMember cm",
                ChoreMember.class).getResultList();
        assertThat(choreMembers).hasSize(1)
                .extracting("chore", "member")
                .containsExactlyInAnyOrder(
                        tuple(chores.get(0), member1)
                );
    }

    private Member createMemberInHouse(House house, String email) {

        Member member = Member.builder().email(email).password("password").build();
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