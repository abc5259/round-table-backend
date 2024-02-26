package com.roundtable.roundtable.implement.chore;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.category.dto.CategoryDetailV1Dto;
import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.chore.ChoreMemberRepository;
import com.roundtable.roundtable.entity.chore.ChoreRepository;
import com.roundtable.roundtable.entity.chore.dto.ChoreDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.entity.common.CursorPagination;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleRepository;
import com.roundtable.roundtable.implement.common.CursorBasedResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ChoreReaderTest {

    @Autowired
    private ChoreReader choreReader;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;

    @DisplayName("특정 하우스에서 특정 날짜에 특정 멤버가 해야할 일 목록을 읽을 수 있다.")
    @Test
    void readByIdAndDateInHouse() {
        //given
        House house = createHouse();
        House house2 = createHouse();
        houseRepository.save(house);
        houseRepository.save(house2);

        Member member1 = createMember("member1", "member1", house);
        Member member2 = createMember("member2", "member2", house);
        Member member3 = createMember("member3", "member3", house2);
        memberRepository.saveAll(List.of(member1, member2, member3));

        Category category = Category.builder().name("분리 수거").point(100).house(house).build();
        Category category2 = Category.builder().name("분리 수거").point(100).house(house2).build();
        categoryRepository.save(category);
        categoryRepository.save(category2);

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
        List<ChoreDetailV1Dto> result = choreReader.readByIdAndDateInHouse(member1.getId(), searchDate, house.getId());

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
                                new CategoryDetailV1Dto(
                                        category.getId(),
                                        category.getName(),
                                        category.getPoint()
                                )),
                        tuple(
                                chore3.getId(),
                                schedule2.getName(),
                                chore3.isCompleted(),
                                chore3.getStartDate(),
                                schedule2.getStartTime(),
                                new CategoryDetailV1Dto(
                                        category.getId(),
                                        category.getName(),
                                        category.getPoint()
                                ))
                );

    }

    @DisplayName("특정 하우스에서 특정 날짜에 해야할 일 목록을 조회할 수 있다.")
    @Test
    void readChoreMembersByDateSinceLastChoreIdInHouse() {
        //given
        House house = createHouse();
        House house2 = createHouse();
        houseRepository.save(house);
        houseRepository.save(house2);

        Member member1 = createMember("member1", "member1", house);
        Member member2 = createMember("member2", "member2", house);
        Member member3 = createMember("member3", "member3", house2);
        memberRepository.saveAll(List.of(member1, member2, member3));

        Category category = Category.builder().name("분리 수거").point(100).house(house).build();
        Category category2 = Category.builder().name("분리 수거").point(100).house(house2).build();
        categoryRepository.save(category);
        categoryRepository.save(category2);

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
        CursorBasedResponse<List<ChoreResponse>> result = choreReader.readChoreMembersByDateSinceLastChoreIdInHouse(
                searchDate, house.getId(), cursorPagination);

        //then
        assertThat(result.getLastCursorId()).isEqualTo(chore3.getId());
        assertThat(result.getContent()).hasSize(3)
                .extracting("choreId", "name", "isCompleted", "startDate", "startTime", "memberNames", "category")
                .containsExactly(
                        tuple(
                                chore1.getId(),
                                schedule1.getName(),
                                chore1.isCompleted(),
                                chore1.getStartDate(),
                                schedule1.getStartTime(),
                                List.of(member1.getName(), member2.getName()),
                                new CategoryDetailV1Dto(
                                        category.getId(),
                                        category.getName(),
                                        category.getPoint()
                                )),
                        tuple(
                                chore2.getId(),
                                schedule2.getName(),
                                chore2.isCompleted(),
                                chore2.getStartDate(),
                                schedule2.getStartTime(),
                                List.of(member2.getName()),
                                new CategoryDetailV1Dto(
                                        category.getId(),
                                        category.getName(),
                                        category.getPoint()
                                )),
                        tuple(
                                chore3.getId(),
                                schedule2.getName(),
                                chore3.isCompleted(),
                                chore3.getStartDate(),
                                schedule2.getStartTime(),
                                List.of(member1.getName()),
                                new CategoryDetailV1Dto(
                                        category.getId(),
                                        category.getName(),
                                        category.getPoint()
                                ))
                );

    }

    private House createHouse() {
        return House.builder().name("house").build();
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
                .build();
    }

}