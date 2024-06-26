package com.roundtable.roundtable.business.schedule;

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
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleMemberDetailDto;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleMemberReaderTest extends IntegrationTestSupport {
    @Autowired
    private ScheduleMemberReader scheduleMemberReader;

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
    void readScheduleMemberDetail() {
        //given
        House house = appendHouse();

        Member member1 = appendMember(house, "member1", "email1");
        Member member2 = appendMember(house, "member2", "email2");
        Member member3 = appendMember(house, "member3", "email3");

        Category category = Category.COOKING;

        Schedule schedule = appendSchedule(category, house);

        ScheduleMember scheduleMember1 = appendScheduleMember(schedule, member1, 1);
        ScheduleMember scheduleMember2 = appendScheduleMember(schedule, member2, 1);
        ScheduleMember scheduleMember3 = appendScheduleMember(schedule, member3, 1);

        //when
        List<ScheduleMemberDetailDto> scheduleMemberDetails =
                scheduleMemberReader.readScheduleMemberDetail(schedule.getId());

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
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
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
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member1, int sequence) {
        ScheduleMember scheduleMember = ScheduleMember.builder().schedule(schedule).member(member1).sequence(sequence).build();
        return scheduleMemberRepository.save(scheduleMember);
    }
}