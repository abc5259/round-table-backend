package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.schedule.DivisionType.*;
import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleMemberDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleMemberQueryRepositoryTest extends IntegrationTestSupport {
    
    @Autowired
    private ScheduleMemberQueryRepository scheduleMemberQueryRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleMemberRepository scheduleMemberRepository;
    
    @DisplayName("Schedule에 따른 ScheduleMember의 세부사항을 조회할 수 있다.")
    @Test
    void findScheduleMemberDetail() {
        //given
        House house = appendHouse();

        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");

        Category category = appendCategory(house);

        Schedule schedule = appendSchedule(category, house);

        ScheduleMember scheduleMember1 = appendScheduleMember(schedule, member1, 1);
        ScheduleMember scheduleMember2 = appendScheduleMember(schedule, member2, 1);
        ScheduleMember scheduleMember3 = appendScheduleMember(schedule, member3, 1);

        //when
        List<ScheduleMemberDetailDto> scheduleMemberDetails =
                scheduleMemberQueryRepository.findScheduleMemberDetail(schedule.getId());

        //then
        assertThat(scheduleMemberDetails).hasSize(3)
                .extracting("memberId", "name", "sequence")
                .containsExactly(
                        Tuple.tuple(member1.getId(), member1.getName(), scheduleMember1.getSequence()),
                        Tuple.tuple(member2.getId(), member2.getName(), scheduleMember2.getSequence()),
                        Tuple.tuple(member3.getId(), member3.getName(), scheduleMember3.getSequence())
                );
           
     }

     @DisplayName("여러 schedule과 각각의 schedule의 담당 순서에 맞게 ScheduleMember를 조회할 수 있다.")
     @Test
     void findMembersBySchedulesWithDivisionConditions() {
         //given
         House house = appendHouse();

         Member member1 = appendMember(house, "member1", "email1");
         Member member2 = appendMember(house, "member2", "email2");
         Member member3 = appendMember(house, "member3", "email3");

         Category category = appendCategory(house);

         //한번만
         Frequency frequency1 = Frequency.builder().frequencyType(ONCE).frequencyInterval(0).build();

         //1일 간격
         Frequency frequency2 = Frequency.builder().frequencyType(DAILY).frequencyInterval(1).build();
         //2일 간격
         Frequency frequency3 = Frequency.builder().frequencyType(DAILY).frequencyInterval(2).build();

         //수요일마다
         Frequency frequency4 = Frequency.builder().frequencyType(WEEKLY).frequencyInterval(4).build();

         LocalDate targetDate = LocalDate.of(2024, 3, 20); //수요일

         //
         List<Schedule> schedules = List.of(
                 appendSchedule(category, house, "schedule1", frequency1, targetDate, FIX, 3),
                 appendSchedule(category, house, "schedule2", frequency2, LocalDate.of(2024,3,17), ROTATION, 3),
                 appendSchedule(category, house, "schedule3", frequency3, LocalDate.of(2024,3,16), ROTATION, 3),
                 appendSchedule(category, house, "schedule4", frequency3, LocalDate.of(2024,3,16), FIX, 3),
                 appendSchedule(category, house, "schedule5", frequency4, LocalDate.of(2024,3,6), ROTATION, 3)
         );

         for(int i=0; i<5; i++) {
             appendScheduleMember(schedules.get(i), member1, 1);
             appendScheduleMember(schedules.get(i), member2, 2);
             appendScheduleMember(schedules.get(i), member3, 3);
         }



         //when
         List<ScheduleMember> result = scheduleMemberQueryRepository.findMembersBySchedulesWithDivisionConditions(
                 schedules, LocalDate.of(2024,3,20));

         //then
         assertThat(result).hasSize(9);

         Map<Schedule, List<ScheduleMember>> scheduleMap = result.stream()
                 .collect(Collectors.groupingBy(ScheduleMember::getSchedule));

         assertThat(scheduleMap).hasSize(5);
         for(Schedule schedule: schedules) {
             assertThat(scheduleMap.containsKey(schedule)).isTrue();
         }
         verifyScheduleMembers(scheduleMap, schedules.get(0), 3, member1, member2, member3);
         verifyScheduleMembers(scheduleMap, schedules.get(1), 1, member1);
         verifyScheduleMembers(scheduleMap, schedules.get(2), 1, member3);
         verifyScheduleMembers(scheduleMap, schedules.get(3), 3, member1, member2, member3);
         verifyScheduleMembers(scheduleMap, schedules.get(4), 1, member3);
      }

    private void verifyScheduleMembers(Map<Schedule, List<ScheduleMember>> scheduleMap, Schedule schedule, int expectedSize, Member... expectedMembers) {
        assertThat(scheduleMap.containsKey(schedule)).isTrue();
        assertThat(scheduleMap.get(schedule)).hasSize(expectedSize)
                .extracting("member")
                .containsExactlyInAnyOrder(expectedMembers);
    }


    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Member appendMember(House house, String name, String email) {
        Member member = Member.builder().name(name).email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Schedule appendSchedule(Category category, House house) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .frequency(Frequency.builder().frequencyType(DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(FIX)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Schedule appendSchedule(Category category,
                                    House house,
                                    String name,
                                    Frequency frequency,
                                    LocalDate startDate,
                                    DivisionType divisionType, int sequenceSize) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(startDate)
                .startTime(LocalTime.of(11,11))
                .frequency(frequency)
                .sequence(1)
                .sequenceSize(sequenceSize)
                .house(house)
                .divisionType(divisionType)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member1, int sequence) {
        ScheduleMember scheduleMember = ScheduleMember.builder().schedule(schedule).member(member1).sequence(sequence).build();
        return scheduleMemberRepository.save(scheduleMember);
    }

    private Category appendCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        return categoryRepository.save(category);
    }
}