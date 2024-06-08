package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleMemberFactoryTest extends IntegrationTestSupport {
    @Autowired
    ScheduleMemberFactory scheduleMemberFactory;

    @Autowired
    HouseRepository houseRepository;

    @DisplayName("분담방식이 고정이라면 ScheduleMember의 sequence가 모두 1인 ScheduleMember를 만든다.")
    @Test
    void createScheduleMembers_FIX() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        List<Member> members = List.of(member1, member2);
        Schedule schedule = createSchedule(DivisionType.FIX);

        //when
        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members, schedule);

        //then
        Assertions.assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence", "member")
                .containsExactly(
                        tuple(1, member1),
                        tuple(1, member2)
                );

     }

    @DisplayName("분담방식이 로테이션이라면 ScheduleMember의 sequence는 1부터 시작해서 1씩 증가하는 ScheduleMember를 만든다.")
    @ValueSource(ints = {0,8})
    @ParameterizedTest
    void createScheduleMembers_ROTATION() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        List<Member> members = List.of(member1, member2);
        Schedule schedule = createSchedule(DivisionType.ROTATION);

        //when
        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members, schedule);

        //then
        Assertions.assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence", "member")
                .containsExactly(
                        tuple(1, member1),
                        tuple(2, member2)
                );

    }

    private Member createMember() {
        return Member.builder()
                .email("email")
                .password("password")
                .build();
    }

    private Schedule createSchedule(DivisionType divisionType) {
        House house = createHouse();
        Category category = Category.COOKING;
        return Schedule.create("schedule", LocalDate.now(),
                LocalTime.of(11, 1), divisionType, ScheduleType.REPEAT, house, 1, category, LocalDate.now());
    }


    private House createHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }

}