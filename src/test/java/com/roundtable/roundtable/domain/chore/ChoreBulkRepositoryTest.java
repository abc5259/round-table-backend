package com.roundtable.roundtable.domain.chore;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Frequency;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @DisplayName("여러개의 Chore을 한번에 insert할 수 있다.")
    @Test
    void insertChores() {
        //given
        House house = createHouse();
        houseRepository.save(house);

        Member member = createMember(house);
        memberRepository.save(member);

        Category category = Category.builder().name("분리 수거").point(100).house(house).build();
        categoryRepository.save(category);

        Schedule schedule = createSchedule(category, house);
        scheduleRepository.save(schedule);

        List<Chore> chores = new ArrayList<>();
        for(int i=0; i<5; i++) {
            chores.add(createChore(schedule));
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

    private House createHouse() {
        return House.builder().name("house1").inviteCode(InviteCode.builder().code("code1").build()).build();
    }

    private Member createMember(House house) {
        return Member.builder().name("name").email("email").house(house).password("password").build();
    }

    private Chore createChore(Schedule schedule) {
        return Chore.builder()
                .startDate(LocalDate.now())
                .schedule(schedule)
                .isCompleted(false)
                .matchKey("matchkey")
                .build();
    }

    private Schedule createSchedule(Category category, House house) {
        return Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(category)
                .house(house)
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .build();
    }

}