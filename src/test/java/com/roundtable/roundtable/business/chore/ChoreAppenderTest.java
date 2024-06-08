package com.roundtable.roundtable.business.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.chore.dto.CreateChore;
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
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreAppenderTest extends IntegrationTestSupport {

    @Autowired
    ChoreAppender choreAppender;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;

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
                ScheduleType.REPEAT,
                member.getHouse(),
                1,
                category,
                startDate
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

    @DisplayName("여러 Chore을 저장할 수 있다.")
    @Test
    void appendChores() {
        //given
        House house = createHouse();
        Member member1 = createMember(house, "email1");
        Member member2 = createMember(house, "email2");

        Map<ScheduleIdDto, List<Member>> scheduleAllocatorsMap = new HashMap<>();
        for(int i=0; i<10; i++) {
            Schedule schedule = createSchedule(Category.COOKING, house);
            scheduleAllocatorsMap.put(new ScheduleIdDto(schedule.getId()), List.of(member1, member2));
        }
        LocalDate startDate = LocalDate.now();

        //when
        choreAppender.appendChores(scheduleAllocatorsMap, startDate);

        //then
        List<Chore> chores = choreRepository.findAll();
        List<ChoreMember> choreMembers = choreMemberRepository.findAll();
        assertThat(chores).hasSize(10);
        assertThat(choreMembers).hasSize(10 * 2);
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

    private Schedule createSchedule(Category category, House house) {
        return scheduleRepository.save(Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(category)
                .scheduleType(ScheduleType.REPEAT)
                .house(house)
                .sequence(1)
                .sequenceSize(1)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .build());
    }
}