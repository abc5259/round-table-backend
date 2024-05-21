package com.roundtable.roundtable.business.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.chore.dto.CreateChore;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreAppenderTest extends IntegrationTestSupport {

    @Autowired
    ChoreAppender choreAppender;

    @Autowired
    EntityManager em;

    @DisplayName("집안일을 등록한다.")
    @Test
    void test() {
        //given
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        em.persist(house);
        Category category = Category.CLEANING;
        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);

        LocalDate startDate = LocalDate.now();

        Schedule schedule = Schedule.create(
                "schedule1",
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                member.getHouse(),
                1,
                category
        );
        em.persist(schedule);

        CreateChore createChore = new CreateChore(startDate, schedule, List.of(member));

        //when
        Chore chore = choreAppender.appendChore(createChore, house);

        //then
        assertThat(chore).isNotNull()
                .extracting("id","schedule", "isCompleted", "startDate")
                .contains(chore.getId(), schedule, false, startDate);

        List<ChoreMember> choreMembers = em.createQuery("select cm from ChoreMember cm where cm.chore = :chore",
                        ChoreMember.class)
                .setParameter("chore", chore)
                .getResultList();
        assertThat(choreMembers).hasSize(1)
                .extracting("chore", "member")
                .containsExactlyInAnyOrder(
                        tuple(chore, member)
                );
    }
}