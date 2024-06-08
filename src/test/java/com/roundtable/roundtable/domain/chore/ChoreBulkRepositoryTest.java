package com.roundtable.roundtable.domain.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
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
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreBulkRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ChoreBulkRepository choreBulkRepository;

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

    @DisplayName("여러개의 Chore을 한번에 insert할 수 있다.")
    @Test
    void insertChores() {
        //given
        House house = createHouse();
        houseRepository.save(house);

        Member member = createMember(house, "email");

        Category category = Category.CLEANING;

        Schedule schedule = createSchedule(category, house);
        scheduleRepository.save(schedule);

        List<Chore> chores = new ArrayList<>();
        for(int i=0; i<5; i++) {
            chores.add(createChore(schedule, "matchKey"));
        }

        //when
        List<Chore> result = choreBulkRepository.insertChores(chores);

        //then
        List<Long> distinctId = result.stream().map(Chore::getId).distinct().toList();
        assertThat(distinctId).hasSize(chores.size());
        for (Chore chore : result) {
            assertThat(chore.getId()).isNotNull();
            assertThat(chore.getMatchKey()).isNotNull();
        }

    }

    @DisplayName("여러개의 Chore을 chunkSize로 나눠서 insert할 수 있다.")
    @Test
    void saveAll() {
        //given
        House house = createHouse();
        houseRepository.save(house);

        Member member1 = createMember(house, "email1");
        Member member2 = createMember(house, "email2");

        Category category = Category.CLEANING;

        Schedule schedule = createSchedule(category, house);
        scheduleRepository.save(schedule);


        List<Chore> chores = new ArrayList<>();

        final int SAVE_CHORE_SIZE = 10;

        for(int i=0; i<SAVE_CHORE_SIZE; i++) {
            Chore chore = createChore(schedule, null);
            chore.addChoreMembers(List.of(
                    ChoreMember.createBulkChoreMember(member1),
                    ChoreMember.createBulkChoreMember(member2)
            ));
            chores.add(chore);
        }

        //when
        choreBulkRepository.saveAll(chores, 5);

        //then
        List<Chore> findChores = choreRepository.findAll();
        List<ChoreMember> choreMembers = choreMemberRepository.findAll();
        assertThat(findChores).hasSize(SAVE_CHORE_SIZE);
        assertThat(choreMembers).hasSize(SAVE_CHORE_SIZE * 2);
    }

    private House createHouse() {
        return House.builder().name("house1").inviteCode(InviteCode.builder().code("code1").build()).build();
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
        return Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(category)
                .scheduleType(ScheduleType.REPEAT)
                .house(house)
                .sequence(1)
                .sequenceSize(1)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .build();
    }

}