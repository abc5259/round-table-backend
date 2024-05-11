package com.roundtable.roundtable.domain.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.domain.chore.dto.ChoreOfMemberDto;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Frequency;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ChoreQueryRepository choreQueryRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;


    @DisplayName("특정 하우스에서 특정 날짜에 특정 멤버가 해야할 일 목록을 조회할 수 있다.")
    @Test
    void findChoresOfMember() {
        //given
        House house = createHouse("code");
        House house2 = createHouse("code2");
        houseRepository.save(house);
        houseRepository.save(house2);

        Member member1 = createMember("member1", "member1", house);
        Member member2 = createMember("member2", "member2", house);
        Member member3 = createMember("member3", "member3", house2);
        memberRepository.saveAll(List.of(member1, member2, member3));

        Category category = Category.CLEANING;
        Category category2 = Category.GROCERY;

        LocalDate searchDate = LocalDate.of(2024, 2, 24);

        Schedule schedule1 = createSchedule(category, house, searchDate, "쓰레기 분리 수거1");
        Schedule schedule2 = createSchedule(category, house, searchDate, "쓰레기 분리 수거2");
        Schedule schedule3 = createSchedule(category2, house2, searchDate, "쓰레기 분리 수거2");
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        Chore chore1 = createChore(searchDate, schedule1, false);
        Chore chore2 = createChore(searchDate, schedule2, false);
        Chore chore3 = createChore(searchDate, schedule2, true);
        Chore chore4 = createChore(searchDate, schedule3, true);
        choreRepository.saveAll(List.of(chore1, chore2, chore3, chore4));

        ChoreMember choreMember1 = createChoreMember(chore1, member1);
        ChoreMember choreMember4 = createChoreMember(chore1, member2);
        ChoreMember choreMember2 = createChoreMember(chore2, member2);
        ChoreMember choreMember3 = createChoreMember(chore3, member1);
        ChoreMember choreMember5 = createChoreMember(chore4, member3);
        choreMemberRepository.saveAll(List.of(choreMember1, choreMember2, choreMember3, choreMember4, choreMember5));

        //when
        List<ChoreOfMemberDto> result = choreQueryRepository.findChoresOfMember(member1.getId(), searchDate, house.getId());

        //then
        assertThat(result).hasSize(2)
                .extracting("choreId", "name", "isCompleted", "startDate", "startTime", "category")
                .containsExactly(
                        tuple(
                                chore1.getId(),
                                schedule1.getName(),
                                chore1.isCompleted(),
                                chore1.getStartDate(),
                                schedule1.getStartTime(),
                                category),
                        tuple(
                                chore3.getId(),
                                schedule2.getName(),
                                chore3.isCompleted(),
                                chore3.getStartDate(),
                                schedule2.getStartTime(),
                                category)
                );

     }

     @DisplayName("특정 하우스에서 특정 날짜에 해야할 일 목록을 조회할 수 있다.")
     @Test
     void findChoresOfHouse() {
         //given
         House house = createHouse("code");
         House house2 = createHouse("code2");
         houseRepository.save(house);
         houseRepository.save(house2);

         Member member1 = createMember("member1", "member1", house);
         Member member2 = createMember("member2", "member2", house);
         Member member3 = createMember("member3", "member3", house2);
         memberRepository.saveAll(List.of(member1, member2, member3));

         Category category = Category.CLEANING;
         Category category2 = Category.COOKING;

         LocalDate searchDate = LocalDate.of(2024, 2, 24);

         Schedule schedule1 = createSchedule(category, house, searchDate, "쓰레기 분리 수거1");
         Schedule schedule2 = createSchedule(category, house, searchDate, "쓰레기 분리 수거2");
         Schedule schedule3 = createSchedule(category2, house2, searchDate, "쓰레기 분리 수거2");
         scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

         Chore chore1 = createChore(searchDate, schedule1, false);
         Chore chore2 = createChore(searchDate, schedule2, false);
         Chore chore3 = createChore(searchDate, schedule2, true);
         Chore chore4 = createChore(searchDate, schedule3, true);
         choreRepository.saveAll(List.of(chore1, chore2, chore3, chore4));

         ChoreMember choreMember1 = createChoreMember(chore1, member1);
         ChoreMember choreMember4 = createChoreMember(chore1, member2);
         ChoreMember choreMember2 = createChoreMember(chore2, member2);
         ChoreMember choreMember3 = createChoreMember(chore3, member1);
         ChoreMember choreMember5 = createChoreMember(chore4, member3);
         choreMemberRepository.saveAll(List.of(choreMember1, choreMember2, choreMember3, choreMember4, choreMember5));

         CursorPagination cursorPagination = new CursorPagination(0L, 10);
         //when
         List<ChoreMembersDetailDto> result = choreQueryRepository.findChoresOfHouse(
                 searchDate, house.getId(), cursorPagination);

         //then
         assertThat(result).hasSize(3)
                 .extracting("choreId", "name", "isCompleted", "startDate", "startTime", "memberNames", "category")
                 .containsExactly(
                         tuple(
                                 chore1.getId(),
                                 schedule1.getName(),
                                 chore1.isCompleted(),
                                 chore1.getStartDate(),
                                 schedule1.getStartTime(),
                                 member1.getName() + "," + member2.getName(),
                                 category),
                         tuple(
                                 chore2.getId(),
                                 schedule2.getName(),
                                 chore2.isCompleted(),
                                 chore2.getStartDate(),
                                 schedule2.getStartTime(),
                                 member2.getName(),
                                 category),
                         tuple(
                                 chore3.getId(),
                                 schedule2.getName(),
                                 chore3.isCompleted(),
                                 chore3.getStartDate(),
                                 schedule2.getStartTime(),
                                 member1.getName(),
                                 category)
                 );

     }

    @DisplayName("특정 하우스에서 특정 날짜에 해야할 일 목록을 조회할 때 limit 수만큼 가져온다.")
    @Test
    void findChoresOfHouse_limit() {
        //given
        House house = createHouse("code1");
        House house2 = createHouse("code2");
        houseRepository.save(house);
        houseRepository.save(house2);

        Member member1 = createMember("member1", "member1", house);
        Member member2 = createMember("member2", "member2", house);
        Member member3 = createMember("member3", "member3", house2);
        memberRepository.saveAll(List.of(member1, member2, member3));

        Category category = Category.CLEANING;
        Category category2 = Category.GROCERY;

        LocalDate searchDate = LocalDate.of(2024, 2, 24);

        Schedule schedule1 = createSchedule(category, house, searchDate, "쓰레기 분리 수거1");
        Schedule schedule2 = createSchedule(category, house, searchDate, "쓰레기 분리 수거2");
        Schedule schedule3 = createSchedule(category2, house2, searchDate, "쓰레기 분리 수거2");
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        Chore chore1 = createChore(searchDate, schedule1, false);
        Chore chore2 = createChore(searchDate, schedule2, false);
        Chore chore3 = createChore(searchDate, schedule2, true);
        Chore chore4 = createChore(searchDate, schedule3, true);
        choreRepository.saveAll(List.of(chore1, chore2, chore3, chore4));

        ChoreMember choreMember1 = createChoreMember(chore1, member1);
        ChoreMember choreMember4 = createChoreMember(chore1, member2);
        ChoreMember choreMember2 = createChoreMember(chore2, member2);
        ChoreMember choreMember3 = createChoreMember(chore3, member1);
        ChoreMember choreMember5 = createChoreMember(chore4, member3);
        choreMemberRepository.saveAll(List.of(choreMember1, choreMember2, choreMember3, choreMember4, choreMember5));

        CursorPagination cursorPagination = new CursorPagination(0L, 2);

        //when
        List<ChoreMembersDetailDto> result = choreQueryRepository.findChoresOfHouse(
                searchDate, house.getId(), cursorPagination);

        //then
        assertThat(result).hasSize(2)
                .extracting("choreId", "name", "isCompleted", "startDate", "startTime", "memberNames", "category")
                .containsExactly(
                        tuple(
                                chore1.getId(),
                                schedule1.getName(),
                                chore1.isCompleted(),
                                chore1.getStartDate(),
                                schedule1.getStartTime(),
                                member1.getName() + "," + member2.getName(),
                                category),
                        tuple(
                                chore2.getId(),
                                schedule2.getName(),
                                chore2.isCompleted(),
                                chore2.getStartDate(),
                                schedule2.getStartTime(),
                                member2.getName(),
                                category)
                );

    }

    @DisplayName("특정 하우스에서 특정 날짜에 해야할 일 목록을 조회할 때 특정 chore id 이후부터 조회한다.")
    @Test
    void findChoresOfHouse_lasChoreId() {
        //given
        House house = createHouse("code");
        House house2 = createHouse("code2");
        houseRepository.save(house);
        houseRepository.save(house2);

        Member member1 = createMember("member1", "member1", house);
        Member member2 = createMember("member2", "member2", house);
        Member member3 = createMember("member3", "member3", house2);
        memberRepository.saveAll(List.of(member1, member2, member3));

        Category category = Category.CLEANING;
        Category category2 = Category.COOKING;

        LocalDate searchDate = LocalDate.of(2024, 2, 24);

        Schedule schedule1 = createSchedule(category, house, searchDate, "쓰레기 분리 수거1");
        Schedule schedule2 = createSchedule(category, house, searchDate, "쓰레기 분리 수거2");
        Schedule schedule3 = createSchedule(category2, house2, searchDate, "쓰레기 분리 수거2");
        scheduleRepository.saveAll(List.of(schedule1, schedule2, schedule3));

        Chore chore1 = createChore(searchDate, schedule1, false);
        Chore chore2 = createChore(searchDate, schedule2, false);
        Chore chore3 = createChore(searchDate, schedule2, true);
        Chore chore4 = createChore(searchDate, schedule3, true);
        choreRepository.saveAll(List.of(chore1, chore2, chore3, chore4));

        ChoreMember choreMember1 = createChoreMember(chore1, member1);
        ChoreMember choreMember4 = createChoreMember(chore1, member2);
        ChoreMember choreMember2 = createChoreMember(chore2, member2);
        ChoreMember choreMember3 = createChoreMember(chore3, member1);
        ChoreMember choreMember5 = createChoreMember(chore4, member3);
        choreMemberRepository.saveAll(List.of(choreMember1, choreMember4, choreMember2, choreMember3, choreMember5));

        CursorPagination cursorPagination = new CursorPagination(chore1.getId(), 3);
        //when
        List<ChoreMembersDetailDto> result = choreQueryRepository.findChoresOfHouse(
                searchDate, house.getId(), cursorPagination);

        //then
        assertThat(result).hasSize(2)
                .extracting("choreId", "name", "isCompleted", "startDate", "startTime", "memberNames", "category")
                .containsExactly(
                        tuple(
                                chore2.getId(),
                                schedule2.getName(),
                                chore2.isCompleted(),
                                chore2.getStartDate(),
                                schedule2.getStartTime(),
                                member2.getName(),
                                category),
                        tuple(
                                chore3.getId(),
                                schedule2.getName(),
                                chore3.isCompleted(),
                                chore3.getStartDate(),
                                schedule2.getStartTime(),
                                member1.getName(),
                                category)
                );

    }


    private House createHouse(String code) {
        return House.builder().name("house1").inviteCode(InviteCode.builder().code(code).build()).build();
    }

    private Member createMember(String email, String name, House house) {
        return Member.builder().name(name).email(email).house(house).password("password").build();
    }

    private ChoreMember createChoreMember(Chore chore1, Member member1) {
        return ChoreMember.builder()
                .chore(chore1)
                .member(member1)
                .build();
    }

    private Chore createChore(LocalDate searchDate, Schedule schedule1, boolean isCompleted) {
        return Chore.builder()
                .startDate(searchDate)
                .schedule(schedule1)
                .isCompleted(isCompleted)
                .build();
    }

    private Schedule createSchedule(Category category, House house, LocalDate searchDate, String name) {
        return Schedule.builder()
                .name(name)
                .startTime(LocalTime.of(1, 0))
                .category(category)
                .house(house)
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .startDate(searchDate)
                .divisionType(DivisionType.FIX)
                .build();
    }
}