package com.roundtable.roundtable.domain.feedback;

import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @DisplayName("자신이 수행한 스케줄에 피드백을 생성하면 예외가 발생한다.")
    @Test
    void createFeedbackForOwnScheduleThrowsException() {
        //given
        Emoji emoji = Emoji.FIRE;
        String message = "깔끔해";
        House house = House.builder().id(1L).name("부산 하우스").inviteCode(InviteCode.builder().code("code").build()).build();
        Member member1 = Member.builder().id(1L).email("email1").house(house).password("password").build();
        Member member2 = Member.builder().id(2L).email("email2").house(house).password("password").build();
        Schedule schedule = Schedule.builder().id(1L).house(house).build();
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.builder()
                .id(1L)
                .schedule(schedule)
                .build();
        ScheduleCompletionMember scheduleCompletionMember1 = ScheduleCompletionMember.builder().scheduleCompletion(scheduleCompletion).member(member1).build();
        ScheduleCompletionMember scheduleCompletionMember2 = ScheduleCompletionMember.builder().scheduleCompletion(scheduleCompletion).member(member2).build();

        //when then
        Assertions.assertThatThrownBy(() -> Feedback.create(emoji, message, scheduleCompletion, member1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자신이 완료한 스케줄에 피드백을 남길 수 없습니다.");
    }

    @DisplayName("자신과 다른 하우스의 스케줄에 피드백을 생성하려하면 예외가 발생한다.")
    @Test
    void createFeedbackWhenOtherHouseScheduleThrowsException() {
        //given
        Emoji emoji = Emoji.FIRE;
        String message = "깔끔해";
        House house1 = House.builder().id(1L).name("부산 하우스").inviteCode(InviteCode.builder().code("code").build()).build();
        House house2 = House.builder().id(2L).name("서울 하우스").inviteCode(InviteCode.builder().code("code").build()).build();
        Member member1 = Member.builder().id(1L).email("email1").house(house1).password("password").build();
        Member member2 = Member.builder().id(2L).email("email2").house(house2).password("password").build();
        Schedule schedule = Schedule.builder().id(1L).house(house2).build();
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.builder()
                .id(1L)
                .schedule(schedule)
                .build();
        ScheduleCompletionMember scheduleCompletionMember2 = ScheduleCompletionMember.builder().scheduleCompletion(scheduleCompletion).member(member2).build();

        //when then
        Assertions.assertThatThrownBy(() -> Feedback.create(emoji, message, scheduleCompletion, member1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 하우스의 사용자만 피드백을 보낼 수 있습니다.");
    }
}