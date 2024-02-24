package com.roundtable.roundtable.entity.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.category.dto.CategoryDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreDetailV1Dto;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleRepository;
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
class ChoreQueryRepositoryTest {

    @Autowired
    private ChoreQueryRepository choreQueryRepository;

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


    @DisplayName("")
    @Test
    void findByIdAndDate() {
        //given
        House house = createHouse();
        houseRepository.save(house);
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");
        memberRepository.saveAll(List.of(member1, member2));

        Category category = Category.builder().name("분리 수거").point(100).house(house).build();
        categoryRepository.save(category);
        System.out.println(category.getId());

        LocalDate searchDate = LocalDate.of(2024, 2, 24);

        Schedule schedule1 = createSchedule(category, house, searchDate, "쓰레기 분리 수거1");
        Schedule schedule2 = createSchedule(category, house, searchDate, "쓰레기 분리 수거2");
        scheduleRepository.saveAll(List.of(schedule1, schedule2));

        Chore chore1 = createChore(searchDate, schedule1, false);
        Chore chore2 = createChore(searchDate, schedule2, false);
        Chore chore3 = createChore(searchDate, schedule2, true);
        choreRepository.saveAll(List.of(chore1, chore2, chore3));

        ChoreMember choreMember1 = createChoreMember(chore1, member1);
        ChoreMember choreMember2 = createChoreMember(chore2, member2);
        ChoreMember choreMember3 = createChoreMember(chore3, member1);
        ChoreMember choreMember4 = createChoreMember(chore1, member2);
        choreMemberRepository.saveAll(List.of(choreMember1, choreMember2, choreMember3, choreMember4));

        //when
        List<ChoreDetailV1Dto> result = choreQueryRepository.findByIdAndDate(member1.getId(), searchDate, house.getId());

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

    private static House createHouse() {
        return House.builder().name("house").build();
    }

    private static Member createMember(String member1) {
        return Member.builder().email(member1).password("password").build();
    }

    private static ChoreMember createChoreMember(Chore chore1, Member member1) {
        return ChoreMember.builder()
                .chore(chore1)
                .member(member1)
                .build();
    }

    private static Chore createChore(LocalDate searchDate, Schedule schedule1, boolean isCompleted) {
        return Chore.builder()
                .startDate(searchDate)
                .schedule(schedule1)
                .isCompleted(isCompleted)
                .build();
    }

    private static Schedule createSchedule(Category category, House house, LocalDate searchDate, String name) {
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