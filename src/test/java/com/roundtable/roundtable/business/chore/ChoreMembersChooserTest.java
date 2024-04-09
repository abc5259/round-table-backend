package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChoreMembersChooserTest {

    @DisplayName("집안일 담당자를 ScheduleMember를 통해 찾는다.")
    @Test
    void chooseChoreMember() {
        //given

        Schedule schedule = Schedule.builder().sequence(1).build();


        Member member1 = Member.builder().email("email1").build();
        ScheduleMember scheduleMember1 = ScheduleMember.builder().member(member1).schedule(schedule).sequence(1).build();

        Member member2 = Member.builder().email("email2").build();
        ScheduleMember scheduleMember2 = ScheduleMember.builder().member(member2).schedule(schedule).sequence(2).build();

        ChoreMembersChooser choreMembersChooser = new ChoreMembersChooser();

        //when
        List<Member> result = choreMembersChooser.chooseChoreMembers(List.of(scheduleMember1, scheduleMember2));

        //then
        Assertions.assertThat(result).hasSize(1)
                .contains(member1);

     }
}