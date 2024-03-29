package com.roundtable.roundtable.business.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
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
        Category category = createCategory(house);
        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);

        LocalDate startDate = LocalDate.now();

        Schedule schedule = Schedule.create(
                "schedule1",
                Frequency.of(FrequencyType.DAILY, 2),
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

    private Category createCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        em.persist(category);
        return category;
    }
}