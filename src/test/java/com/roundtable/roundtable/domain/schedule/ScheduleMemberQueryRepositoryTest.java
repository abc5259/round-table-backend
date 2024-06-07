package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.schedule.DivisionType.*;
import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleMemberDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
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
    private ScheduleMemberRepository scheduleMemberRepository;
    
    @DisplayName("Schedule에 따른 ScheduleMember의 세부사항을 조회할 수 있다.")
    @Test
    void findScheduleMemberDetail() {
        //given
        House house = appendHouse();

        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");

        Schedule schedule = appendSchedule(Category.CLEANING, house, 1, 3);

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

    @DisplayName("스케줄의 오늘 담당자를 조회할 수 있다.")
    @Test
    void findAllocators() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");

        Schedule schedule1 = appendSchedule(Category.CLEANING, house, 1, 3);

        ScheduleMember scheduleMember1 = appendScheduleMember(schedule1, member1, 1);
        ScheduleMember scheduleMember2 = appendScheduleMember(schedule1, member2, 1);
        ScheduleMember scheduleMember3 = appendScheduleMember(schedule1, member3, 2);

        Schedule schedule2 = appendSchedule(Category.CLEANING, house, 1, 3);

        ScheduleMember scheduleMember4 = appendScheduleMember(schedule2, member1, 1);
        ScheduleMember scheduleMember5 = appendScheduleMember(schedule2, member2, 2);
        ScheduleMember scheduleMember6 = appendScheduleMember(schedule2, member3, 3);

        //when
        Map<ScheduleIdDto, List<Member>> allocators = scheduleMemberQueryRepository.findAllocators(List.of(schedule1, schedule2));

        //then
        assertThat(allocators.get(new ScheduleIdDto(schedule1.getId()))).hasSize(2)
                .extracting("id")
                .contains(member1.getId(), member2.getId());

        assertThat(allocators.get(new ScheduleIdDto(schedule2.getId()))).hasSize(1)
                .extracting("id")
                .contains(member1.getId());
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

    private Schedule appendSchedule(Category category, House house, int sequence, int sequenceSize) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(sequence)
                .sequenceSize(sequenceSize)
                .house(house)
                .divisionType(ROTATION)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Schedule appendSchedule(Category category,
                                    House house,
                                    String name,
                                    LocalDate startDate,
                                    DivisionType divisionType, int sequenceSize) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .category(category)
                .startDate(startDate)
                .startTime(LocalTime.of(11,11))
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
}