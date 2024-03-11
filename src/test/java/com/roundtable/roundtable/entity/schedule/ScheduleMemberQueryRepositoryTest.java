package com.roundtable.roundtable.entity.schedule;

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

        ScheduleMember scheduleMember1 = appendScheduleMember(schedule, member1);
        ScheduleMember scheduleMember2 = appendScheduleMember(schedule, member2);
        ScheduleMember scheduleMember3 = appendScheduleMember(schedule, member3);

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
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member1) {
        ScheduleMember scheduleMember = ScheduleMember.builder().schedule(schedule).member(member1).sequence(1).build();
        return scheduleMemberRepository.save(scheduleMember);
    }

    private Category appendCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        return categoryRepository.save(category);
    }
}