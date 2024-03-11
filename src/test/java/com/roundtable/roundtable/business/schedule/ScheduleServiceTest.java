package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.house.CreateScheduleDto;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleServiceTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleService scheduleService;


    @Autowired
    private EntityManager em;

    @DisplayName("시작날짜가 오늘과 같다면 스케줄과 집안일 둘다 생성한다.")
    @Test
    void createScheduleWhenStartDateWithToday() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Category category = createCategory(house);
        Member member = createMemberInHouse(house);

        CreateScheduleDto createScheduleDto = new CreateScheduleDto(
                "schedule1",
                FrequencyType.DAILY,
                2,
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId()),
                category.getId()
        );

        //when
        Long scheduleId = scheduleService.createSchedule(createScheduleDto, member, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();
        assertThat(chore).hasSize(1);
    }

    @DisplayName("시작날짜가 오늘이 아니라면 스케줄만 생성한다.")
    @Test
    void createScheduleWhenStartDateWithNotToday() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,15);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Category category = createCategory(house);
        Member member = createMemberInHouse(house);

        CreateScheduleDto createSchedule = new CreateScheduleDto(
                "schedule1",
                FrequencyType.DAILY,
                2,
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId()),
                category.getId()
        );

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, member, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(0);
    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);
        return member;
    }

    private Category createCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        em.persist(category);
        return category;
    }

    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        em.persist(house);
        return house;
    }
}