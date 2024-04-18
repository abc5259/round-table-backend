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
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreMemberBulkRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;

    @Autowired
    private ChoreMemberBulkRepository choreMemberBulkRepository;

    @DisplayName("여러개의 ChoreMember을 한번에 insert할 수 있다.")
    @Test
    void insertChoreMembers() {
        //given
        House house = createHouse();
        Category category = createCategory(house);
        Schedule schedule = createSchedule(category, house);

        final int size = 10;

        List<Chore> chores = IntStream.rangeClosed(1, size)
                .mapToObj(i -> {
                    Chore chore = createChore(schedule);
                    ChoreMember choreMember = ChoreMember.builder()
                            .member(createMember(house, "user" + i + "@example.com"))
                            .build();
                    chore.addChoreMembers(List.of(choreMember));
                    return chore;
                })
                .toList();

        choreRepository.saveAll(chores);
        ChoreUniqueMatcher choreUniqueMatcher = new ChoreUniqueMatcher(chores);

        //when
        choreMemberBulkRepository.insertChoreMembers(choreUniqueMatcher, chores);

        //then
        List<ChoreMember> result = choreMemberRepository.findAll();
        assertThat(result).hasSize(size);

        List<Long> expectedIdChores = result.stream().map(choreMember -> choreMember.getChore().getId()).sorted().toList();
        List<Long> idChores = chores.stream().map(Chore::getId).sorted().toList();
        assertThat(idChores).isEqualTo(expectedIdChores);
    }

    @DisplayName("여러개의 ChoreMember을 한번에 insert할 수 있다.")
    @Test
    void saveAll() {
        //given
        House house = createHouse();
        Category category = createCategory(house);
        Schedule schedule = createSchedule(category, house);
        Chore chore = createChore(schedule);
        choreRepository.save(chore);

        final int size = 10;
        List<ChoreMember> choreMembers = IntStream.rangeClosed(1, size)
                .mapToObj(i ->
                        ChoreMember.builder()
                                    .chore(chore)
                                    .member(createMember(house, "user" + i + "@example.com"))
                                    .build())
                .toList();

        //when
        choreMemberBulkRepository.saveAll(choreMembers);

        //then
        List<ChoreMember> result = choreMemberRepository.findAll();
        assertThat(result).hasSize(size);

    }

    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code1").build()).build();
        return houseRepository.save(house);
    }

    private Member createMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).house(house).password("password").build();
        return memberRepository.save(member);
    }

    private Schedule createSchedule(Category category, House house) {
        Schedule schedule = Schedule.builder()
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
        return scheduleRepository.save(schedule);
    }

    private Category createCategory(House house) {
        Category category = Category.builder().name("category").point(100).house(house).build();
        return categoryRepository.save(category);
    }

    private Chore createChore(Schedule schedule) {
        return Chore.builder()
                .startDate(LocalDate.now())
                .schedule(schedule)
                .isCompleted(false)
                .build();
    }
}