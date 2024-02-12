package com.roundtable.roundtable.implement.schedule;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.Member.MemberBuilder;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.ScheduleMember;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleMemberFactoryTest {
    @Autowired
    ScheduleMemberFactory scheduleMemberFactory;

    @DisplayName("분담방식이 고정이라면 ScheduleMember의 sequence가 1인 ScheduleMember를 만든다.")
    @Test
    void createScheduleMembers_FIX() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        List<Member> members = List.of(member1, member2);
        DivisionType targetDivisionType = DivisionType.FIX;

        //when
        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members, targetDivisionType);

        //then
        Assertions.assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence", "member")
                .containsExactly(
                        tuple(1, member1),
                        tuple(1, member2)
                );

     }

    @DisplayName("분담방식이 로테이션이라면 ScheduleMember의 sequence는 1부터 시작해서 1씩 증가하는 ScheduleMember를 만든다.")
    @Test
    void createScheduleMembers_ROTATION() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        List<Member> members = List.of(member1, member2);
        DivisionType targetDivisionType = DivisionType.ROTATION;

        //when
        List<ScheduleMember> scheduleMembers = scheduleMemberFactory.createScheduleMembers(members, targetDivisionType);

        //then
        Assertions.assertThat(scheduleMembers).hasSize(2)
                .extracting("sequence", "member")
                .containsExactly(
                        tuple(1, member1),
                        tuple(2, member2)
                );

    }

    private static Member createMember() {
        return Member.builder()
                .email("email")
                .password("password")
                .build();
    }
}