package com.roundtable.roundtable.implement.chore;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ScheduleChoreAppendDirectorTest {

    @Autowired
    private ScheduleChoreAppendDirector scheduleChoreAppendDirector;

    @Autowired
    private EntityManager em;


    @DisplayName("시작날짜가 오늘과 같다면 스케줄과 집안일 둘다 추가한다.")
    @Test
    void test() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Member member = createMemberInHouse(house);

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                FrequencyType.DAILY,
                2,
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId())
        );

        //when
        Schedule schedule = scheduleChoreAppendDirector.append(createSchedule, house, now);

        //then
        assertThat(schedule.getId()).isNotNull();
        assertThat(schedule).isNotNull()
                .extracting( "name", "sequence", "sequenceSize", "divisionType", "house")
                .contains(
                        createSchedule.name(),
                        1,
                        createSchedule.memberIds().size(),
                        createSchedule.divisionType(),
                        house
                );

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(1)
                .extracting("schedule")
                .contains(schedule);
    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);
        return member;
    }

    private House createHouse() {
        House house = House.of("house");
        em.persist(house);
        return house;
    }
}